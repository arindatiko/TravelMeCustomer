package arindatiko.example.com.travelmecustomer;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import arindatiko.example.com.travelmecustomer.fragment.KulinerFragment;
import arindatiko.example.com.travelmecustomer.fragment.MenuFragment;
import arindatiko.example.com.travelmecustomer.model.Kuliner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailKulinerActivity extends AppCompatActivity {

    public static final String KULINER_ID = "kuliner_id";

    private int kulinerId;
    private Kuliner kuliner;

    private ImageView imgDetail;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kuliner);

        kulinerId = getIntent().getIntExtra(KULINER_ID, 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        imgDetail = (ImageView) findViewById(R.id.img_detail);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        loadKulinerData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new KulinerFragment(), "Deskripsi");
        adapter.addFragment(new MenuFragment(), "Menu");
        viewPager.setAdapter(adapter);
    }

    public void loadKulinerData() {
        API.service_post.get_detail_kuliner(kulinerId).enqueue(new Callback<Kuliner>() {
            @Override
            public void onResponse(Call<Kuliner> call, Response<Kuliner> response) {
                kuliner = response.body();

                Glide.with(DetailKulinerActivity.this)
                        .load(kuliner.getFoto())
                        .into(imgDetail);

                setKuliner(kuliner);
                setupViewPager(viewPager);
                tabLayout.setupWithViewPager(viewPager);
            }

            @Override
            public void onFailure(Call<Kuliner> call, Throwable t) {
                Log.d("DetailKulinerActivity", t.getMessage());
            }
        });
    }

    public Kuliner getKuliner() {
        return kuliner;
    }

    public void setKuliner(Kuliner kuliner) {
        this.kuliner = kuliner;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
