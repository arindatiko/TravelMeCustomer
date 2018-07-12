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

import arindatiko.example.com.travelmecustomer.fragment.KamarFragment;
import arindatiko.example.com.travelmecustomer.fragment.PenginapanFragment;
import arindatiko.example.com.travelmecustomer.model.Penginapan;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPenginapanActivity extends AppCompatActivity {
    public static final String PENGINAPAN_ID = "penginapan_id";

    private int penginapanId;
    private Penginapan penginapan;

    private ImageView imgDetail;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_penginapan);

        penginapanId = getIntent().getIntExtra(PENGINAPAN_ID, 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        imgDetail = (ImageView) findViewById(R.id.img_detail);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        loadPenginapanData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PenginapanFragment(), "Deskripsi");
        adapter.addFragment(new KamarFragment(), "Kamar");
        viewPager.setAdapter(adapter);
    }

    public void loadPenginapanData() {
        API.service_post.get_detail_penginapan(penginapanId).enqueue(new Callback<Penginapan>() {
            @Override
            public void onResponse(Call<Penginapan> call, Response<Penginapan> response) {
                penginapan = response.body();

                Glide.with(DetailPenginapanActivity.this)
                        .load(penginapan.getFoto())
                        .into(imgDetail);

                setPenginapan(penginapan);
                setupViewPager(viewPager);
                tabLayout.setupWithViewPager(viewPager);
            }

            @Override
            public void onFailure(Call<Penginapan> call, Throwable t) {
                Log.d("DetailPenginapanActiv", t.getMessage());
            }
        });
    }

    public Penginapan getPenginapan() {
        return penginapan;
    }

    public void setPenginapan(Penginapan penginapan) {
        this.penginapan = penginapan;
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
