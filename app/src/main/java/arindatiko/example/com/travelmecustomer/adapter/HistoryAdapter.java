package arindatiko.example.com.travelmecustomer.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import arindatiko.example.com.travelmecustomer.API;
import arindatiko.example.com.travelmecustomer.HistoryActivity;
import arindatiko.example.com.travelmecustomer.MainActivity;
import arindatiko.example.com.travelmecustomer.R;
import arindatiko.example.com.travelmecustomer.model.Pesanan;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by arindatiko on 27/03/2018.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyHolder> {
    private Context context;
    private ArrayList<Pesanan> pesanans;
    private Pesanan pesanan;

    public HistoryAdapter(Context context, ArrayList<Pesanan> dataHistory) {
        this.context = context;
        this.pesanans = dataHistory;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);

        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
        //final HistoryAdapter.MyHolder myHolder = (HistoryAdapter.MyHolder) holder;
        pesanan = pesanans.get(position);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, HistoryActivity.class);
                context.startActivity(i);
            }
        });

        /*API.service_post.get_cust(String.valueOf(pesanan.getId_customer())).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                Toast.makeText(context, "test", Toast.LENGTH_SHORT).show();
                holder.tvName.setText(user.getNama_lengkap());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });*/

        holder.tvTital.setText(String.valueOf(pesanan.getTotal_budget()));
        holder.tvTime.setText(pesanan.getWaktu_pesan());
    }

    @Override
    public int getItemCount() {
        return pesanans.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder{
        private TextView tvName, tvTime, tvTital;
        private CardView cardView;

        public MyHolder(View view){
            super(view);

            cardView = (CardView) view.findViewById(R.id.card_items);
            tvName = (TextView) view.findViewById(R.id.tv_title);
            tvTime = (TextView) view.findViewById(R.id.tv_date);
            tvTital = (TextView) view.findViewById(R.id.tv_total);

        }
    }
}
