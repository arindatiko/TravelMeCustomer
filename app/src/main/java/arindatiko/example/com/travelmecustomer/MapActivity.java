package arindatiko.example.com.travelmecustomer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.config.GoogleDirectionConfiguration;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import arindatiko.example.com.travelmecustomer.adapter.OrderMapAdapter;
import arindatiko.example.com.travelmecustomer.model.Jarak;
import arindatiko.example.com.travelmecustomer.model.Kamar;
import arindatiko.example.com.travelmecustomer.model.Menu;
import arindatiko.example.com.travelmecustomer.model.MyChoice;
import arindatiko.example.com.travelmecustomer.model.Rekomendasi;
import arindatiko.example.com.travelmecustomer.model.Wisata;
import arindatiko.example.com.travelmecustomer.util.GPSTracker;
import arindatiko.example.com.travelmecustomer.util.SessionManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.media.CamcorderProfile.get;
import static arindatiko.example.com.travelmecustomer.LetsActivity.MYCHOICE;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, DirectionCallback {

    private ArrayList<LatLng> dataMarker = new ArrayList<>();

    private List<String> akses = new ArrayList<>();
    private List<Double> daftarJarak = new ArrayList<>();
    private List<LatLng> waypoint = new ArrayList<>();
    private List<Wisata> travels = new ArrayList<>();
    private List<Kamar> hotels = new ArrayList<>();
    private List<Menu> menus = new ArrayList<>();

    //public static MyChoice myChoice = new MyChoice();
    //RekomendasiActivity rekomendasiActivity = new RekomendasiActivity();

    private GPSTracker gps;
    private GoogleMap mMap;
    private LatLng asal, destination;
    private double lat, lng;
    private Double total_km, jasa, biaya_tambahan, rekomendasi, budgetBaru;

    SharedPreferences sharedPreferences = null;
    SessionManager sessionManager;

    /*@BindView(R.id.rc_order)
    RecyclerView rc_order;*/
    @BindView(R.id.btnOrder)
    Button btnOrder;
    @BindView(R.id.txtNominal)
    TextView txtNominal;
    /*@BindView(R.id.pb_budget)
    ProgressBar pbBudget;*//*
    @BindView(R.id.tv_my_budget)
    TextView tvMyBudget;
    @BindView(R.id.tv_total_budget)
    TextView tvTotalBudget;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        sharedPreferences = getApplicationContext().getSharedPreferences("myTravel", Context.MODE_PRIVATE);
        sessionManager = new SessionManager(this);

        //myChoice = getIntent().getParcelableExtra(MYCHOICE);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MapActivity.this, "cek", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RekomendasiActivity.currentFragment = RekomendasiActivity.RESTAURANT;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("Activity", getClass().getSimpleName());

        String id_wisata = sharedPreferences.getString("id_wisata", "");
        String id_kamar = sharedPreferences.getString("id_kamar", "");
        String id_menu = sharedPreferences.getString("id_menu", "");
        String id_rute = sharedPreferences.getString("id_rute", "");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        gps = new GPSTracker(MapActivity.this);
        if(gps.canGetLocation()){
            lat = gps.getLatitude();
            lng = gps.getLongitude();
            asal = new LatLng(lat, lng);
            //Toast.makeText(this, (String.valueOf(gps.getLatitude()) + ", " +String.valueOf(gps.getLongitude())), Toast.LENGTH_SHORT).show();
        }
        mMap.setMyLocationEnabled(true);
        //currentLoc = loc.getLatitude() + "," + loc.getLongitude();
        sessionManager.setLoc(String.valueOf(asal));

        waterFallGetApi(id_wisata, id_kamar, id_menu);
    }

    private void waterFallGetApi(String idWisata, final String idKamar, final String idMenu){
        API.service_post.package_recomendation_wisata(idWisata, "wisata").enqueue(new Callback<ArrayList<Wisata>>() {
            @Override
            public void onResponse(Call<ArrayList<Wisata>> call, Response<ArrayList<Wisata>> response) {
                travels = response.body();
                for (int i =0 ; i<travels.size();i++){
                    createMarker(travels.get(i).getPosisi_lat(), travels.get(i).getPosisi_lng(), travels.get(i).getNama());
                    akses.add(travels.get(i).getAkses());
                }
                waterFallGetApiPhraseOne(response.body(), idKamar, idMenu);
                Log.d("success",response.body().toString());
            }

            @Override
            public void onFailure(Call<ArrayList<Wisata>> call, Throwable t) {
                Log.d("error",t.getMessage());

            }
        });
    }

    private void waterFallGetApiPhraseOne(final ArrayList<Wisata> dataWisata, String idKamar, final String idMenu){
        API.service_post.package_recomendation_kamar(idKamar, "kamar").enqueue(new Callback<ArrayList<Kamar>>() {
            @Override
            public void onResponse(Call<ArrayList<Kamar>> call, Response<ArrayList<Kamar>> response) {
                hotels = response.body();
                for (int i = 0; i < hotels.size(); i++) {
                    createMarker(hotels.get(i).getPenginapan().getPosisi_lat(), hotels.get(i).getPenginapan().getPosisi_lng(), hotels.get(i).getPenginapan().getNama());
                    akses.add(hotels.get(i).getPenginapan().getAkses());
                }
                waterFallGetApiPhraseTwo(dataWisata, response.body(), idMenu);
                Log.d("success", response.body().toString());
            }
            @Override
            public void onFailure(Call<ArrayList<Kamar>> call, Throwable t) {

            }
        });
    }

    private void waterFallGetApiPhraseTwo(final ArrayList<Wisata> dataWisata, final ArrayList<Kamar> dataKamar, String idMenu){
        API.service_post.package_recomendation_menu(idMenu, "menu").enqueue(new Callback<ArrayList<Menu>>() {
            @Override
            public void onResponse(Call<ArrayList<Menu>> call, Response<ArrayList<Menu>> response) {
                //create marker menu
                menus = response.body();
                for (int i = 0; i < menus.size(); i++) {
                    createMarker(menus.get(i).getKuliner().getPosisi_lat(), menus.get(i).getKuliner().getPosisi_lng(), menus.get(i).getKuliner().getNama());
                    akses.add(menus.get(i).getKuliner().getAkses());
                }

                //draw marker all
                for(int i = 0 ; i< dataWisata.size();i++){
                    dataMarker.add(new LatLng(dataWisata.get(i).getPosisi_lat(), dataWisata.get(i).getPosisi_lng()));
                }

                for(int i = 0 ; i< dataKamar.size();i++){
                    dataMarker.add(new LatLng(dataKamar.get(i).getPenginapan().getPosisi_lat(), dataKamar.get(i).getPenginapan().getPosisi_lng()));
                }

                for(int i = 0 ; i< response.body().size();i++){
                    dataMarker.add(new LatLng(response.body().get(i).getKuliner().getPosisi_lat(), response.body().get(i).getKuliner().getPosisi_lng()));
                }
                Log.d("success", String.valueOf(dataMarker));

                //hitung jarak
                for(int i=0; i<dataMarker.size(); i++){
                    daftarJarak.add(distance(asal.latitude, asal.longitude, dataMarker.get(i).latitude, dataMarker.get(i).longitude));
                    if(i > 0 && i <= dataMarker.size()-1)
                        waypoint.add(dataMarker.get(i));
                    else
                        destination = dataMarker.get(i);
                }
                Collections.sort(daftarJarak);
                Log.d("success", String.valueOf(daftarJarak));

                //ambil total jarak
                total_km = Math.ceil(daftarJarak.get(daftarJarak.size()-1));
                Log.d("totalKm", String.valueOf(Math.ceil(total_km)));

                //draw route
                GoogleDirectionConfiguration.getInstance().setLogEnabled(true);
                GoogleDirection.withServerKey("AIzaSyBl2cV8z5qJeyTKLPG8JlXfk8rfnzPJ2QI")
                        .from(asal)
                        .and(waypoint)
                        .to(destination)
                        .transportMode(TransportMode.DRIVING)
                        .execute(MapActivity.this);

                //itung harga berdasarkan akses
                for (int i = 0; i < akses.size() ; i++) {
                    if(akses.get(i).equals("sulit")){
                        biaya_tambahan = 20000.0;
                    }else if(akses.get(i).equals("sedang")){
                        biaya_tambahan = 10000.0;
                    }
                    //biaya_tambahan = + biaya_tambahan[i+1];
                }
                Log.d("success", String.valueOf(akses));
                Log.d("success", String.valueOf(biaya_tambahan));

                //kondisi budget

                final MyChoice myChoice = getIntent().getExtras().getParcelable("myChoice");
                //TextView tvMyBudget = getIntent().getExtras().getParcelable("budget");
                //ProgressBar pbBudget = getIntent().getExtras().getParcelable("progressBar");

                jasa = 50000d;

                API.service_post.get_r_jarak("transport", jasa, biaya_tambahan, total_km,
                        myChoice.getTicketMotor(), myChoice.getTicketCar(), myChoice.getTicketBus()).enqueue(new Callback<Double>() {
                    @Override
                    public void onResponse(Call<Double> call, Response<Double> response) {
                        rekomendasi = response.body();
                        myChoice.setBudget(myChoice.getBudget()-rekomendasi);

                        if(rekomendasi>myChoice.getBudget())
                            Toast.makeText(MapActivity.this, "Budget anda tidak cukup", Toast.LENGTH_SHORT).show();
                        else{
                            txtNominal.setText("Rp "+rekomendasi.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<Double> call, Throwable t) {

                    }
                });
                //progress bar
                //pbBudget.setProgress(myChoice.getBudget().intValue());
                /*tvMyBudget.setText("Rp "+ myChoice.getBudget());
                tvTotalBudget.setText("Rp "+myChoice.getBudget());*/
                //Log.d("budget", String.valueOf(myChoice.getBudget()));
            }

            @Override
            public void onFailure(Call<ArrayList<Menu>> call, Throwable t) {

            }
        });
    }

    protected Marker createMarker(double latitude, double longitude, String title){
        return mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title));
    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        //Snackbar.make(btnRequestDirection, "Success with status : " + direction.getStatus(), Snackbar.LENGTH_SHORT).show();
        Toast.makeText(this, "Success with status : "+direction.getStatus(), Toast.LENGTH_SHORT).show();
        if (direction.isOK()) {
            Route route = direction.getRouteList().get(0);
            int legCount = route.getLegList().size();
            for (int index = 0; index < legCount; index++) {
                Leg leg = route.getLegList().get(index);
                //mMap.addMarker(new MarkerOptions().position(leg.getStartLocation().getCoordination()));
                /*if (index == legCount) {
                    mMap.addMarker(new MarkerOptions().position(leg.getEndLocation().getCoordination()));
                }*/
                List<Step> stepList = leg.getStepList();
                ArrayList<PolylineOptions> polylineOptionList = DirectionConverter.createTransitPolyline(
                        this, stepList, 5, Color.BLUE, 3, Color.BLUE);
                for (PolylineOptions polylineOption : polylineOptionList) {
                    mMap.addPolyline(polylineOption);
                }
            }
            setCameraWithCoordinationBounds(route);
        }
    }

    @Override
    public void onDirectionFailure(Throwable t) {
        Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void setCameraWithCoordinationBounds(Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }

    public static double distance(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        //double dist = earthRadius * c *1000;
        //dist = Math.pow(dist,2);
        double dist = earthRadius * c;
        return dist;
    }
}

