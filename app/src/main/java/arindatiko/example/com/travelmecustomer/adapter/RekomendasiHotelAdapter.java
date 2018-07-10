package arindatiko.example.com.travelmecustomer.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import arindatiko.example.com.travelmecustomer.DetailKamarActivity;
import arindatiko.example.com.travelmecustomer.R;
import arindatiko.example.com.travelmecustomer.model.Kamar;
import arindatiko.example.com.travelmecustomer.model.MyChoice;

import static arindatiko.example.com.travelmecustomer.DetailKamarActivity.KAMAR_ID;
import static arindatiko.example.com.travelmecustomer.fragment.HomeFragment.HOME_FRAG_TAG;

public class RekomendasiHotelAdapter extends RecyclerView.Adapter<RekomendasiHotelAdapter.MyViewHolder> {
    private Context context;
    private List<Kamar> hotels;
    private MyChoice myChoice;
    private TextView tvMyBudget;
    private ProgressBar pbBudget;

    public RekomendasiHotelAdapter(Context context, List<Kamar> hotels, MyChoice myChoice, TextView tvMyBudget, ProgressBar pbBudget) {
        this.context = context;
        this.hotels = hotels;
        this.myChoice = myChoice;
        this.tvMyBudget = tvMyBudget;
        this.pbBudget = pbBudget;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_travel, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Kamar kamar = hotels.get(position);
        final Double totalHarga = kamar.getHarga() * myChoice.getJumKamar() * myChoice.getJumDay();

        holder.tvTitle.setText(kamar.getNama());
        holder.tvResto.setVisibility(View.VISIBLE);
        holder.tvResto.setText(kamar.getPenginapan().getNama());
        holder.tvPrice.setText("Rp "+totalHarga);
        holder.tvDetailPrice.setText("Jumlah kamar : "+ myChoice.getJumKamar() +"\nJumlah hari : "+ myChoice.getJumDay() +"\nHarga kamar : "+ kamar.getHarga());
        holder.tvTime.setVisibility(View.GONE);

        holder.cardItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailKamarActivity.class);
                intent.putExtra(KAMAR_ID, kamar.getId_kamar());
                context.startActivity(intent);
            }
        });

        holder.imgCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = ((Activity)context).getSharedPreferences("myTravel",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();


                if(sharedPreferences.getString("id_kamar","").contains(","+String.valueOf(kamar.getId_kamar()))){
                    holder.imgCheck.setImageTintList(ColorStateList.valueOf(Color.parseColor("#D5D5D5")));
                    myChoice.setBudget(myChoice.getBudget()+totalHarga);

                    //tampung total
                    //myChoice.setTotalBiaya(myChoice.getTotalBiaya()+totalHarga);

                    String add_kamar = sharedPreferences.getString("id_kamar","");
                    add_kamar = add_kamar.replace(","+String.valueOf(kamar.getId_kamar()),"");
                    editor.putString("id_kamar", String.valueOf(add_kamar));
                    editor.commit();
                }
                else {
                    if (totalHarga>myChoice.getBudget()){
                        Toast.makeText(context, "Budget anda tidak cukup", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        holder.imgCheck.setImageTintList(ColorStateList.valueOf(Color.parseColor("#000000")));

                        myChoice.setBudget(myChoice.getBudget()-totalHarga);

                        String add_kamar = sharedPreferences.getString("id_kamar","")+","+kamar.getId_kamar();
                        editor.putString("id_kamar", String.valueOf(add_kamar));
                        editor.commit();
                    }
                }

                pbBudget.setProgress(myChoice.getBudget().intValue());
                tvMyBudget.setText("Rp "+ myChoice.getBudget());

                Log.d("selectedKamar",sharedPreferences.getString("id_kamar",""));
                Log.d("budget", String.valueOf(myChoice.getBudget()));
            }
        });

        holder.imgCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + kamar.getPenginapan().getNo_telp()));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    context.startActivity(intent);
                }
            }
        });

        Glide.with(context)
                .load(kamar.getPenginapan().getFoto())
                .into(holder.imgItem);

        Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(kamar.getPenginapan().getPosisi_lat(), kamar.getPenginapan().getPosisi_lng(), 1);

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
        return hotels.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView cardItem;
        private ImageView imgItem, imgCall, imgCheck;
        private TextView tvTitle, tvAddress, tvPrice, tvDetailPrice, tvTime, tvResto;

        public MyViewHolder(View itemView) {
            super(itemView);

            cardItem = (CardView) itemView.findViewById(R.id.card_item);
            imgItem = (ImageView) itemView.findViewById(R.id.img_item);
            imgCall = (ImageView) itemView.findViewById(R.id.img_call);
            imgCheck = (ImageView) itemView.findViewById(R.id.img_check);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvAddress = (TextView) itemView.findViewById(R.id.tv_address);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            tvDetailPrice = (TextView) itemView.findViewById(R.id.tv_detail_price);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvResto = (TextView) itemView.findViewById(R.id.tv_resto);
        }
    }
}
