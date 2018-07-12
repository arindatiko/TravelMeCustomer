package arindatiko.example.com.travelmecustomer;

import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import arindatiko.example.com.travelmecustomer.model.Wisata;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailWisataActivity extends AppCompatActivity {

    public static final String WISATA_ID = "wisata_id";

    private int wisataId;
    private Wisata wisata;

    private ImageView imgDetail;
    private TextView tvTitle, tvAddress, tvDescription, tvTimeOpen, tvTimeClose, tvTicketAdult,
            tvTicketChild, tvTicketMotor, tvTicketCar, tvTicketBus, tvFacilities;
    private GoogleMap map;
    private SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_wisata);

        wisataId = getIntent().getIntExtra(WISATA_ID, 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        imgDetail = (ImageView) findViewById(R.id.img_detail);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        tvDescription = (TextView) findViewById(R.id.tv_description);
        tvTimeOpen = (TextView) findViewById(R.id.tv_time_open);
        tvTimeClose = (TextView) findViewById(R.id.tv_time_close);
        tvTicketAdult = (TextView) findViewById(R.id.tv_price_adult);
        tvTicketChild = (TextView) findViewById(R.id.tv_price_child);
        tvTicketMotor = (TextView) findViewById(R.id.tv_price_motor);
        tvTicketCar = (TextView) findViewById(R.id.tv_price_car);
        tvTicketBus = (TextView) findViewById(R.id.tv_price_bus);
        tvFacilities = (TextView) findViewById(R.id.tv_facilities);

        loadWisataData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    public void loadWisataData() {
        API.service_post.get_detail_wisata(wisataId).enqueue(new Callback<Wisata>() {
            @Override
            public void onResponse(Call<Wisata> call, Response<Wisata> response) {
                wisata = response.body();
                setWisataView();
            }

            @Override
            public void onFailure(Call<Wisata> call, Throwable t) {
                Log.d("DetailWisataActivity", t.getMessage());
            }
        });
    }

    public void setWisataView() {
        Glide.with(this)
                .load(wisata.getFoto())
                .into(imgDetail);

        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(wisata.getPosisi_lat(), wisata.getPosisi_lng(), 1);

            if (addresses.size() > 0) {
                Address fetchedAddress = addresses.get(0);
                tvAddress.setText(fetchedAddress.getAddressLine(0));
            } else {
                tvAddress.setText("-");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("DetailWisataActivity", e.getMessage());
            tvAddress.setText("-");
        }

        tvTitle.setText(wisata.getNama());
        tvDescription.setText(wisata.getDeskripsi());
        tvTimeOpen.setText(wisata.getJam_buka() +" wib");
        tvTimeClose.setText(wisata.getJam_tutup() +" wib");
        tvTicketAdult.setText(String.valueOf(wisata.getTiket_masuk_dewasa()));
        tvTicketChild.setText(String.valueOf(wisata.getTiket_masuk_anak()));
        tvTicketMotor.setText("Rp "+ wisata.getBiaya_parkir_motor());
        tvTicketCar.setText("Rp "+ wisata.getBiaya_parkir_mobil());
        tvTicketBus.setText("Rp "+ wisata.getBiaya_parkir_bus());
        tvFacilities.setText(wisata.getFasilitas());

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;

                LatLng latLng = new LatLng(wisata.getPosisi_lat(),  wisata.getPosisi_lng());
                map.addMarker(new MarkerOptions().position(latLng).title(wisata.getNama()));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
                map.getUiSettings().setAllGesturesEnabled(true);
            }
        });
    }
}
