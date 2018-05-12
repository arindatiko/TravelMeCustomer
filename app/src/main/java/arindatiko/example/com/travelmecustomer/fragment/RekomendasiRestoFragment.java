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
import arindatiko.example.com.travelmecustomer.adapter.RekomendasiRestoAdapter;
import arindatiko.example.com.travelmecustomer.model.Menu;
import arindatiko.example.com.travelmecustomer.model.MyChoice;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RekomendasiRestoFragment extends Fragment {
    public static final String RERESTO_FRAG_TAG = "RekomendasiRestoFragment";

    private RecyclerView rcRestaurant;
    private List<Menu> menus = new ArrayList<>();

    public RekomendasiRestoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rekomendasi_resto, container, false);

        rcRestaurant = (RecyclerView) view.findViewById(R.id.rc_restaurant);

        RecyclerView.LayoutManager travelLayout = new LinearLayoutManager(getContext());
        rcRestaurant.setLayoutManager(travelLayout);
        rcRestaurant.setItemAnimator(new DefaultItemAnimator());
        rcRestaurant.setFocusable(false);

        loadMenuData();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RekomendasiActivity.currentFragment = RekomendasiActivity.HOTEL;
    }

    public void loadMenuData() {
        final MyChoice myChoice = ((RekomendasiActivity) getActivity()).getMyChoice();
        final TextView tvMyBudget = ((RekomendasiActivity) getActivity()).getTvMyBudget();
        final ProgressBar pbBudget = ((RekomendasiActivity) getActivity()).getPbBudget();

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

                rcRestaurant.setAdapter(new RekomendasiRestoAdapter(getContext(), menus, myChoice, tvMyBudget, pbBudget));
                rcRestaurant.getAdapter().notifyDataSetChanged();

                progressDialog.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<ArrayList<Menu>> call, Throwable t) {
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

