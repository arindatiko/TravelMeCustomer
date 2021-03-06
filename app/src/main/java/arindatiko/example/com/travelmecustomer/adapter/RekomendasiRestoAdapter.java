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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import arindatiko.example.com.travelmecustomer.DetailKulinerActivity;
import arindatiko.example.com.travelmecustomer.R;
import arindatiko.example.com.travelmecustomer.model.Kuliner;
import arindatiko.example.com.travelmecustomer.model.Menu;
import arindatiko.example.com.travelmecustomer.model.MyChoice;
import arindatiko.example.com.travelmecustomer.model.Wisata;

import static arindatiko.example.com.travelmecustomer.DetailKulinerActivity.KULINER_ID;
import static arindatiko.example.com.travelmecustomer.fragment.HomeFragment.HOME_FRAG_TAG;

public class RekomendasiRestoAdapter extends RecyclerView.Adapter<RekomendasiRestoAdapter.MyViewHolder> {

    private Context context;
    //private List<Menu> menus;
    private List<Kuliner> kuliners;
    private MyChoice myChoice;
    private TextView tvMyBudget;
    private ProgressBar pbBudget;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public void replaceData(List<Kuliner> data) {
        this.kuliners = data;

        for (int i = 0; i < kuliners.size(); i++) {
            Double totalHarga = kuliners.get(i).getHarga_atas() * myChoice.getJumPorsi();

            kuliners.get(i).setTotalHarga(totalHarga);
        }
    }

    public RekomendasiRestoAdapter(Context context, List<Kuliner> kuliners, MyChoice myChoice, TextView tvMyBudget, ProgressBar pbBudget) {
        this.context = context;
        this.kuliners = kuliners;
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
        final Kuliner kuliner = kuliners.get(position);

        sharedPreferences = ((Activity)context).getSharedPreferences("myTravel",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        holder.tvTitle.setText(kuliner.getNama());
        holder.tvPrice.setText("Rp "+kuliner.getTotalHarga());
        holder.tvDetailPrice.setText("Harga tertinggi per porsi : Rp "+kuliner.getHarga_atas());
        holder.tvTime.setText(kuliner.getJam_buka() +" wib - "+ kuliner.getJam_tutup() +" wib");

        holder.cardItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailKulinerActivity.class);
                intent.putExtra(KULINER_ID, kuliner.getId_kuliner());
                context.startActivity(intent);
            }
        });

        holder.imgCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = ((Activity)context).getSharedPreferences("myTravel",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();


                if(sharedPreferences.getString("id_menu","").contains(","+String.valueOf(kuliner.getId_kuliner()))){
                    holder.imgCheck.setImageTintList(ColorStateList.valueOf(Color.parseColor("#D5D5D5")));
                    myChoice.setBudget(myChoice.getBudget()+kuliner.getTotalHarga());

                    String add_menu = sharedPreferences.getString("id_menu","");
                    add_menu = add_menu.replace(","+String.valueOf(kuliner.getId_kuliner()),"");
                    editor.putString("id_menu", String.valueOf(add_menu));
                    editor.commit();
                }
                else {
                    if (kuliner.getTotalHarga()>myChoice.getBudget()){
                        Toast.makeText(context, "Budget anda tidak cukup", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        holder.imgCheck.setImageTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                        holder.tvPrice.setText("Rp "+kuliner.getTotalHarga());

                        myChoice.setBudget(myChoice.getBudget()-kuliner.getTotalHarga());

                        String add_menu = sharedPreferences.getString("id_menu","")+","+kuliner.getId_kuliner();
                        editor.putString("id_menu", String.valueOf(add_menu));
                        editor.commit();
                    }
                }
                editor.putString("sisabudget", String.valueOf(myChoice.getBudget()));
                editor.commit();

                pbBudget.setProgress(myChoice.getBudget().intValue());
                tvMyBudget.setText("Rp "+ myChoice.getBudget());

                Log.d("selectedMenu",sharedPreferences.getString("id_menu",""));
                Log.d("budget", String.valueOf(myChoice.getBudget()));
            }
        });

        holder.imgCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + kuliner.getNo_telp()));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    context.startActivity(intent);
                }
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

    public void updateWisataList(List<Kuliner> kuliners){
        Collections.sort(kuliners, new Comparator<Kuliner>() {
            @Override
            public int compare(Kuliner o1, Kuliner o2) {
                return (int) (o1.getTotalHarga() - o2.getTotalHarga());

            }
        });
        this.notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView cardItem;
        private ImageView imgItem, imgCall, imgCheck, imgHide, imgShow;
        private TextView tvTitle, tvAddress, tvPrice, tvDetailPrice, tvTime, tvResto, tvJumPorsi;
        private LinearLayout ln_porsi;
        private EditText etJumPorsi;
        private RelativeLayout rvDone;

        public MyViewHolder(View itemView) {
            super(itemView);

            cardItem = (CardView) itemView.findViewById(R.id.card_item);
            imgItem = (ImageView) itemView.findViewById(R.id.img_item);
            imgCall = (ImageView) itemView.findViewById(R.id.img_call);
            imgCheck = (ImageView) itemView.findViewById(R.id.img_check);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvResto = (TextView) itemView.findViewById(R.id.tv_resto);
            tvAddress = (TextView) itemView.findViewById(R.id.tv_address);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            tvDetailPrice = (TextView) itemView.findViewById(R.id.tv_detail_price);
            tvJumPorsi = (TextView) itemView.findViewById(R.id.tv_jum_porsi);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);

            ln_porsi = (LinearLayout) itemView.findViewById(R.id.ln_porsi);
            etJumPorsi = (EditText) itemView.findViewById(R.id.et_jumPorsi);
            rvDone = (RelativeLayout) itemView.findViewById(R.id.rv_done);
        }
    }
}
