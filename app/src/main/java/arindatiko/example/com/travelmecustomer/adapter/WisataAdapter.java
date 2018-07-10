package arindatiko.example.com.travelmecustomer.adapter;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import arindatiko.example.com.travelmecustomer.DetailWisataActivity;
import arindatiko.example.com.travelmecustomer.R;
import arindatiko.example.com.travelmecustomer.model.Wisata;

import static arindatiko.example.com.travelmecustomer.DetailWisataActivity.WISATA_ID;
import static arindatiko.example.com.travelmecustomer.fragment.HomeFragment.HOME_FRAG_TAG;

public class WisataAdapter extends RecyclerView.Adapter<WisataAdapter.MyViewHolder> {
    private Context context;
    private List<Wisata> travels;

    public WisataAdapter(Context context, List<Wisata> travels) {
        this.context = context;
        this.travels = travels;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Wisata wisata = travels.get(position);

        holder.tvTitle.setText(wisata.getNama());

        Glide.with(context)
                .load(wisata.getFoto())
                .into(holder.imgItem);

        Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(wisata.getPosisi_lat(), wisata.getPosisi_lng(), 1);

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

        holder.cardItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailWisataActivity.class);
                intent.putExtra(WISATA_ID, wisata.getId_wisata());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return travels.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView cardItem;
        private ImageView imgItem;
        private TextView tvTitle, tvAddress;

        public MyViewHolder(View itemView) {
            super(itemView);

            cardItem = (CardView) itemView.findViewById(R.id.card_item);
            imgItem = (ImageView) itemView.findViewById(R.id.img_item);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvAddress = (TextView) itemView.findViewById(R.id.tv_address);
        }
    }
}