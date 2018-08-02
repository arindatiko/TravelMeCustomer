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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import arindatiko.example.com.travelmecustomer.API;
import arindatiko.example.com.travelmecustomer.R;
import arindatiko.example.com.travelmecustomer.RekomendasiActivity;
import arindatiko.example.com.travelmecustomer.adapter.RekomendasiRestoAdapter;
import arindatiko.example.com.travelmecustomer.adapter.RekomendasiTravelAdapter;
import arindatiko.example.com.travelmecustomer.model.Kuliner;
import arindatiko.example.com.travelmecustomer.model.Menu;
import arindatiko.example.com.travelmecustomer.model.MyChoice;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RekomendasiRestoFragment extends Fragment {
    public static final String RERESTO_FRAG_TAG = "RekomendasiRestoFragment";

    private MyChoice myChoice;
    private TextView tvMyBudget;
    private ProgressBar pbBudget;
    private Spinner spinnerSort;

    private RecyclerView rcRestaurant;
    private List<Kuliner> kuliners = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private RekomendasiRestoAdapter restoAdapter;

    public RekomendasiRestoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rekomendasi_resto, container, false);

        rcRestaurant = (RecyclerView) view.findViewById(R.id.rc_restaurant);
        spinnerSort = (Spinner) view.findViewById(R.id.spinner_sort);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, new String[]{"Pilih","Nama", "Harga"} );

        RecyclerView.LayoutManager travelLayout = new LinearLayoutManager(getContext());
        rcRestaurant.setLayoutManager(travelLayout);
        rcRestaurant.setItemAnimator(new DefaultItemAnimator());
        rcRestaurant.setFocusable(false);

        //loadMenuData();

        spinnerSort.setAdapter(adapter);
        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemIdAtPosition(position) == 1){
                    loadMenuData(1);
                }else if(parent.getItemIdAtPosition(position) == 2){
                    loadMenuData(2);
                }else{
                    loadMenuData(0);
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
        RekomendasiActivity.currentFragment = RekomendasiActivity.HOTEL;
    }

    public void loadMenuData(final int sort) {
        myChoice = ((RekomendasiActivity) getActivity()).getMyChoice();
        tvMyBudget = ((RekomendasiActivity) getActivity()).getTvMyBudget();
        pbBudget = ((RekomendasiActivity) getActivity()).getPbBudget();

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait!");
        progressDialog.show();

        restoAdapter = new RekomendasiRestoAdapter(getContext(), kuliners, myChoice, tvMyBudget, pbBudget);

        API.service_post.get_r_menu(
                "menu",
                myChoice.getJumPorsi(),
                myChoice.getBudget()
        ).enqueue(new Callback<ArrayList<Kuliner>>() {
            @Override
            public void onResponse(Call<ArrayList<Kuliner>> call, Response<ArrayList<Kuliner>> response) {
                kuliners = response.body();

                if(sort == 1){
                    Collections.sort(kuliners, new Comparator<Kuliner>() {
                        @Override
                        public int compare(Kuliner o1, Kuliner o2) {
                            return o1.getNama().compareTo(o2.getNama());
                        }
                    });
                    restoAdapter.replaceData(kuliners);
                    rcRestaurant.setAdapter(restoAdapter);
                    rcRestaurant.getAdapter().notifyDataSetChanged();
                }else if(sort == 2){
                    restoAdapter.replaceData(kuliners);
                    restoAdapter.updateWisataList(kuliners);
                    rcRestaurant.setAdapter(restoAdapter);
                    rcRestaurant.getAdapter().notifyDataSetChanged();
                }else {
                    restoAdapter.replaceData(kuliners);
                    rcRestaurant.setAdapter(restoAdapter);
                    rcRestaurant.getAdapter().notifyDataSetChanged();
                }
                progressDialog.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<ArrayList<Kuliner>> call, Throwable t) {
                Log.d(RERESTO_FRAG_TAG, t.getMessage());
            }
        });
    }
    /*public void loadMenuData() {
        final MyChoice myChoice = ((RekomendasiActivity) getActivity()).getMyChoice();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait!");
        progressDialog.show();

        API.service_post.get_r_menu(
                "menu",
                myChoice.getJumPorsi(),
                myChoice.getBudget()
        ).enqueue(new Callback<ArrayList<Menu>>() {
            @Override
            public void onResponse(Call<ArrayList<Menu>> call, Response<ArrayList<Menu>> response) {
                menus = response.body();

                rcRestaurant.setAdapter(new RekomendasiRestoAdapter(getContext(), menus, myChoice));
                rcRestaurant.getAdapter().notifyDataSetChanged();

                progressDialog.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<ArrayList<Menu>> call, Throwable t) {
                Log.d(RERESTO_FRAG_TAG, t.getMessage());
            }
        });
    }*/

}

