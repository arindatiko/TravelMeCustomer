package arindatiko.example.com.travelmecustomer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.app.Fragment;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.firebase.auth.FirebaseAuth;


import arindatiko.example.com.travelmecustomer.fragment.AccountFragment;
import arindatiko.example.com.travelmecustomer.fragment.HistoryFragment;
import arindatiko.example.com.travelmecustomer.fragment.HomeFragment;
import arindatiko.example.com.travelmecustomer.fragment.MyTravelFragment;
import arindatiko.example.com.travelmecustomer.util.SessionManager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    SessionManager sessionManager;
    AHBottomNavigation navigation;
    LocationManager cek;
    AlertDialog.Builder alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //sessionManager = new SessionManager(this);
        //Toast.makeText(MainActivity.this, sessionManager.getId(), Toast.LENGTH_SHORT).show();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null){
            if(!FirebaseAuth.getInstance().getCurrentUser().getEmail().isEmpty()){
                startActivity(new Intent(this, LoginActivity.class)
                        .putExtra("phone", FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().isEmpty()));
                finish();
            }else{

            }
        }

        navigation = (AHBottomNavigation)findViewById(R.id.navigation_bottom);

        cek = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(!cek.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            alert = new AlertDialog.Builder(this);
            alert.setMessage("GPS tidak aktif. Aktifkan sekarang?")
                    .setCancelable(false)
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .create();
            alert.show();
        }

        //Toast.makeText(this, sessionManager.getStatus(), Toast.LENGTH_SHORT).show();

       /* if(sessionManager.getStatus() == 0) {
*/
            AHBottomNavigationItem item1 = new AHBottomNavigationItem("Home", R.drawable.ic_ai_home);
            AHBottomNavigationItem item2 = new AHBottomNavigationItem("My Travel", R.drawable.ic_ai_mytravel);
            AHBottomNavigationItem item3 = new AHBottomNavigationItem("LET'S TRAVEL", R.drawable.ic_ai_lets);
            AHBottomNavigationItem item4 = new AHBottomNavigationItem("History Travel", R.drawable.ic_ai_history);
            AHBottomNavigationItem item5 = new AHBottomNavigationItem("Profil Saya", R.drawable.ic_ai_account);

            navigation.addItem(item1);
            navigation.addItem(item2);
            navigation.addItem(item3);
            navigation.addItem(item4);
            navigation.addItem(item5);

            //navigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
            navigation.setDefaultBackgroundColor(Color.parseColor("#FFFFFF"));
            navigation.setAccentColor(Color.parseColor("#36A9E3"));
            navigation.setInactiveColor(Color.parseColor("#95989A"));

            HomeFragment fragment = new HomeFragment();
            if (savedInstanceState == null) {
                fragment.setArguments(getIntent().getExtras());
                getFragmentManager()
                        .beginTransaction()
                        .add(R.id.frame_container, fragment)
                        .commit();
            }

            navigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
                @Override
                public boolean onTabSelected(int position, boolean wasSelected) {
                    Fragment fragment = null;
                    switch (position) {
                        case 0:
                            fragment = new HomeFragment();
                            break;
                        case 1:
                            fragment = new MyTravelFragment();
                            break;
                        case 2:
                            fragment = new HomeFragment();
                            Intent intent = new Intent(MainActivity.this, LetsActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            break;
                        case 3:
                            fragment = new HistoryFragment();
                            break;
                        case 4:
                            fragment = new AccountFragment();
                            break;
                        default:
                            break;
                    }
                    fragment.setArguments(getIntent().getExtras());
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_container, fragment)
                            .commit();
                    return true;
                }
            });
        /*}else{
            AHBottomNavigationItem item1 = new AHBottomNavigationItem("Home", R.drawable.ic_ai_home);
            AHBottomNavigationItem item2 = new AHBottomNavigationItem("My Travel", R.drawable.ic_ai_mytravel);
            AHBottomNavigationItem item3 = new AHBottomNavigationItem("History Travel", R.drawable.ic_ai_history);
            AHBottomNavigationItem item4 = new AHBottomNavigationItem("Profil Saya", R.drawable.ic_ai_account);

            navigation.addItem(item1);
            navigation.addItem(item2);
            navigation.addItem(item3);
            navigation.addItem(item4);

            //navigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
            navigation.setDefaultBackgroundColor(Color.parseColor("#FFFFFF"));
            navigation.setAccentColor(Color.parseColor("#36A9E3"));
            navigation.setInactiveColor(Color.parseColor("#95989A"));

            HomeFragment fragment = new HomeFragment();
            if (savedInstanceState == null) {
                fragment.setArguments(getIntent().getExtras());
                getFragmentManager()
                        .beginTransaction()
                        .add(R.id.frame_container, fragment)
                        .commit();
            }

            navigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
                @Override
                public boolean onTabSelected(int position, boolean wasSelected) {
                    Fragment fragment = null;
                    switch (position) {
                        case 0:
                            fragment = new HomeFragment();
                            break;
                        case 1:
                            fragment = new MyTravelFragment();
                            break;
                        case 2:
                            fragment = new HistoryFragment();
                            break;
                        case 3:
                            fragment = new AccountFragment();
                            break;
                        default:
                            break;
                    }
                    fragment.setArguments(getIntent().getExtras());
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_container, fragment)
                            .commit();
                    return true;
                }
            });
        }*/
    }
}
