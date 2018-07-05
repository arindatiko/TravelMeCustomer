package arindatiko.example.com.travelmecustomer;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import arindatiko.example.com.travelmecustomer.model.Kamar;
import arindatiko.example.com.travelmecustomer.model.Menu;
import arindatiko.example.com.travelmecustomer.model.MyChoice;
import arindatiko.example.com.travelmecustomer.model.Pesanan;
import arindatiko.example.com.travelmecustomer.model.Tujuan;
import arindatiko.example.com.travelmecustomer.model.User;
import arindatiko.example.com.travelmecustomer.model.Wisata;
import arindatiko.example.com.travelmecustomer.util.GPSTracker;
import arindatiko.example.com.travelmecustomer.util.SessionManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.media.CamcorderProfile.get;

public class MapActivity2 extends FragmentActivity implements OnMapReadyCallback, DirectionCallback {

    private ArrayList<LatLng> dataMarker = new ArrayList<>();
    private ArrayList<Wisata> wisata = new ArrayList<>();
    private ArrayList<Kamar> kamar = new ArrayList<>();
    private ArrayList<Menu> menu = new ArrayList<>();
    //private ArrayList<LatLng> dataMarker = new ArrayList<>();
    private List<String> akses = new ArrayList<>();
    private List<Double> daftarJarak = new ArrayList<>();
    private List<LatLng> waypoint = new ArrayList<>();
    private ArrayList<Tujuan> tujuan = new ArrayList<>();
    private ArrayList<Tujuan> tujuan1 = new ArrayList<>();
//    private Tujuan tujuan;
    private ArrayList<String> upload = new ArrayList<>();
    //private ArrayList<ArrayList<Tujuan>> coba = new ArrayList<ArrayList<Tujuan>>();

    private GoogleMap mMap;
    private GPSTracker gps;

    private User user = new User();
    private Pesanan pesanan = new Pesanan();

    private LatLng asal, destination;
    private int id_user;
    private String id_pesanan;
    private double lat, lng;
    private Double total_km, jasa, biaya_tambahan, sisa_awal, biaya_transport, sisa_akhir, total_biaya;

    SharedPreferences sharedPreferences = null;
    SharedPreferences.Editor editor;
    SessionManager sessionManager;
    ProgressDialog progressDialog;

    DatabaseReference pesanan_db;

    @BindView(R.id.btnOrder)
    Button btnOrder;
    @BindView(R.id.txtNominal)
    TextView txtNominal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        sharedPreferences = getApplicationContext().getSharedPreferences("myTravel", Context.MODE_PRIVATE);;
        editor = sharedPreferences.edit();
        sessionManager = new SessionManager(this);
        id_user = Integer.parseInt(sessionManager.getId());

        API.service_post.get_user(id_user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                user = response.body();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        gps = new GPSTracker(MapActivity2.this);
        if(gps.canGetLocation()){
            lat = gps.getLatitude();
            lng = gps.getLongitude();
            asal = new LatLng(lat, lng);
        }
        mMap.setMyLocationEnabled(true);

        Log.d("id", id_wisata);
        Log.d("id", id_kamar);
        Log.d("id", id_menu);

        waterFallGetApi(id_wisata, id_kamar, id_menu);
    }

    private void waterFallGetApi(final String idWisata, final String idKamar, final String idMenu){
        API.service_post.add_objek(id_user,idWisata, "wisata").enqueue(new Callback<ArrayList<Tujuan>>() {
            @Override
            public void onResponse(Call<ArrayList<Tujuan>> call, Response<ArrayList<Tujuan>> response) {
                tujuan.addAll(response.body());

                for (int i = 0; i < tujuan.size(); i++){
                    if(tujuan.get(i).getJenis_layanan().equals("wisata")) {
                        createMarker(tujuan.get(i).getWisata().get(0).getPosisi_lat(), tujuan.get(i).getWisata().get(0).getPosisi_lng(),
                                tujuan.get(i).getWisata().get(0).getNama());
                        akses.add(tujuan.get(i).getWisata().get(0).getAkses());
                        //dataMarker.add(new LatLng(tujuan.get(i).getWisata().get(0).getPosisi_lat(), tujuan.get(i).getWisata().get(0).getPosisi_lng()));
                    }
                }

                waterFallGetApiPhraseOne(response.body(), idKamar, idMenu);
                Log.d("wisata",response.body().toString());
                Log.d("idWisata",idWisata);
            }

            @Override
            public void onFailure(Call<ArrayList<Tujuan>> call, Throwable t) {
                Log.d("error",t.getMessage());
            }
        });
    }

    private void waterFallGetApiPhraseOne(final ArrayList<Tujuan> dataWisata, final String idKamar, final String idMenu){
        API.service_post.add_objek(id_user, idKamar, "kamar").enqueue(new Callback<ArrayList<Tujuan>>() {
            @Override
            public void onResponse(Call<ArrayList<Tujuan>> call, Response<ArrayList<Tujuan>> response) {
                tujuan.addAll(response.body());

                for (int i = 0; i < tujuan.size(); i++){
                    if(tujuan.get(i).getJenis_layanan().equals("kamar")) {
                        createMarker(tujuan.get(i).getKamar().get(0).getPenginapan().getPosisi_lat(),tujuan.get(i).getKamar().get(0).getPenginapan().getPosisi_lng(),
                                tujuan.get(i).getKamar().get(0).getPenginapan().getNama());
                        akses.add(tujuan.get(i).getKamar().get(0).getPenginapan().getAkses());
                    }
                }

                waterFallGetApiPhraseTwo(dataWisata, response.body(), idMenu);
            }
            @Override
            public void onFailure(Call<ArrayList<Tujuan>> call, Throwable t) {
                Log.d("error",t.getMessage());
            }
        });
    }

    private void waterFallGetApiPhraseTwo(final ArrayList<Tujuan> dataWisata, final ArrayList<Tujuan> dataKamar, final String idMenu){
        Log.d("id", idMenu);
        API.service_post.add_objek(id_user, idMenu, "menu").enqueue(new Callback<ArrayList<Tujuan>>() {
            @Override
            public void onResponse(Call<ArrayList<Tujuan>> call, Response<ArrayList<Tujuan>> response) {
                tujuan.addAll(response.body());

                for (int i = 0; i < tujuan.size(); i++) {
                    if(tujuan.get(i).getJenis_layanan().equals("menu")) {
                        createMarker(tujuan.get(i).getMenu().get(0).getKuliner().getPosisi_lat(), tujuan.get(i).getMenu().get(0).getKuliner().getPosisi_lng(),
                                tujuan.get(i).getMenu().get(0).getKuliner().getNama());
                        akses.add(tujuan.get(i).getMenu().get(0).getKuliner().getAkses());
                    }
                }

                Log.d("menu",response.body().toString());
                Log.d("idMenu",idMenu);

                //draw marker all
                for (int i = 0; i < tujuan.size(); i++) {
                    if (tujuan.get(i).getJenis_layanan().equals("wisata")) {
                        dataMarker.add(new LatLng(tujuan.get(i).getWisata().get(0).getPosisi_lat(), tujuan.get(i).getWisata().get(0).getPosisi_lng()));
                    } else if (tujuan.get(i).getJenis_layanan().equals("kamar")) {
                        dataMarker.add(new LatLng(tujuan.get(i).getKamar().get(0).getPenginapan().getPosisi_lat(), tujuan.get(i).getKamar().get(0).getPenginapan().getPosisi_lng()));
                    } else if (tujuan.get(i).getJenis_layanan().equals("menu")) {
                        dataMarker.add(new LatLng(tujuan.get(i).getMenu().get(0).getKuliner().getPosisi_lat(), tujuan.get(i).getMenu().get(0).getKuliner().getPosisi_lng()));
                    }
                }

                //hitung jarak
                for(int i=0; i<dataMarker.size(); i++){
                    daftarJarak.add(distance(asal.latitude, asal.longitude, dataMarker.get(i).latitude, dataMarker.get(i).longitude));
                    if(i > 0 && i <= dataMarker.size()-1)
                        waypoint.add(dataMarker.get(i));
                    else
                        destination = dataMarker.get(i);
                }
                Collections.sort(daftarJarak);

                //ambil total jarak
                total_km = Math.ceil(daftarJarak.get(daftarJarak.size()-1));

                //draw route
                GoogleDirectionConfiguration.getInstance().setLogEnabled(true);
                GoogleDirection.withServerKey("AIzaSyBl2cV8z5qJeyTKLPG8JlXfk8rfnzPJ2QI")
                        .from(asal)
                        .and(waypoint)
                        .to(destination)
                        .transportMode(TransportMode.DRIVING)
                        .execute(MapActivity2.this);

                //itung harga berdasarkan akses
                for (int i = 0; i < akses.size() ; i++) {
                    if(akses.get(i).equals("sulit")){
                        biaya_tambahan = 20000.0;
                    }else if(akses.get(i).equals("sedang")){
                        biaya_tambahan = 10000.0;
                    }
                }

                //kondisi budget
                final MyChoice myChoice = getIntent().getExtras().getParcelable("myChoice");

                sisa_awal = myChoice.getBudget();

                jasa = 50000d;

                API.service_post.get_r_jarak("transport", jasa, biaya_tambahan, total_km,
                        myChoice.getTicketMotor(), myChoice.getTicketCar(), myChoice.getTicketBus()).enqueue(new Callback<Double>() {
                    @Override
                    public void onResponse(Call<Double> call, Response<Double> response) {
                        biaya_transport = response.body();

                        if(biaya_transport>myChoice.getBudget()) {
                            Toast.makeText(MapActivity2.this, "Budget anda tidak cukup", Toast.LENGTH_SHORT).show();
                        }else{
                            txtNominal.setText("Rp "+biaya_transport.toString());
                            myChoice.setBudget(sisa_awal - biaya_transport); // sisa
                            sisa_akhir = myChoice.getBudget();
                            Log.d("sisa akhir", sisa_akhir.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<Double> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onFailure(Call<ArrayList<Tujuan>> call, Throwable t) {

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

    @OnClick(R.id.btnOrder)
    public void toOrder (View view){
        String total = sharedPreferences.getString("totalbudget","");
        total_biaya = Double.valueOf(total) - sisa_akhir;

        progressDialog = new ProgressDialog(this);

        API.service_post.send_pesanan(id_user,lat,lng,total_biaya).enqueue(new Callback<Pesanan>() {
            @Override
            public void onResponse(Call<Pesanan> call, Response<Pesanan> response) {
                if (response.isSuccessful()) {
                    pesanan = response.body();
                    id_pesanan = String.valueOf(pesanan.getId_pesanan());

                    pesanan_db = FirebaseDatabase.getInstance().getReference("pesanan");
                    pesanan_db.child(id_pesanan).setValue(pesanan);
                    Log.d("id", pesanan_db.getKey());

                    pesanan_db.child(id_pesanan).child("tujuan").setValue(tujuan);
                    pesanan_db.child(id_pesanan).child("user").setValue(user);
                    //pesanan_db.child(String.valueOf(id_pesanan)).child("tujuan").setValue();

                    //Toast.makeText(MapActivity2.this, pesanan.getUser().getNo_telp(), Toast.LENGTH_SHORT).show();

                    progressDialog.setMessage("Please Wait!");
                    progressDialog.show();
                } else {
                    Toast.makeText(MapActivity2.this, "EROORRRRRRR", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Pesanan> call, Throwable t) {
                Toast.makeText(MapActivity2.this, "EROORRRR22222", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

