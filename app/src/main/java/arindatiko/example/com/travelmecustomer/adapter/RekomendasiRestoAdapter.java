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
import java.util.List;
import java.util.Locale;

import arindatiko.example.com.travelmecustomer.R;
import arindatiko.example.com.travelmecustomer.model.Menu;
import arindatiko.example.com.travelmecustomer.model.MyChoice;

import static arindatiko.example.com.travelmecustomer.fragment.HomeFragment.HOME_FRAG_TAG;

public class RekomendasiRestoAdapter extends RecyclerView.Adapter<RekomendasiRestoAdapter.MyViewHolder> {

    private Context context;
    private List<Menu> menus;
    private MyChoice myChoice;
    private TextView tvMyBudget;
    private ProgressBar pbBudget;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private int jumPorsi = 0, kode = 0, cek = 0;
    private Double totalHarga = 0.0;
    private boolean click = false;

    public RekomendasiRestoAdapter(Context context, List<Menu> menus, MyChoice myChoice, TextView tvMyBudget, ProgressBar pbBudget) {
        this.context = context;
        this.menus = menus;
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

        final Menu menu = menus.get(position);
        sharedPreferences = ((Activity)context).getSharedPreferences("myTravel",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        holder.tvTitle.setText(menu.getNama());
        holder.tvResto.setVisibility(View.VISIBLE);
        holder.tvResto.setText(menu.getKuliner().getNama());
        holder.tvDetailPrice.setText("Harga per porsi : Rp "+menu.getHarga());
        holder.tvTime.setText(menu.getKuliner().getJam_buka() +" wib - "+ menu.getKuliner().getJam_tutup() +" wib");

       /* holder.imgDrink.setImageResource(menu.getDrinks().get(0).getImage());
        holder.imgFood.setImageResource(menu.getFoods().get(0).getImage());*/

        /*holder.cardItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailKulinerActivity.class);
                intent.putExtra(KULINER_ID, menu.getId_kuliner());
                context.startActivity(intent);
            }
        });*/

        holder.imgCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cek == 0){
                    holder.ln_porsi.setVisibility(View.VISIBLE);
                    holder.imgHide.setVisibility(View.VISIBLE);
                    cek++;
                }

                if(sharedPreferences.getString("id_menu","").contains(","+String.valueOf(menu.getId_menu()))){
                    holder.imgCheck.setImageTintList(ColorStateList.valueOf(Color.parseColor("#D5D5D5")));

                    String add_menu = sharedPreferences.getString("id_menu","");
                    add_menu = add_menu.replace(","+String.valueOf(menu.getId_menu()),"");

                    editor.putString("id_menu", String.valueOf(add_menu));
                    editor.commit();
                }
                else {
                    int choose = 0;
                    if(choose == 0) {
                        holder.imgCheck.setImageTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                        String add_menu = sharedPreferences.getString("id_menu", "") + "," + menu.getId_menu();

                        editor.putString("id_menu", String.valueOf(add_menu));
                        editor.commit();
                    }
                }

                Log.d("selectedMenu",sharedPreferences.getString("id_menu",""));
                Log.d("budget", String.valueOf(myChoice.getBudget()));
            }

        });

        holder.rvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click = true;
                if(click = true){
                    if(holder.etJumPorsi.getText().toString().equals("")){
                        jumPorsi = 0;
                        myChoice.setBudget(myChoice.getBudget() + totalHarga);
                    }else{
                        jumPorsi = Integer.parseInt(holder.etJumPorsi.getText().toString());
                    }

                    totalHarga = menu.getHarga() * jumPorsi;

                    holder.tvPrice.setText("Rp "+totalHarga);
                    holder.tvJumPorsi.setVisibility(View.VISIBLE);
                    holder.tvJumPorsi.setText("Jumlah porsi : " + jumPorsi);

                    if (totalHarga<myChoice.getBudget()){
                        myChoice.setBudget(myChoice.getBudget() - totalHarga);

                        editor.putString("sisabudget", String.valueOf(myChoice.getBudget()));
                        editor.commit();

                        pbBudget.setProgress(myChoice.getBudget().intValue());
                        tvMyBudget.setText("Rp "+ myChoice.getBudget());
                    }else{
                        Toast.makeText(context, "Budget anda tidak cukup", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        holder.imgHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(kode % 2 == 0){
                    holder.imgHide.setImageTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                    holder.ln_porsi.setVisibility(View.GONE);
                    kode++;
                }else{
                    holder.imgHide.setImageTintList(ColorStateList.valueOf(Color.parseColor("#D5D5D5")));
                    holder.ln_porsi.setVisibility(View.VISIBLE);
                    kode++;
                }
            }
        });

        holder.imgCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + menu.getKuliner().getNo_telp()));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    context.startActivity(intent);
                }
            }
        });

        Glide.with(context)
                .load(menu.getFoto())
                .into(holder.imgItem);

        Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(menu.getKuliner().getPosisi_lat(), menu.getKuliner().getPosisi_lng(), 1);

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
        return menus.size();
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
            imgHide = (ImageView) itemView.findViewById(R.id.img_hide);
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
