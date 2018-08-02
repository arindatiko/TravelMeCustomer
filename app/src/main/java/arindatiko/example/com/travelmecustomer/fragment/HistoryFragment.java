package arindatiko.example.com.travelmecustomer.fragment;

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
import android.widget.LinearLayout;

import java.util.ArrayList;

import arindatiko.example.com.travelmecustomer.API;
import arindatiko.example.com.travelmecustomer.R;
import arindatiko.example.com.travelmecustomer.adapter.HistoryAdapter;
import arindatiko.example.com.travelmecustomer.model.Pesanan;
import arindatiko.example.com.travelmecustomer.util.SessionManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment {

    @BindView(R.id.rc_history)
    RecyclerView rcHistory;
    @BindView(R.id.ln_mytravel)
    LinearLayout lnMyTravel;

    private ArrayList<Pesanan> pesanan = new ArrayList<>();

    SessionManager sessionManager;
    HistoryAdapter historyAdapter;

    public HistoryFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, v);

        sessionManager = new SessionManager(getActivity());

        RecyclerView.LayoutManager layout = new LinearLayoutManager(getContext());
        rcHistory.setLayoutManager(layout);
        rcHistory.setItemAnimator(new DefaultItemAnimator());
        rcHistory.setFocusable(false);

        /*pesanan.add(new Pesanan("Tukijo", 720000.0, "31-07-2018 07:15:00 PM"));

        historyAdapter = new HistoryAdapter(getContext(),pesanan);
        rcHistory.setAdapter(historyAdapter);
        rcHistory.getAdapter().notifyDataSetChanged();*/

        //Toast.makeText(getActivity(), sessionManager.getId(), Toast.LENGTH_SHORT).show();
        API.service_post.history_pesanan(sessionManager.getId()).enqueue(new Callback<ArrayList<Pesanan>>() {
            @Override
            public void onResponse(Call<ArrayList<Pesanan>> call, Response<ArrayList<Pesanan>> response) {
                pesanan = response.body();

                //Toast.makeText(getActivity(), "test", Toast.LENGTH_SHORT).show();
                historyAdapter = new HistoryAdapter(getContext(), pesanan);
                rcHistory.setAdapter(historyAdapter);
                rcHistory.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<Pesanan>> call, Throwable t) {
                Log.d("cek", t.getMessage());
            }
        });

        return v;
    }

    public void onResume(){
        super.onResume();
    }
}
