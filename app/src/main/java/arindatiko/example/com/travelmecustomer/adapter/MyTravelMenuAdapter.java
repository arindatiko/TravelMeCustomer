package arindatiko.example.com.travelmecustomer.adapter;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import arindatiko.example.com.travelmecustomer.API;
import arindatiko.example.com.travelmecustomer.R;
import arindatiko.example.com.travelmecustomer.model.Kuliner;
import arindatiko.example.com.travelmecustomer.model.Menu;
import arindatiko.example.com.travelmecustomer.model.Rekomendasi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static arindatiko.example.com.travelmecustomer.fragment.HomeFragment.HOME_FRAG_TAG;

public class MyTravelMenuAdapter extends RecyclerView.Adapter<MyTravelMenuAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Kuliner> kuliners = new ArrayList<>();
    private ArrayList<Rekomendasi> rekomendasis = new ArrayList<>();

    /*public MyTravelMenuAdapter(Context context, List<Kuliner> kuliners) {
        this.context = context;
        this.kuliners = kuliners;
    }*/

    public MyTravelMenuAdapter(Context context, ArrayList<Rekomendasi> rekomendasis) {
        this.context = context;
        this.rekomendasis = rekomendasis;
        for (int i = 0; i < rekomendasis.size(); i++) {
            kuliners.add(rekomendasis.get(i).getKuliner().get(0));
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_travel, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Kuliner kuliner = kuliners.get(position);

        holder.tvTitle.setText(kuliner.getNama());
        holder.tvTime.setVisibility(View.GONE);
        holder.imgCall.setVisibility(View.GONE);
        holder.lnHarga.setVisibility(View.GONE);

        API.service_post.get_rekomendasi_2(rekomendasis.get(position).getId_rekomendasi(),
                "menu",
                rekomendasis.get(position).getKuliner().get(0).getId_kuliner()).enqueue(new Callback<Rekomendasi>() {
            @Override
            public void onResponse(Call<Rekomendasi> call, Response<Rekomendasi> response) {
                Rekomendasi r = response.body();
                if(r.getFlag() == 1){
                    holder.lnSelesai.setVisibility(View.VISIBLE);
                }else{
                    holder.lnSelesai.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Rekomendasi> call, Throwable t) {

            }
        });

        Glide.with(context)
                .load(kuliner.getFoto())
                .into(holder.imgItem);

        Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(kuliner.getPosisi_lat(), kuliner.getPosisi_lng(), 1);

            if (addresses.size() > 0) {
                Address fetchedAddress = addresses.get(0);
                holder.tvAddress.setText(fetchedAddress.getAddressLine(0));
            } else {
                holder.tvAddress.setText("-");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(HOME_FRAG_TAG, e.getMessage());
            holder.tvAddress.setText("-");
        }
    }

    @Override
    public int getItemCount() {
        return kuliners.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView cardItem;
        private ImageView imgItem, imgCall, imgCheck, imgSelesai;
        private TextView tvTitle, tvAddress, tvPrice, tvDetailPrice, tvTime, tvSelesai;
        private LinearLayout lnItem, lnHarga, lnSelesai;


        public MyViewHolder(View itemView) {
            super(itemView);

            cardItem = (CardView) itemView.findViewById(R.id.card_item);
            lnHarga = (LinearLayout) itemView.findViewById(R.id.ln_harga);
            imgItem = (ImageView) itemView.findViewById(R.id.img_item);
            imgCall = (ImageView) itemView.findViewById(R.id.img_call);
            imgCheck = (ImageView) itemView.findViewById(R.id.img_check);
            imgSelesai = (ImageView) itemView.findViewById(R.id.check);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvAddress = (TextView) itemView.findViewById(R.id.tv_address);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            tvDetailPrice = (TextView) itemView.findViewById(R.id.tv_detail_price);
            tvSelesai = (TextView) itemView.findViewById(R.id.tv_done);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            lnItem = (LinearLayout) itemView.findViewById(R.id.ln_item);
            lnSelesai = (LinearLayout) itemView.findViewById(R.id.ln_selesai);
        }
    }
}
