package arindatiko.example.com.travelmecustomer;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    public static String currentFragment = TRAVEL;

    public static MyChoice myChoice = new MyChoice();

    private RelativeLayout rvNext;
    private ProgressBar pbBudget;
    private TextView tvMyBudget;
    private TextView tvTotalBudget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekomendasi);

        myChoice = getIntent().getParcelableExtra(MYCHOICE);

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
            public void onClick(View view) {
                switch (currentFragment) {
                    case TRAVEL:
                        loadFragment(new RekomendasiHotelFragment());
                        currentFragment = HOTEL;
                        break;
                    case HOTEL:
                        loadFragment(new RekomendasiRestoFragment());
                        currentFragment = RESTAURANT;
                        break;
                    case RESTAURANT:
                        Intent intent = new Intent(RekomendasiActivity.this, MapActivity.class);
                        intent.putExtra("myChoice", myChoice);
                        intent.putExtra("budget", String.valueOf(tvMyBudget));
                        intent.putExtra("progressBar", String.valueOf(pbBudget));
                        startActivity(intent);
                        finish();
                        break;
                }
            }
        });

        loadFragment(new RekomendasiTravelFragment());
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
