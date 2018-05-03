package arindatiko.example.com.travelmecustomer.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import arindatiko.example.com.travelmecustomer.API;
import arindatiko.example.com.travelmecustomer.R;
import arindatiko.example.com.travelmecustomer.adapter.KulinerAdapter;
import arindatiko.example.com.travelmecustomer.adapter.PenginapanAdapter;
import arindatiko.example.com.travelmecustomer.adapter.WisataAdapter;
import arindatiko.example.com.travelmecustomer.model.Kuliner;
import arindatiko.example.com.travelmecustomer.model.Penginapan;
import arindatiko.example.com.travelmecustomer.model.Wisata;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class HomeFragment extends Fragment {

    public static final String HOME_FRAG_TAG = "HomeFragment";
    private EditText etSearch;
    private RecyclerView rcTravel, rcRestaurant, rcHotel;

    private List<Wisata> travels = new ArrayList<>();
    private List<Kuliner> restaurants = new ArrayList<>();
    private List<Penginapan> hotels = new ArrayList<>();
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        etSearch = (EditText) view.findViewById(R.id.et_search);
        rcTravel = (RecyclerView) view.findViewById(R.id.rc_travel);
        rcRestaurant = (RecyclerView) view.findViewById(R.id.rc_restaurant);
        rcHotel = (RecyclerView) view.findViewById(R.id.rc_hotel);

        @SuppressLint({"NewApi", "LocalSuppress"})
        RecyclerView.LayoutManager travelLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rcTravel.setLayoutManager(travelLayout);
        rcTravel.setItemAnimator(new DefaultItemAnimator());

        @SuppressLint({"NewApi", "LocalSuppress"})
        RecyclerView.LayoutManager restaurantLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rcRestaurant.setLayoutManager(restaurantLayout);
        rcRestaurant.setItemAnimator(new DefaultItemAnimator());

        @SuppressLint({"NewApi", "LocalSuppress"})
        RecyclerView.LayoutManager hotelLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rcHotel.setLayoutManager(hotelLayout);
        rcHotel.setItemAnimator(new DefaultItemAnimator());

        loadWisataData();
        loadKulinerData();
        loadPenginapanData();


        return view;
    }

    public void loadWisataData() {
        API.service_post.get_all_wisata(0).enqueue(new Callback<ArrayList<Wisata>>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(Call<ArrayList<Wisata>> call, Response<ArrayList<Wisata>> response) {
                travels = response.body();

                rcTravel.setAdapter(new WisataAdapter(getContext(), travels));
                rcTravel.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<Wisata>> call, Throwable t) {
                Log.d(HOME_FRAG_TAG, t.getMessage());
            }
        });
    }

    public void loadKulinerData() {
        API.service_post.get_all_kuliner(0).enqueue(new Callback<ArrayList<Kuliner>>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(Call<ArrayList<Kuliner>> call, Response<ArrayList<Kuliner>> response) {
                restaurants = response.body();

                rcRestaurant.setAdapter(new KulinerAdapter(getContext(), restaurants));
                rcRestaurant.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<Kuliner>> call, Throwable t) {
                Log.d(HOME_FRAG_TAG, t.getMessage());
            }
        });
    }

    public void loadPenginapanData() {
        API.service_post.get_all_penginapan(0).enqueue(new Callback<ArrayList<Penginapan>>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(Call<ArrayList<Penginapan>> call, Response<ArrayList<Penginapan>> response) {
                hotels = response.body();

                rcHotel.setAdapter(new PenginapanAdapter(getContext(), hotels));
                rcHotel.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<Penginapan>> call, Throwable t) {
                Log.d(HOME_FRAG_TAG, t.getMessage());
            }
        });
    }
}
