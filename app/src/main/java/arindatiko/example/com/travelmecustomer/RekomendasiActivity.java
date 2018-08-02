package arindatiko.example.com.travelmecustomer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import arindatiko.example.com.travelmecustomer.fragment.RekomendasiHotelFragment;
import arindatiko.example.com.travelmecustomer.fragment.RekomendasiRestoFragment;
import arindatiko.example.com.travelmecustomer.fragment.RekomendasiTravelFragment;
import arindatiko.example.com.travelmecustomer.model.MyChoice;

import static arindatiko.example.com.travelmecustomer.LetsActivity.MYCHOICE;

public class RekomendasiActivity extends AppCompatActivity {

    public static final String RECOMEND_ACTIVITY_TAG = "RekomendasiActivity";

    public static final String TRAVEL = "TRAVEL";
    public static final String HOTEL = "HOTEL";
    public static final String RESTAURANT = "RESTAURANT";
    //public static String currentFragment = TRAVEL;
    public static String currentFragment = null;

    public static MyChoice myChoice = new MyChoice();
    SharedPreferences sharedPreferences = null;
    SharedPreferences.Editor editor;

    private RelativeLayout rvNext;
    private ProgressBar pbBudget;
    private TextView tvMyBudget;
    private TextView tvTotalBudget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekomendasi);

        myChoice = getIntent().getParcelableExtra(MYCHOICE);

        sharedPreferences = getApplicationContext().getSharedPreferences("myTravel", Context.MODE_PRIVATE);;
        editor = sharedPreferences.edit();

        pbBudget = (ProgressBar) findViewById(R.id.pb_budget);
        tvMyBudget = (TextView) findViewById(R.id.tv_my_budget);
        tvTotalBudget = (TextView) findViewById(R.id.tv_total_budget);
        rvNext = (RelativeLayout) findViewById(R.id.rv_next);

        pbBudget.setIndeterminate(false);
        pbBudget.setMax(myChoice.getBudget().intValue());
        pbBudget.setProgress(myChoice.getBudget().intValue());
        tvMyBudget.setText("Rp "+ myChoice.getBudget());
        tvTotalBudget.setText("Rp "+ myChoice.getBudget());

        rvNext = (RelativeLayout) findViewById(R.id.rv_next);

        rvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentFragment == TRAVEL){
                    String id_wisata = sharedPreferences.getString("id_wisata", "");
                    if(id_wisata.equals("")){
                        Toast.makeText(RekomendasiActivity.this, "Harap Pilih Tempat Wisata Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                    }else{
                        if(myChoice.getJumKamar() != 0 && myChoice.getJumDay() != 0) {
                            loadFragment(new RekomendasiHotelFragment());
                            currentFragment = HOTEL;
                        }else if(myChoice.getJumPorsi() != 0){
                            loadFragment(new RekomendasiRestoFragment());
                            currentFragment = RESTAURANT;
                        }else{
                            Intent intent = new Intent(RekomendasiActivity.this, MapActivity2.class);
                            intent.putExtra("myChoice", myChoice);
                            intent.putExtra("budget", String.valueOf(tvMyBudget));
                            intent.putExtra("progressBar", String.valueOf(pbBudget));
                            intent.putExtra("totalbudget", String.valueOf(myChoice.getBudget()));
                            startActivity(intent);
                            finish();
                        }

                    }
                }else if (currentFragment == HOTEL){
                    String id_kamar = sharedPreferences.getString("id_kamar", "");
                    Log.d("id_kamar", id_kamar);
                    if(id_kamar.equals("")){
                        Toast.makeText(RekomendasiActivity.this, "Harap Pilih Kamar Penginapan Terlebuh Dahulu", Toast.LENGTH_SHORT).show();
                    }else{
                        if(myChoice.getJumPorsi() != 0) {
                            loadFragment(new RekomendasiRestoFragment());
                            currentFragment = RESTAURANT;
                        }else{
                            Intent intent = new Intent(RekomendasiActivity.this, MapActivity2.class);
                            intent.putExtra("myChoice", myChoice);
                            intent.putExtra("budget", String.valueOf(tvMyBudget));
                            intent.putExtra("progressBar", String.valueOf(pbBudget));
                            intent.putExtra("totalbudget", String.valueOf(myChoice.getBudget()));
                            startActivity(intent);
                            finish();
                        }
                    }
                }else{
                    String id_menu = sharedPreferences.getString("id_menu", "");
                    Log.d("id_menu", id_menu);
                    if(id_menu.equals("")){
                        Toast.makeText(RekomendasiActivity.this, "Harap Pilih Menu Makanan Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                    }else{
                        Intent intent = new Intent(RekomendasiActivity.this, MapActivity2.class);
                        intent.putExtra("myChoice", myChoice);
                        intent.putExtra("budget", String.valueOf(tvMyBudget));
                        intent.putExtra("progressBar", String.valueOf(pbBudget));
                        intent.putExtra("totalbudget", String.valueOf(myChoice.getBudget()));
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

        if(myChoice.getCategoryWisata().size() != 0) {
            currentFragment = TRAVEL;
            loadFragment(new RekomendasiTravelFragment());
        }else if(myChoice.getJumDay() != 0 && myChoice.getJumKamar() != 0){
            currentFragment = HOTEL;
            loadFragment(new RekomendasiHotelFragment());
        }else if(myChoice.getJumPorsi() != 0){
            currentFragment = RESTAURANT;
            loadFragment(new RekomendasiRestoFragment());
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public MyChoice getMyChoice() {
        return myChoice;
    }

    public ProgressBar getPbBudget() {
        return pbBudget;
    }

    public TextView getTvMyBudget() {
        return tvMyBudget;
    }
}
