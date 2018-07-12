package arindatiko.example.com.travelmecustomer.fragment;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import arindatiko.example.com.travelmecustomer.DetailKulinerActivity;
import arindatiko.example.com.travelmecustomer.R;
import arindatiko.example.com.travelmecustomer.model.Kuliner;

public class KulinerFragment extends Fragment {

    private TextView tvTitle, tvAddress, tvDescription, tvTimeOpen, tvTimeClose, tvPhone,
            tvTicketMotor, tvTicketCar, tvTicketBus, tvFacilities;
    private GoogleMap map;
    private SupportMapFragment mapFragment;
    private Kuliner kuliner;

    public KulinerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_kuliner, container, false);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvAddress = (TextView) view.findViewById(R.id.tv_address);
        tvDescription = (TextView) view.findViewById(R.id.tv_description);
        tvTimeOpen = (TextView) view.findViewById(R.id.tv_time_open);
        tvTimeClose = (TextView) view.findViewById(R.id.tv_time_close);
        tvTicketMotor = (TextView) view.findViewById(R.id.tv_price_motor);
        tvTicketCar = (TextView) view.findViewById(R.id.tv_price_car);
        tvTicketBus = (TextView) view.findViewById(R.id.tv_price_bus);
        tvFacilities = (TextView) view.findViewById(R.id.tv_facilities);
        tvPhone = (TextView) view.findViewById(R.id.tv_phone);

        kuliner = ((DetailKulinerActivity) getActivity()).getKuliner();

        Geocoder geocoder = new Geocoder(getContext(), Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(kuliner.getPosisi_lat(), kuliner.getPosisi_lng(), 1);

            if (addresses.size() > 0) {
                Address fetchedAddress = addresses.get(0);
                tvAddress.setText(fetchedAddress.getAddressLine(0));
            } else {
                tvAddress.setText("-");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("DetailKulinerActivity", e.getMessage());
            tvAddress.setText("-");
        }

        tvTitle.setText(kuliner.getNama());
        tvDescription.setText(kuliner.getDeskripsi());
        tvTimeOpen.setText(kuliner.getJam_buka() +" wib");
        tvTimeClose.setText(kuliner.getJam_tutup() +" wib");
        tvTicketMotor.setText("Rp "+ kuliner.getHarga_tiket_parkir_motor());
        tvTicketCar.setText("Rp "+ kuliner.getHarga_tiket_parkir_mobil());
        tvTicketBus.setText("Rp "+ kuliner.getHarga_tiket_parkir_bus());
        tvFacilities.setText(kuliner.getFasilitas());
        tvPhone.setText(kuliner.getNo_telp());

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;

                LatLng latLng = new LatLng(kuliner.getPosisi_lat(),  kuliner.getPosisi_lng());
                map.addMarker(new MarkerOptions().position(latLng).title(kuliner.getNama()));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
                map.getUiSettings().setAllGesturesEnabled(true);
            }
        });

        return view;
    }

}
