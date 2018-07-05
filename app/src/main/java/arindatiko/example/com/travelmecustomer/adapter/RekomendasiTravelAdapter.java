package arindatiko.example.com.travelmecustomer.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import arindatiko.example.com.travelmecustomer.R;
import arindatiko.example.com.travelmecustomer.model.MyChoice;
import arindatiko.example.com.travelmecustomer.model.Wisata;

import static arindatiko.example.com.travelmecustomer.fragment.HomeFragment.HOME_FRAG_TAG;

public class RekomendasiTravelAdapter extends RecyclerView.Adapter<RekomendasiTravelAdapter.MyViewHolder> {
    private Context context;
    private List<Wisata> travels;
    private MyChoice myChoice;
    private TextView tvMyBudget;
    private ProgressBar pbBudget;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private int choose = 0;

    public RekomendasiTravelAdapter(Context context, List<Wisata> travels, MyChoice myChoice, TextView tvMyBudget, ProgressBar pbBudget) {
        this.context = context;
        this.travels = travels;
        this.myChoice = myChoice;
        this.tvMyBudget = tvMyBudget;
        this.pbBudget = pbBudget;

        sharedPreferences = ((Activity)context).getSharedPreferences("myTravel",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("totalbudget", String.valueOf(myChoice.getBudget()));
        editor.commit();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_travel, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Wisata wisata = travels.get(position);

        final Double totalHarga = myChoice.getTicketMotor() * wisata.getBiaya_parkir_motor() + myChoice.getTicketCar() * wisata.getBiaya_parkir_mobil() + myChoice.getTicketBus() * wisata.getBiaya_parkir_bus() + myChoice.getTicketAdult() * wisata.getTiket_masuk_dewasa() + myChoice.getTicketChild() * wisata.getTiket_masuk_anak();

        String detailPrice = null;
        if (myChoice.getTicketAdult() != 0) { detailPrice = "Tiket dewasa : "+ wisata.getTiket_masuk_dewasa().toString() +" x "+ myChoice.getTicketAdult() +" = "+ myChoice.getTicketAdult() * wisata.getTiket_masuk_dewasa(); }
        if (myChoice.getTicketChild() != 0 && myChoice.getTicketAdult() != 0) {
            detailPrice += "\nTiket anak : "+ wisata.getTiket_masuk_anak().toString() +" x "+ myChoice.getTicketChild() +" = "+ myChoice.getTicketChild() * wisata.getTiket_masuk_anak();
        } else if (myChoice.getTicketChild() != 0) {
            detailPrice = "Tiket anak : "+ wisata.getTiket_masuk_anak().toString() +" x "+ myChoice.getTicketChild() +" = "+ myChoice.getTicketChild() * wisata.getTiket_masuk_anak();
        }
        if (myChoice.getTicketMotor() != 0) { detailPrice +="\nParkir motor : "+ wisata.getBiaya_parkir_motor() +" x "+ myChoice.getTicketMotor() +" = "+ myChoice.getTicketMotor() * wisata.getBiaya_parkir_motor(); }
        if (myChoice.getTicketCar() != 0) { detailPrice +="\nParkir mobil : "+ wisata.getBiaya_parkir_mobil() +" x "+ myChoice.getTicketCar() +" = "+ myChoice.getTicketCar() * wisata.getBiaya_parkir_mobil(); }
        if (myChoice.getTicketBus() != 0) { detailPrice +="\nParkir bus : "+ wisata.getBiaya_parkir_bus() +" x "+ myChoice.getTicketBus() +" = "+ myChoice.getTicketBus() * wisata.getBiaya_parkir_bus(); }

        holder.tvTitle.setText(wisata.getNama());
        holder.tvPrice.setText("Rp "+ totalHarga.toString());
        holder.tvDetailPrice.setText(detailPrice);
        holder.tvTime.setText(wisata.getJam_buka() +" wib - "+ wisata.getJam_tutup() +" wib");
        holder.imgCall.setVisibility(View.GONE);

        /*holder.cardItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailWisataActivity.class);
                intent.putExtra(WISATA_ID, wisata.getId_wisata());
                context.startActivity(intent);
            }
        });*/

        /*String key = sharedPreferences.getString("key_wisata", "");
        if(!key.equals("")) {
            holder.imgCheck.setImageTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
        }
*/
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

        holder.imgCheck.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                sharedPreferences = ((Activity)context).getSharedPreferences("myTravel",Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();

                if(sharedPreferences.getString("id_wisata","").contains(","+String.valueOf(wisata.getId_wisata()))){
                    choose = 0;
                    holder.imgCheck.setImageTintList(ColorStateList.valueOf(Color.parseColor("#D5D5D5")));

                    myChoice.setBudget(myChoice.getBudget()+totalHarga);

                    //tampung total
                    //myChoice.setTotalBiaya(totalHarga);

                    String add_wisata = sharedPreferences.getString("id_wisata","");
                    add_wisata = add_wisata.replace(","+String.valueOf(wisata.getId_wisata()),"");
                    editor.putString("id_wisata", String.valueOf(add_wisata));
                    editor.commit();
                }
                else {
                    if (totalHarga>myChoice.getBudget()){
                        Toast.makeText(context, "Budget anda tidak cukup", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        holder.imgCheck.setImageTintList(ColorStateList.valueOf(Color.parseColor("#000000")));

                        myChoice.setBudget(myChoice.getBudget() - totalHarga);

                        String add_wisata = sharedPreferences.getString("id_wisata", "") + "," + wisata.getId_wisata();
                        editor.putString("id_wisata", String.valueOf(add_wisata));
                        editor.commit();
                    }
                }

                pbBudget.setProgress(myChoice.getBudget().intValue());
                tvMyBudget.setText("Rp "+ myChoice.getBudget());

                Log.d("selectedTravel",sharedPreferences.getString("id_wisata",""));
                Log.d("budget", String.valueOf(myChoice.getBudget()));

            }
        });
    }

    @Override
    public int getItemCount() {
        return travels.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView cardItem;
        private ImageView imgItem, imgCall, imgCheck;
        private TextView tvTitle, tvAddress, tvPrice, tvDetailPrice, tvTime;
        private LinearLayout lnItem;

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
            lnItem = (LinearLayout) itemView.findViewById(R.id.ln_item);
        }
    }
}