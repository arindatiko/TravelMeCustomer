package arindatiko.example.com.travelmecustomer.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import arindatiko.example.com.travelmecustomer.API;
import arindatiko.example.com.travelmecustomer.R;
import arindatiko.example.com.travelmecustomer.RekomendasiActivity;
import arindatiko.example.com.travelmecustomer.adapter.RekomendasiTravelAdapter;
import arindatiko.example.com.travelmecustomer.model.MyChoice;
import arindatiko.example.com.travelmecustomer.model.Wisata;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RekomendasiTravelFragment extends Fragment {
    public static final String RETRAVEL_FRAG_TAG = "RekomendasiTravelFragment";

    private MyChoice myChoice;
    private TextView tvMyBudget;
    private ProgressBar pbBudget;
    private RecyclerView rcTravel;
    private Spinner spinnerSort;

    private List<Wisata> travels = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private RekomendasiTravelAdapter travelAdapter;


    public RekomendasiTravelFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rekomendasi_travel, container, false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("myTravel", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

        rcTravel = (RecyclerView) view.findViewById(R.id.rc_travel);
        spinnerSort = (Spinner) view.findViewById(R.id.spinner_sort);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, new String[]{"Pilih","Nama", "Harga"} );

        final RecyclerView.LayoutManager travelLayout = new LinearLayoutManager(getContext());
        rcTravel.setLayoutManager(travelLayout);
        rcTravel.setItemAnimator(new DefaultItemAnimator());
        rcTravel.setFocusable(false);

        //loadWisataData();

        spinnerSort.setAdapter(adapter);
        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemIdAtPosition(position) == 1){
                    loadWisataData(1);
                }else if(parent.getItemIdAtPosition(position) == 2){
                    loadWisataData(2);
                }else{
                    loadWisataData(0);
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
        getActivity().finish();
    }

    public void loadWisataData(final int sort) {
        myChoice = ((RekomendasiActivity) getActivity()).getMyChoice();
        tvMyBudget = ((RekomendasiActivity) getActivity()).getTvMyBudget();
        pbBudget = ((RekomendasiActivity) getActivity()).getPbBudget();

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait!");
        progressDialog.show();

        travelAdapter = new RekomendasiTravelAdapter(getContext(), travels, myChoice, tvMyBudget, pbBudget);

        API.service_post.get_r_wisata(
                "wisata",
                myChoice.getCategoryWisata().toString(),
                myChoice.getTicketChild(),
                myChoice.getTicketAdult(),
                myChoice.getTicketMotor(),
                myChoice.getTicketCar(),
                myChoice.getTicketBus(),
                myChoice.getBudget()
        ).enqueue(new Callback<ArrayList<Wisata>>() {
            @Override
            public void onResponse(Call<ArrayList<Wisata>> call, Response<ArrayList<Wisata>> response) {
                //Log.d("Travel", String.valueOf(response.body().toString()));
                travels = response.body();

                if (sort == 1) {
                    Collections.sort(travels, new Comparator<Wisata>() {
                        @Override
                        public int compare(Wisata o1, Wisata o2) {
                            return o1.getNama().compareTo(o2.getNama());
                        }
                    });

                    travelAdapter.replaceData(travels);
                    rcTravel.setAdapter(travelAdapter);
                    rcTravel.getAdapter().notifyDataSetChanged();
                } else if(sort == 2){
                    travelAdapter.replaceData(travels);
                    travelAdapter.updateWisataList(travels);
                    rcTravel.setAdapter(travelAdapter);
                    rcTravel.getAdapter().notifyDataSetChanged();
                }else{
                    travelAdapter.replaceData(travels);
                    rcTravel.setAdapter(travelAdapter);
                    rcTravel.getAdapter().notifyDataSetChanged();
                }

                progressDialog.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<ArrayList<Wisata>> call, Throwable t) {
                Log.d(RETRAVEL_FRAG_TAG, t.getMessage());
            }
        });
    }
}
