package arindatiko.example.com.travelmecustomer.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.googledirection.model.Line;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import arindatiko.example.com.travelmecustomer.API;
import arindatiko.example.com.travelmecustomer.R;
import arindatiko.example.com.travelmecustomer.adapter.MyTravelKamarAdapter;
import arindatiko.example.com.travelmecustomer.adapter.MyTravelMenuAdapter;
import arindatiko.example.com.travelmecustomer.adapter.MyTravelWisataAdapter;
import arindatiko.example.com.travelmecustomer.model.Drivers;
import arindatiko.example.com.travelmecustomer.model.Kamar;
import arindatiko.example.com.travelmecustomer.model.Kuliner;
import arindatiko.example.com.travelmecustomer.model.Menu;
import arindatiko.example.com.travelmecustomer.model.Pesanan;
import arindatiko.example.com.travelmecustomer.model.Tujuan;
import arindatiko.example.com.travelmecustomer.model.Wisata;
import arindatiko.example.com.travelmecustomer.util.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyTravelFragment extends Fragment {

    private RecyclerView rcTravel,rcHotel,rcRestaurant;
    private List<Wisata> travels = new ArrayList<>();
    private List<Tujuan> tujuans = new ArrayList<>();
    private List<Kamar> hotels = new ArrayList<>();
    private List<Kuliner> menus = new ArrayList<>();
    private Pesanan pesanan = new Pesanan();
    private Drivers driver = new Drivers();
    private TextView tvMyBudget, tvTotalBudget, tvName, tvNoTelp, tvId, tvNone;
    private LinearLayout lnMytravel;

    SharedPreferences sharedPreferences = null;
    SharedPreferences.Editor editor;

    public MyTravelFragment() {
        // Required empty public constructor
    }

    SessionManager sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_travel, container, false);

        sessionManager = new SessionManager(getActivity());
        String id_user = sessionManager.getId();

        sharedPreferences = getContext().getSharedPreferences("myTravel", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
//        editor.clear();
//        editor.commit();

        /*if(sharedPreferences.getString("id_wisata",null) == null){
            tvNone = (TextView) view.findViewById(R.id.tv_none);
            tvNone.setVisibility(View.VISIBLE);
        }else {*/
        lnMytravel = (LinearLayout) view.findViewById(R.id.ln_mytravel);

        rcTravel = (RecyclerView) view.findViewById(R.id.rc_travel);
        RecyclerView.LayoutManager travelLayout = new LinearLayoutManager(getContext());
        rcTravel.setLayoutManager(travelLayout);
        rcTravel.setItemAnimator(new DefaultItemAnimator());
        rcTravel.setFocusable(false);

        rcHotel = (RecyclerView) view.findViewById(R.id.rc_hotel);
        RecyclerView.LayoutManager travelLayout1 = new LinearLayoutManager(getContext());
        rcHotel.setLayoutManager(travelLayout1);
        rcHotel.setItemAnimator(new DefaultItemAnimator());
        rcHotel.setFocusable(false);

        rcRestaurant = (RecyclerView) view.findViewById(R.id.rc_restaurant);
        RecyclerView.LayoutManager travelLayout2 = new LinearLayoutManager(getContext());
        rcRestaurant.setLayoutManager(travelLayout2);
        rcRestaurant.setItemAnimator(new DefaultItemAnimator());
        rcRestaurant.setFocusable(false);

        tvMyBudget = (TextView) view.findViewById(R.id.tv_my_budget);
        tvMyBudget.setText("Rp " + sharedPreferences.getString("sisabudget", ""));

        tvTotalBudget = (TextView) view.findViewById(R.id.tv_total_budget);
        tvTotalBudget.setText("Rp " + sharedPreferences.getString("totalbudget", ""));

        tvName = (TextView) view.findViewById(R.id.tv_Name);
        tvName.setText(""+sharedPreferences.getString("name_driver",""));

        tvNoTelp = (TextView) view.findViewById(R.id.tv_NoTelp);
        tvNoTelp.setText(""+sharedPreferences.getString("notelp_driver",""));

        tvId = (TextView) view.findViewById(R.id.tv_idpesanan);
        tvId.setText(""+sharedPreferences.getString("id_pesanan",""));

        String id_wisata = sharedPreferences.getString("id_wisata", "");
        String id_kamar = sharedPreferences.getString("id_kamar", "");
        String id_menu = sharedPreferences.getString("id_menu", "");

        Log.d("TravelFragment", "true");
        Log.d("selectedWisata", id_wisata);
        Log.d("selectedKamar", id_kamar);
        Log.d("selectedMenu", id_menu);
        Log.d("total budget", sharedPreferences.getString("totalbudget", ""));
        Log.d("sisa budget", sharedPreferences.getString("sisabudget", ""));


        API.service_post.get_rekomendasi(id_wisata, "wisata", id_user).enqueue(new Callback<ArrayList<Tujuan>>() {
            @Override
            public void onResponse(Call<ArrayList<Tujuan>> call, Response<ArrayList<Tujuan>> response) {
                //tujuans.addAll(response.body());
                tujuans = response.body();

                tujuans.size();
                for (int i = 0; i < tujuans.size(); i++) {
                    if (tujuans.get(i).getJenis_layanan().equals("wisata")) {
                        rcTravel.setAdapter(new MyTravelWisataAdapter(getContext(), tujuans.get(i).getWisata()));
                        rcTravel.getAdapter().notifyDataSetChanged();
                    }
                }
                Log.d("success", response.body().toString());
            }

            @Override
            public void onFailure(Call<ArrayList<Tujuan>> call, Throwable t) {
                Log.d("error", t.getMessage());

            }
        });

        API.service_post.get_rekomendasi(id_kamar, "kamar", id_user).enqueue(new Callback<ArrayList<Tujuan>>() {
            @Override
            public void onResponse(Call<ArrayList<Tujuan>> call, Response<ArrayList<Tujuan>> response) {
                //tujuans.addAll(response.body());
                tujuans = response.body();

                tujuans.size();
                for (int i = 0; i < tujuans.size(); i++) {
                    if (tujuans.get(i).getJenis_layanan().equals("kamar")) {
                        rcHotel.setAdapter(new MyTravelKamarAdapter(getContext(), tujuans.get(i).getKamar()));
                        rcHotel.getAdapter().notifyDataSetChanged();
                    }
                }
                Log.d("success", response.body().toString());
            }

            @Override
            public void onFailure(Call<ArrayList<Tujuan>> call, Throwable t) {
                Log.d("error", t.getMessage());

            }
        });

        API.service_post.get_rekomendasi(id_menu, "menu", id_user).enqueue(new Callback<ArrayList<Tujuan>>() {
            @Override
            public void onResponse(Call<ArrayList<Tujuan>> call, Response<ArrayList<Tujuan>> response) {
                //tujuans.addAll(response.body());
                tujuans = response.body();

                tujuans.size();
                for (int i = 0; i < tujuans.size(); i++) {
                    if (tujuans.get(i).getJenis_layanan().equals("menu")) {
                        rcRestaurant.setAdapter(new MyTravelMenuAdapter(getContext(), tujuans.get(i).getKuliner()));
                        rcRestaurant.getAdapter().notifyDataSetChanged();
                    }
                }
                Log.d("success", response.body().toString());
            }

            @Override
            public void onFailure(Call<ArrayList<Tujuan>> call, Throwable t) {
                Log.d("error", t.getMessage());

            }
        });

        /*API.service_post.package_recomendation_wisata(id_wisata, "wisata").enqueue(new Callback<ArrayList<Wisata>>() {
            @Override
            public void onResponse(Call<ArrayList<Wisata>> call, Response<ArrayList<Wisata>> response) {
                travels = response.body();

                rcTravel.setAdapter(new MyTravelWisataAdapter(getContext(), travels));
                rcTravel.getAdapter().notifyDataSetChanged();

                Log.d("success", response.body().toString());
            }

            @Override
            public void onFailure(Call<ArrayList<Wisata>> call, Throwable t) {
                Log.d("error", t.getMessage());

            }
        });*/


            /*API.service_post.package_recomendation_kamar(id_kamar, "kamar").enqueue(new Callback<ArrayList<Kamar>>() {
                @Override
                public void onResponse(Call<ArrayList<Kamar>> call, Response<ArrayList<Kamar>> response) {
                    hotels = response.body();

                    rcHotel.setAdapter(new MyTravelKamarAdapter(getContext(), hotels));
                    rcHotel.getAdapter().notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<ArrayList<Kamar>> call, Throwable t) {

                }
            });

            API.service_post.package_recomendation_menu(id_menu, "menu").enqueue(new Callback<ArrayList<Kuliner>>() {
                @Override
                public void onResponse(Call<ArrayList<Kuliner>> call, Response<ArrayList<Kuliner>> response) {
                    menus = response.body();

                    rcRestaurant.setAdapter(new MyTravelMenuAdapter(getContext(), menus));
                    rcRestaurant.getAdapter().notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<ArrayList<Kuliner>> call, Throwable t) {

                }
            });*/
        return view;
    }
}
