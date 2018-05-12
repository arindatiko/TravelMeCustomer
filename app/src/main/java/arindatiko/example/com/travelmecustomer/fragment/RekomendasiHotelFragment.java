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
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
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

    private RecyclerView rcHotel;
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

        RecyclerView.LayoutManager travelLayout = new LinearLayoutManager(getContext());
        rcHotel.setLayoutManager(travelLayout);
        rcHotel.setItemAnimator(new DefaultItemAnimator());
        rcHotel.setFocusable(false);

        loadHotelData();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RekomendasiActivity.currentFragment = RekomendasiActivity.TRAVEL;
    }

    /*public void loadHotelData() {
        final MyChoice myChoice = ((RekomendasiActivity) getActivity()).getMyChoice();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait!");
        progressDialog.show();

        API.service_post.get_r_kamar(
                "kamar",
                myChoice.getJumKamar(),
                myChoice.getJumDay(),
                myChoice.getBudget()
        ).enqueue(new Callback<ArrayList<Kamar>>() {
            @Override
            public void onResponse(Call<ArrayList<Kamar>> call, Response<ArrayList<Kamar>> response) {
                hotels = response.body();

                rcHotel.setAdapter(new RekomendasiHotelAdapter(getContext(), hotels, myChoice));
                rcHotel.getAdapter().notifyDataSetChanged();

                progressDialog.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<ArrayList<Kamar>> call, Throwable t) {
                Log.d(REHOTEL_FRAG_TAG, t.getMessage());
            }
        });
    }*/

    public void loadHotelData() {
        final MyChoice myChoice = ((RekomendasiActivity) getActivity()).getMyChoice();
        final TextView tvMyBudget = ((RekomendasiActivity) getActivity()).getTvMyBudget();
        final ProgressBar pbBudget = ((RekomendasiActivity) getActivity()).getPbBudget();

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait!");
        progressDialog.show();

        API.service_post.get_r_kamar(
                "kamar",
                myChoice.getJumKamar(),
                myChoice.getJumDay(),
                myChoice.getBudget()
        ).enqueue(new Callback<ArrayList<Kamar>>() {
            @Override
            public void onResponse(Call<ArrayList<Kamar>> call, Response<ArrayList<Kamar>> response) {
                hotels = response.body();

                rcHotel.setAdapter(new RekomendasiHotelAdapter(getContext(), hotels, myChoice, tvMyBudget, pbBudget));
                rcHotel.getAdapter().notifyDataSetChanged();

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
