package arindatiko.example.com.travelmecustomer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DatabaseError;
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

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, DirectionCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener  {

    private ArrayList<LatLng> dataMarker = new ArrayList<>();
    private List<String> akses = new ArrayList<>();
    private List<Double> daftarJarak = new ArrayList<>();
    private List<LatLng> waypoint = new ArrayList<>();
    private ArrayList<Tujuan> tujuan = new ArrayList<>();

    private Pesanan pesanan;

    private GPSTracker gps;
    private LatLng asal, destination, pickUpLocation;
    private double lat, lng;
    private Double total_km, jasa, biaya_tambahan, rekomendasi;
    private int radius = 1;
    private boolean driverFound = false;
    private String driverFoundId;

    SharedPreferences sharedPreferences = null;
    SessionManager sessionManager;
    ProgressDialog progressDialog;

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;

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

        sharedPreferences = getApplicationContext().getSharedPreferences("myTravel", Context.MODE_PRIVATE);
        sessionManager = new SessionManager(this);

        /*dataMarker.clear();
        akses.clear();
        waypoint.clear();
        tujuan.clear();
        daftarJarak.clear();*/

        //myChoice = getIntent().getParcelableExtra(MYCHOICE);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RekomendasiActivity.currentFragment = RekomendasiActivity.RESTAURANT;
    }

    @OnClick(R.id.btnOrder)
    public void toOrder(View view){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        //userId = sessionManager.getId();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("customerRequest");

        GeoFire geoFire = new GeoFire(ref);
        geoFire.setLocation(sessionManager.getId(), new GeoLocation(lat, lng));
        //geoFire.setLocation(userId, new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()));

        //pickUpLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        createMarker(lat, lng, "Pick Up Here");
        //mMap.addMarker(new MarkerOptions().position(pickUpLocation).title("Pick Up Here"));

        btnOrder.setText("Mencari Driver....");

        getClosestDriver();
    }

    private void getClosestDriver(){
        DatabaseReference driversLocation = FirebaseDatabase.getInstance().getReference().child("driversAvailable");

        GeoFire geofire = new GeoFire(driversLocation);

        GeoQuery geoQuery = geofire.queryAtLocation(new GeoLocation(asal.latitude, asal.longitude), radius);
        geoQuery.removeAllListeners();

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if(!driverFound){
                    driverFound = true;
                    driverFoundId = key;
                }
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                if(!driverFound){
                    radius++;
                    getClosestDriver();
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("Activity", getClass().getSimpleName());

        String id_wisata = sharedPreferences.getString("id_wisata", "");
        String id_kamar = sharedPreferences.getString("id_kamar", "");
        String id_menu = sharedPreferences.getString("id_menu", "");

        Log.d("idMenu", id_menu);

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
        //sessionManager.setLoc(String.valueOf(asal));

        buildGoogleApiClient();

        Log.d("id", id_wisata);
        Log.d("id", id_kamar);
        Log.d("id", id_menu);

       //waterFallGetApi(id_wisata, id_kamar, id_menu);
    }

    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        /*if(getApplicationContext() != null){
            mLastLocation = location;
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            //mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

            //String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }*/
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        /*LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        //String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String userId = sessionManager.getId();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("driversAvailable");

        GeoFire geoFire = new GeoFire(ref);
        geoFire.removeLocation(userId);*/
    }

   /* private void waterFallGetApi(final String idWisata, final String idKamar, final String idMenu){
        API.service_post.add_objek(sessionManager.getId(),idWisata, "wisata").enqueue(new Callback<ArrayList<Tujuan>>() {
            @Override
            public void onResponse(Call<ArrayList<Tujuan>> call, Response<ArrayList<Tujuan>> response) {
                tujuan = response.body();

                for (int i = 0; i < tujuan.size(); i++){
                    if(tujuan.get(i).getJenis_layanan().equals("wisata")) {
                        createMarker(tujuan.get(i).getWisata().get(0).getPosisi_lat(), tujuan.get(i).getWisata().get(0).getPosisi_lng(),
                                tujuan.get(i).getWisata().get(0).getNama());
                        akses.add(tujuan.get(i).getWisata().get(0).getAkses());
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
        API.service_post.add_objek(sessionManager.getId(), idKamar, "kamar").enqueue(new Callback<ArrayList<Tujuan>>() {
            @Override
            public void onResponse(Call<ArrayList<Tujuan>> call, Response<ArrayList<Tujuan>> response) {
                tujuan = response.body();

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

            }
        });
    }

    private void waterFallGetApiPhraseTwo(final ArrayList<Tujuan> dataWisata, final ArrayList<Tujuan> dataKamar, final String idMenu){
        Log.d("id", idMenu);
        API.service_post.add_objek(sessionManager.getId(), idMenu, "menu").enqueue(new Callback<ArrayList<Tujuan>>() {
            @Override
            public void onResponse(Call<ArrayList<Tujuan>> call, Response<ArrayList<Tujuan>> response) {
                tujuan = response.body();

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
                    if(tujuan.get(i).getJenis_layanan().equals("wisata")){
                        dataMarker.add(new LatLng(dataWisata.get(i).getWisata().get(0).getPosisi_lat(), dataWisata.get(i).getWisata().get(0).getPosisi_lng()));
                    }else if(tujuan.get(i).getJenis_layanan().equals("kamar")){
                        dataMarker.add(new LatLng(dataKamar.get(i).getKamar().get(0).getPenginapan().getPosisi_lat(), dataKamar.get(i).getKamar().get(0).getPenginapan().getPosisi_lng()));
                    }else if(response.body().get(i).getJenis_layanan().equals("menu")){
                        dataMarker.add(new LatLng(response.body().get(i).getMenu().get(0).getKuliner().getPosisi_lat(), response.body().get(i).getMenu().get(0).getKuliner().getPosisi_lng()));
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
                GoogleDirection.withServerKey("xxx")
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
                }

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
                        //myChoice.setBudget(myChoice.getBudget() + rekomendasi);

                        if(rekomendasi>myChoice.getBudget())
                            Toast.makeText(MapActivity.this, "Budget anda tidak cukup", Toast.LENGTH_SHORT).show();
                        else{
                            txtNominal.setText("Rp "+rekomendasi.toString());
                            myChoice.setBudget(myChoice.getBudget() - rekomendasi); // sisa
                        }

                        //tampung total
//                        myChoice.setTotalBiaya(myChoice.getTotalBiaya()+rekomendasi);
//                        Double total = myChoice.getTotalBiaya();
//                                Toast.makeText(MapActivity.this, total.toString(), Toast.LENGTH_SHORT).show();
                        //myChoice.setBudget(myChoice.getBudget()-rekomendasi);
//                        Double sisa = myChoice.getBudget();
//                        Toast.makeText(MapActivity.this, sisa.toString(), Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onFailure(Call<Double> call, Throwable t) {

                    }
                });

                //progress bar
                //pbBudget.setProgress(myChoice.getBudget().intValue());
                //tvMyBudget.setText("Rp "+ myChoice.getBudget());
                //tvTotalBudget.setText("Rp "+myChoice.getBudget());
                //Log.d("budget", String.valueOf(myChoice.getBudget()));
            }

            @Override
            public void onFailure(Call<ArrayList<Tujuan>> call, Throwable t) {

            }
        });
    }*/

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

