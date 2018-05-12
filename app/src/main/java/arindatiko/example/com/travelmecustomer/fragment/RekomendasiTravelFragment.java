package arindatiko.example.com.travelmecustomer.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import arindatiko.example.com.travelmecustomer.API;
import arindatiko.example.com.travelmecustomer.R;
import arindatiko.example.com.travelmecustomer.RekomendasiActivity;
import arindatiko.example.com.travelmecustomer.adapter.RekomendasiTravelAdapter;
import arindatiko.example.com.travelmecustomer.model.MyChoice;
import arindatiko.example.com.travelmecustomer.model.Rekomendasi;
import arindatiko.example.com.travelmecustomer.model.Wisata;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RekomendasiTravelFragment extends Fragment {
    public static final String RETRAVEL_FRAG_TAG = "RekomendasiTravelFragment";

    private RecyclerView rcTravel;
    private List<Wisata> travels = new ArrayList<>();

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

        RecyclerView.LayoutManager travelLayout = new LinearLayoutManager(getContext());
        rcTravel.setLayoutManager(travelLayout);
        rcTravel.setItemAnimator(new DefaultItemAnimator());
        rcTravel.setFocusable(false);

        loadWisataData();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().finish();
    }

    public void loadWisataData() {
        final MyChoice myChoice = ((RekomendasiActivity) getActivity()).getMyChoice();
        final TextView tvMyBudget = ((RekomendasiActivity) getActivity()).getTvMyBudget();
        final ProgressBar pbBudget = ((RekomendasiActivity) getActivity()).getPbBudget();

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait!");
        progressDialog.show();

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
                travels = response.body();
                Log.d("Travel", String.valueOf(response.body().toString()));

                rcTravel.setAdapter(new RekomendasiTravelAdapter(getContext(), travels, myChoice, tvMyBudget, pbBudget));
                rcTravel.getAdapter().notifyDataSetChanged();

                progressDialog.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<ArrayList<Wisata>> call, Throwable t) {
                Log.d(RETRAVEL_FRAG_TAG, t.getMessage());
            }
        });
    }

    /*public void loadWisataData() {
        final MyChoice myChoice = ((RekomendasiActivity) getActivity()).getMyChoice();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait!");
        progressDialog.show();
//        Toast.makeText(getContext(), myChoice.getBudget().toString(), Toast.LENGTH_SHORT).show();

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
                travels = response.body();
//                Log.d("Travel", String.valueOf(response.body().toString()));

                rcTravel.setAdapter(new RekomendasiTravelAdapter(getContext(), travels, myChoice));
                rcTravel.getAdapter().notifyDataSetChanged();

                progressDialog.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<ArrayList<Wisata>> call, Throwable t) {
                Log.d(RETRAVEL_FRAG_TAG, t.getMessage());
            }
        });
    }*/

}
