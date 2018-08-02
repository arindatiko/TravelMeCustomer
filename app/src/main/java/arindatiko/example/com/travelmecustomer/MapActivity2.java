package arindatiko.example.com.travelmecustomer;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import arindatiko.example.com.travelmecustomer.model.Drivers;
import arindatiko.example.com.travelmecustomer.model.MyChoice;
import arindatiko.example.com.travelmecustomer.model.Pesanan;
import arindatiko.example.com.travelmecustomer.model.Rekomendasi;
import arindatiko.example.com.travelmecustomer.model.Tujuan;
import arindatiko.example.com.travelmecustomer.model.User;
import arindatiko.example.com.travelmecustomer.model.Wisata;
import arindatiko.example.com.travelmecustomer.model.WisataTujuan;
import arindatiko.example.com.travelmecustomer.util.GPSTracker;
import arindatiko.example.com.travelmecustomer.util.SessionManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity2 extends FragmentActivity implements OnMapReadyCallback, DirectionCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private ArrayList<LatLng> dataMarker = new ArrayList<>();
    private List<String> akses = new ArrayList<>();
    private List<Double> daftarJarak = new ArrayList<>();
    private List<LatLng> waypoint = new ArrayList<>();
    private ArrayList<Tujuan> tujuan = new ArrayList<>();
    private ArrayList<Rekomendasi> rekomendasi;

    private GoogleMap mMap;
    private GPSTracker gps;

    private Pesanan pesanan;
    private Drivers driver;

    private LatLng asal, destination;
    private GoogleApiClient googleApiClient;
    private Location mLocation;
    private int id_user;
    private String id_pesanan, id_pesanan_customer, id_pesanan_driver, id, currentLoc;
    private double lat, lng;
    private Double total_km, jasa, biaya_tambahan, sisa_awal, biaya_transport, sisa_akhir, total_biaya, budget_awal;
    private boolean stop = false;
    private LocationManager mLocationManager;

    private Handler handler = new Handler();
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getDataDriver();
        }
    };

    SharedPreferences sharedPreferences = null;
    SharedPreferences.Editor editor;
    SessionManager sessionManager;
    ProgressDialog progressDialog;
    DatabaseReference pesanan_db;

    @BindView(R.id.btnOrder)
    Button btnOrder;
    @BindView(R.id.txtNominal)
    TextView txtNominal;
    @BindView(R.id.tv_Name)
    TextView tvName;
    @BindView(R.id.tv_NoTelp)
    TextView tvNoTelp;
    @BindView(R.id.tv_idpesanan)
    TextView tvId;
    @BindView(R.id.linear_button)
    RelativeLayout linear;
    @BindView(R.id.linear_btn)
    LinearLayout linearBtn;

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

        Intent intent = getIntent();
        budget_awal = intent.getDoubleExtra("totalbudget", 0.0);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
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

        getcurrentLoc();

        Log.d("id", id_wisata);
        Log.d("id", id_kamar);
        Log.d("id", id_menu);

        if(!id_wisata.equals("")) {
            if(!id_kamar.equals("") && !id_menu.equals("")) {
                drawRouteFull(id_wisata, id_kamar, id_menu);
            }else if(!id_menu.equals("") && id_kamar.equals("")){
                drawWithoutKamar(id_wisata, id_menu);
            }else{
                drawWisata(id_wisata);
            }
        }else if(!id_kamar.equals("")){
            if(!id_menu.equals("")){
                drawWithoutWisata(id_kamar, id_menu);
            }else{
                drawKamar(id_kamar);
            }
        }else{
            drawMenu(id_menu);
        }
    }

    private void getcurrentLoc() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager mlocmag = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location loc = mlocmag.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (loc == null) {
                loc = mlocmag.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                lat = loc.getLatitude();
                lng = loc.getLongitude();
                asal = new LatLng(lat, lng);
                if (loc != null) {
                    lat = loc.getLatitude();
                    lng = loc.getLongitude();
                    asal = new LatLng(lat, lng);
                    currentLoc = loc.getLatitude() + "," + loc.getLongitude();
                }
            } else {
                lat = loc.getLatitude();
                lng = loc.getLongitude();
                asal = new LatLng(lat, lng);
                currentLoc = loc.getLatitude() + "," + loc.getLongitude();
            }
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (mLastLocation != null) {
                lat = mLastLocation.getLatitude();
                lng = mLastLocation.getLongitude();
                asal = new LatLng(lat, lng);
                currentLoc = String.valueOf(mLastLocation.getLatitude())+","+String.valueOf(mLastLocation.getLongitude());
            }
        }
    }

    //full
    private void drawRouteFull(final String idWisata, final String idKamar, final String idMenu){
        API.service_post.add_objek(id_user,idWisata, "wisata").enqueue(new Callback<ArrayList<Tujuan>>() {
            @Override
            public void onResponse(Call<ArrayList<Tujuan>> call, Response<ArrayList<Tujuan>> response) {
                tujuan.addAll(response.body());

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
                        createMarker(tujuan.get(i).getKuliner().get(0).getPosisi_lat(), tujuan.get(i).getKuliner().get(0).getPosisi_lng(),
                               tujuan.get(i).getKuliner().get(0).getNama());
                        akses.add(tujuan.get(i).getKuliner().get(0).getAkses());
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
                        dataMarker.add(new LatLng(tujuan.get(i).getKuliner().get(0).getPosisi_lat(), tujuan.get(i).getKuliner().get(0).getPosisi_lng()));
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
                            Log.d("sisaakhir", sisa_akhir.toString());
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

    //kamar kosong
    private void drawWithoutKamar(final String idWisata, final String idMenu){
        API.service_post.add_objek(id_user,idWisata, "wisata").enqueue(new Callback<ArrayList<Tujuan>>() {
            @Override
            public void onResponse(Call<ArrayList<Tujuan>> call, Response<ArrayList<Tujuan>> response) {
                tujuan.addAll(response.body());

                for (int i = 0; i < tujuan.size(); i++){
                    if(tujuan.get(i).getJenis_layanan().equals("wisata")) {
                        createMarker(tujuan.get(i).getWisata().get(0).getPosisi_lat(), tujuan.get(i).getWisata().get(0).getPosisi_lng(),
                                tujuan.get(i).getWisata().get(0).getNama());
                        akses.add(tujuan.get(i).getWisata().get(0).getAkses());
                    }
                }

                waterfallMenu(response.body(), idMenu);
                Log.d("wisata",response.body().toString());
                Log.d("idWisata",idWisata);
            }

            @Override
            public void onFailure(Call<ArrayList<Tujuan>> call, Throwable t) {
                Log.d("error",t.getMessage());
            }
        });
    }
    private void waterfallMenu(final ArrayList<Tujuan> dataWisata, final String idMenu){
        Log.d("id", idMenu);
        API.service_post.add_objek(id_user, idMenu, "menu").enqueue(new Callback<ArrayList<Tujuan>>() {
            @Override
            public void onResponse(Call<ArrayList<Tujuan>> call, Response<ArrayList<Tujuan>> response) {
                tujuan.addAll(response.body());

                for (int i = 0; i < tujuan.size(); i++) {
                    if(tujuan.get(i).getJenis_layanan().equals("menu")) {
                        createMarker(tujuan.get(i).getKuliner().get(0).getPosisi_lat(), tujuan.get(i).getKuliner().get(0).getPosisi_lng(),
                                tujuan.get(i).getKuliner().get(0).getNama());
                        akses.add(tujuan.get(i).getKuliner().get(0).getAkses());
                    }
                }

                Log.d("menu",response.body().toString());
                Log.d("idMenu",idMenu);

                //draw marker all
                for (int i = 0; i < tujuan.size(); i++) {
                    if (tujuan.get(i).getJenis_layanan().equals("wisata")) {
                        dataMarker.add(new LatLng(tujuan.get(i).getWisata().get(0).getPosisi_lat(), tujuan.get(i).getWisata().get(0).getPosisi_lng()));
                    } else if (tujuan.get(i).getJenis_layanan().equals("menu")) {
                        dataMarker.add(new LatLng(tujuan.get(i).getKuliner().get(0).getPosisi_lat(), tujuan.get(i).getKuliner().get(0).getPosisi_lng()));
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

    //wisata saja
    private void drawWisata(final String idWisata){
        API.service_post.add_objek(id_user, idWisata, "wisata").enqueue(new Callback<ArrayList<Tujuan>>() {
            @Override
            public void onResponse(Call<ArrayList<Tujuan>> call, Response<ArrayList<Tujuan>> response) {
                tujuan.addAll(response.body());

                for (int i = 0; i < tujuan.size(); i++) {
                    if(tujuan.get(i).getJenis_layanan().equals("wisata")) {
                        createMarker(tujuan.get(i).getWisata().get(0).getPosisi_lat(), tujuan.get(i).getWisata().get(0).getPosisi_lng(),
                                tujuan.get(i).getWisata().get(0).getNama());
                        akses.add(tujuan.get(i).getWisata().get(0).getAkses());
                    }
                }

                //draw marker all
                for (int i = 0; i < tujuan.size(); i++) {
                    if (tujuan.get(i).getJenis_layanan().equals("wisata")) {
                        dataMarker.add(new LatLng(tujuan.get(i).getWisata().get(0).getPosisi_lat(), tujuan.get(i).getWisata().get(0).getPosisi_lng()));
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

    //wisata kosong
    private void drawWithoutWisata(final String idKamar, final String idMenu){
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

                waterfallKamar(response.body(), idMenu);
            }
            @Override
            public void onFailure(Call<ArrayList<Tujuan>> call, Throwable t) {
                Log.d("error",t.getMessage());
            }
        });
    }
    private void waterfallKamar(final ArrayList<Tujuan> dataKamar, final String idMenu){
        Log.d("id", idMenu);
        API.service_post.add_objek(id_user, idMenu, "menu").enqueue(new Callback<ArrayList<Tujuan>>() {
            @Override
            public void onResponse(Call<ArrayList<Tujuan>> call, Response<ArrayList<Tujuan>> response) {
                tujuan.addAll(response.body());

                for (int i = 0; i < tujuan.size(); i++) {
                    if(tujuan.get(i).getJenis_layanan().equals("menu")) {
                        createMarker(tujuan.get(i).getKuliner().get(0).getPosisi_lat(), tujuan.get(i).getKuliner().get(0).getPosisi_lng(),
                                tujuan.get(i).getKuliner().get(0).getNama());
                        akses.add(tujuan.get(i).getKuliner().get(0).getAkses());
                    }
                }

                Log.d("menu",response.body().toString());
                Log.d("idMenu",idMenu);

                //draw marker all
                for (int i = 0; i < tujuan.size(); i++) {
                    if (tujuan.get(i).getJenis_layanan().equals("kamar")) {
                        dataMarker.add(new LatLng(tujuan.get(i).getKamar().get(0).getPenginapan().getPosisi_lat(), tujuan.get(i).getKamar().get(0).getPenginapan().getPosisi_lng()));
                    } else if (tujuan.get(i).getJenis_layanan().equals("menu")) {
                        dataMarker.add(new LatLng(tujuan.get(i).getKuliner().get(0).getPosisi_lat(), tujuan.get(i).getKuliner().get(0).getPosisi_lng()));
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

    //kamar saja
    private void drawKamar(final String idKamar){
        API.service_post.add_objek(id_user, idKamar, "kamar").enqueue(new Callback<ArrayList<Tujuan>>() {
            @Override
            public void onResponse(Call<ArrayList<Tujuan>> call, Response<ArrayList<Tujuan>> response) {
                tujuan.addAll(response.body());

                for (int i = 0; i < tujuan.size(); i++) {
                    if(tujuan.get(i).getJenis_layanan().equals("kamar")) {
                        createMarker(tujuan.get(i).getKamar().get(0).getPenginapan().getPosisi_lat(),tujuan.get(i).getKamar().get(0).getPenginapan().getPosisi_lng(),
                                tujuan.get(i).getKamar().get(0).getPenginapan().getNama());
                        akses.add(tujuan.get(i).getKamar().get(0).getPenginapan().getAkses());
                    }
                }

                Log.d("menu",response.body().toString());

                //draw marker all
                for (int i = 0; i < tujuan.size(); i++) {
                    if (tujuan.get(i).getJenis_layanan().equals("kamar")) {
                        dataMarker.add(new LatLng(tujuan.get(i).getKamar().get(0).getPenginapan().getPosisi_lat(), tujuan.get(i).getKamar().get(0).getPenginapan().getPosisi_lng()));
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

    //menu saja
    private void drawMenu(final String idMenu){
        Log.d("id", idMenu);
        API.service_post.add_objek(id_user, idMenu, "menu").enqueue(new Callback<ArrayList<Tujuan>>() {
            @Override
            public void onResponse(Call<ArrayList<Tujuan>> call, Response<ArrayList<Tujuan>> response) {
                tujuan.addAll(response.body());

                for (int i = 0; i < tujuan.size(); i++) {
                    if(tujuan.get(i).getJenis_layanan().equals("menu")) {
                        createMarker(tujuan.get(i).getKuliner().get(0).getPosisi_lat(), tujuan.get(i).getKuliner().get(0).getPosisi_lng(),
                                tujuan.get(i).getKuliner().get(0).getNama());
                        akses.add(tujuan.get(i).getKuliner().get(0).getAkses());
                    }
                }

                Log.d("menu",response.body().toString());
                Log.d("idMenu",idMenu);

                //draw marker all
                for (int i = 0; i < tujuan.size(); i++) {
                    if (tujuan.get(i).getJenis_layanan().equals("menu")) {
                        dataMarker.add(new LatLng(tujuan.get(i).getKuliner().get(0).getPosisi_lat(), tujuan.get(i).getKuliner().get(0).getPosisi_lng()));
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

        //String total = sharedPreferences.getString("totalbudget","");
        total_biaya = budget_awal - sisa_akhir;

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait!");
        progressDialog.setCancelable(false);

        API.service_post.send_pesanan(id_user, lat, lng, total_biaya).enqueue(new Callback<Pesanan>() {
            @Override
            public void onResponse(Call<Pesanan> call, Response<Pesanan> response) {
                if (response.isSuccessful()) {
                    pesanan = response.body();
                    id_pesanan = String.valueOf(pesanan.getId_pesanan());

                    pesanan_db = FirebaseDatabase.getInstance().getReference("pesanan");
                    pesanan_db.child(id_pesanan).setValue(pesanan);
                    pesanan_db.child(id_pesanan).child("tujuan").setValue(tujuan);
                    pesanan_db.child(id_pesanan).child("total_budget").setValue(biaya_transport);
                    id_pesanan_customer = pesanan_db.child(id_pesanan).getKey();
                    Log.d("id", id_pesanan_customer);

                    API.service_post.get_user(sessionManager.getId()).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            User user = response.body();
                            pesanan_db.child(id_pesanan).child("user").setValue(user);
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Log.d("fail", t.getMessage());
                        }
                    });


                    API.service_post.update_pesanan(id_pesanan,sessionManager.getId()).enqueue(new Callback<ArrayList<Rekomendasi>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Rekomendasi>> call, Response<ArrayList<Rekomendasi>> response) {
                            rekomendasi = response.body();
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Rekomendasi>> call, Throwable t) {
                            Log.d("fail", t.getMessage());
                        }
                    });

                    updateDriver();
                    btnOrder.setText("Mencari Driver");
                    progressDialog.show();
                } else {
                    Toast.makeText(MapActivity2.this, "EROORRRRRRR", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Pesanan> call, Throwable t) {
                Log.d("error", t.getMessage());
            }
        });
        //sessionManager.setStatus(1);
    }

    private void getDataDriver() {
        pesanan_db = FirebaseDatabase.getInstance().getReference().child("pesanan");

        pesanan_db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                id_pesanan_driver = dataSnapshot.getKey();
                if(id_pesanan_driver.equals(id_pesanan_customer)){
                    pesanan = dataSnapshot.getValue(Pesanan.class);
                    Log.d("pesanan", String.valueOf(pesanan.getId_pesanan()));
                    id = String.valueOf(pesanan.getId_pesanan());
                    driver = dataSnapshot.child("driver").getValue(Drivers.class);


                    /*Intent intent = new Intent(MapActivity2Backup.this, MyTravelFragment.class);
                    intent.putExtra("pesanan", String.valueOf(pesanan));
                    intent.putExtra("driver", String.valueOf(driver));*/

                    try {
                        if(pesanan.getStatus() == 1){
                            String nama = driver.getNama_lengkap();
                            Log.d("nama", nama);

                            tvName.setText(driver.getNama_lengkap());
                            tvNoTelp.setText(driver.getNo_telp());
                            tvId.setText(id);
                            Log.d("textview", "Suceess");
                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(driver.getPosisi_lat(), driver.getPosisi_lng())));

                            editor.putString("name_driver", String.valueOf(driver.getNama_lengkap()));
                            editor.putString("notelp_driver", String.valueOf(driver.getNo_telp()));
                            editor.putString("id_pesanan", String.valueOf(pesanan.getId_pesanan()));
                            editor.commit();

                            progressDialog.dismiss();
                            linear.setVisibility(View.VISIBLE);
                            linearBtn.setVisibility(View.GONE);
                            
                            sessionManager.setStatus(1);
                            
                            stop = true;
                            Log.d("try", String.valueOf(pesanan.getStatus()));
                        }
                    } catch (Exception e) {
                        stop = false;
                        Log.d("catch", e.toString());
                    }

                    if(stop){
                        handler.removeCallbacks(runnable);
                    }else{
                        handler.postDelayed(runnable, 1000);
                    }
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateDriver(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getDataDriver();
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        //startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        /*if(mLocation == null){
            startLocationUpdates();
        }*/
        if (mLocation != null) {

            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
            currentLoc = String.valueOf(mLocation.getLatitude())+","+String.valueOf(mLocation.getLongitude());
            lat = mLocation.getLatitude();
            lng = mLocation.getLongitude();
        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

