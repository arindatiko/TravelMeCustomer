package arindatiko.example.com.travelmecustomer.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import arindatiko.example.com.travelmecustomer.R;
import arindatiko.example.com.travelmecustomer.model.Kamar;
import arindatiko.example.com.travelmecustomer.model.Menu;
import arindatiko.example.com.travelmecustomer.model.Pesanan;
import arindatiko.example.com.travelmecustomer.model.Wisata;
import butterknife.ButterKnife;

public class MyTravelFragment2 extends Fragment {

    private RecyclerView rcMyTravel;
    private List<Pesanan> pesanans = new ArrayList<>();
    private TextView tvMyBudget, tvTotalBudget;

    public MyTravelFragment2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_my_travel_fragment2, container, false);


        SharedPreferences sharedPreferences = getContext().getSharedPreferences("myTravel", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String id_wisata = sharedPreferences.getString("id_wisata","");
        String id_kamar = sharedPreferences.getString("id_kamar","");
        String id_menu = sharedPreferences.getString("id_menu","");

        rcMyTravel = (RecyclerView) view.findViewById(R.id.rc_my_travel);
        RecyclerView.LayoutManager travelLayout = new LinearLayoutManager(getContext());
        rcMyTravel.setLayoutManager(travelLayout);
        rcMyTravel.setItemAnimator(new DefaultItemAnimator());
        rcMyTravel.setFocusable(false);
        return view;
    }
}
