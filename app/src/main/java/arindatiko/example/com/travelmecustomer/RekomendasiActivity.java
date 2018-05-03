package arindatiko.example.com.travelmecustomer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

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

    public MyChoice getMyChoice() {
        return myChoice;
    }

    public static MyChoice myChoice = new MyChoice();
    private RelativeLayout rvNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekomendasi);

        myChoice = getIntent().getParcelableExtra(MYCHOICE);

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
}
