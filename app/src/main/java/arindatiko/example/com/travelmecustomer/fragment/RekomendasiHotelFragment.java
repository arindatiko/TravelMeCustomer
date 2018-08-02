package arindatiko.example.com.travelmecustomer.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import arindatiko.example.com.travelmecustomer.API;
import arindatiko.example.com.travelmecustomer.R;
import arindatiko.example.com.travelmecustomer.RekomendasiActivity;
import arindatiko.example.com.travelmecustomer.adapter.RekomendasiHotelAdapter;
import arindatiko.example.com.travelmecustomer.model.Kamar;
import arindatiko.example.com.travelmecustomer.model.MyChoice;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RekomendasiHotelFragment extends Fragment {

    public static final String REHOTEL_FRAG_TAG = "RekomendasiHotelFragment";

    private MyChoice myChoice;
    private TextView tvMyBudget;
    private ProgressBar pbBudget;
    private RecyclerView rcHotel;
    private Spinner spinnerSort;

    private ArrayAdapter<String> adapter;
    private RekomendasiHotelAdapter hotelAdapter;
    private List<Kamar> hotels = new ArrayList<>();

    public RekomendasiHotelFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rekomendasi_hotel, container, false);

        rcHotel = (RecyclerView) view.findViewById(R.id.rc_hotel);
        spinnerSort = (Spinner) view.findViewById(R.id.spinner_sort);
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, new String[]{"Pilih", "Nama", "Harga"});

        RecyclerView.LayoutManager travelLayout = new LinearLayoutManager(getContext());
        rcHotel.setLayoutManager(travelLayout);
        rcHotel.setItemAnimator(new DefaultItemAnimator());
        rcHotel.setFocusable(false);

        //loadHotelData();

        spinnerSort.setAdapter(adapter);
        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemIdAtPosition(position) == 1){
                    loadHotelData(1);
                }else if(parent.getItemIdAtPosition(position) == 2){
                    loadHotelData(2);
                }else{
                    loadHotelData(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RekomendasiActivity.currentFragment = RekomendasiActivity.TRAVEL;
    }

    public void loadHotelData(final int sort) {
        myChoice = ((RekomendasiActivity) getActivity()).getMyChoice();
        tvMyBudget = ((RekomendasiActivity) getActivity()).getTvMyBudget();
        pbBudget = ((RekomendasiActivity) getActivity()).getPbBudget();

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait!");
        progressDialog.show();

        hotelAdapter = new RekomendasiHotelAdapter(getContext(), hotels, myChoice, tvMyBudget, pbBudget);

        API.service_post.get_r_kamar(
                "kamar",
                myChoice.getJumKamar(),
                myChoice.getJumDay(),
                myChoice.getBudget()
        ).enqueue(new Callback<ArrayList<Kamar>>() {
            @Override
            public void onResponse(Call<ArrayList<Kamar>> call, Response<ArrayList<Kamar>> response) {
                hotels = response.body();

                if(sort == 1){
                    Collections.sort(hotels, new Comparator<Kamar>() {
                        @Override
                        public int compare(Kamar o1, Kamar o2) {
                            return o1.getNama().compareTo(o2.getNama());
                        }
                    });

                    hotelAdapter.replaceData(hotels);
                    rcHotel.setAdapter(hotelAdapter);
                    rcHotel.getAdapter().notifyDataSetChanged();
                }else if(sort == 2){
                    hotelAdapter.replaceData(hotels);
                    hotelAdapter.updateWisataList(hotels);
                    rcHotel.setAdapter(hotelAdapter);
                    rcHotel.getAdapter().notifyDataSetChanged();
                }else{
                    hotelAdapter.replaceData(hotels);
                    rcHotel.setAdapter(hotelAdapter);
                    rcHotel.getAdapter().notifyDataSetChanged();
                }


                progressDialog.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<ArrayList<Kamar>> call, Throwable t) {
                Log.d(REHOTEL_FRAG_TAG, t.getMessage());
            }
        });
    }
}
