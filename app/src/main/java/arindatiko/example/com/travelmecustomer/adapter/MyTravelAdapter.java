package arindatiko.example.com.travelmecustomer.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import arindatiko.example.com.travelmecustomer.MapActivity;
import arindatiko.example.com.travelmecustomer.R;
import arindatiko.example.com.travelmecustomer.model.MyChoice;
import arindatiko.example.com.travelmecustomer.model.Pesanan;
import arindatiko.example.com.travelmecustomer.model.Tujuan;

import static arindatiko.example.com.travelmecustomer.DetailKamarActivity.KAMAR_ID;
import static arindatiko.example.com.travelmecustomer.fragment.HomeFragment.HOME_FRAG_TAG;

public class MyTravelAdapter extends RecyclerView.Adapter<MyTravelAdapter.MyViewHolder> {
    private Context context;
    private MyChoice myChoice;
    private TextView tvMyBudget, tvStatus;
    private ProgressBar pbBudget;
    private List<Pesanan> pesanans;

    public MyTravelAdapter(Context context, MyChoice myChoice, List<Pesanan> pesanans) {
        this.context = context;
        this.myChoice = myChoice;
        this.pesanans = pesanans;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final Pesanan pesanan = pesanans.get(position);


    }

    @Override
    public int getItemCount() {
        return pesanans.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private CardView cardItem, cardStatus;
        private TextView tvTitle, tvTotalBudget, tvTimestamp;

        public MyViewHolder(View itemView){
            super(itemView);

            cardItem = (CardView) itemView.findViewById(R.id.card_mytravel);
            cardStatus = (CardView) itemView.findViewById(R.id.card_status);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title_mytravel);
            tvTotalBudget = (TextView) itemView.findViewById(R.id.tv_total_budget);
            tvTimestamp = (TextView) itemView.findViewById(R.id.tv_timestamp);
        }
    }
}
