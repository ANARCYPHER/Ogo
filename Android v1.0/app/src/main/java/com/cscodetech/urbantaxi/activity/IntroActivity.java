package com.cscodetech.urbantaxi.activity;

import static com.cscodetech.urbantaxi.utility.Utility.rtl;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.cscodetech.urbantaxi.R;
import com.cscodetech.urbantaxi.fregment.Info1Fragment;
import com.cscodetech.urbantaxi.fregment.Info2Fragment;
import com.cscodetech.urbantaxi.fregment.Info3Fragment;
import com.cscodetech.urbantaxi.utility.AutoScrollViewPager;
import com.cscodetech.urbantaxi.utility.SessionManager;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class IntroActivity extends BaseActivity {

    @BindView(R.id.flexibleIndicator)
    DotsIndicator flexibleIndicator;
    int selectPage = 0;
    SessionManager sessionManager;
    public static AutoScrollViewPager vpPager;
    MyPagerAdapter adapterViewPager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);
        vpPager = findViewById(R.id.vpPager);
        sessionManager = new SessionManager(IntroActivity.this);
        requestPermissions(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
        sessionManager.setBooleanData(rtl,false);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        if (sessionManager.getBooleanData(SessionManager.login)) {
            vpPager.stopAutoScroll();

            startActivity(new Intent(IntroActivity.this, HomeActivity.class));
            finish();
        }
        vpPager.setAdapter(adapterViewPager);
        vpPager.startAutoScroll();
        vpPager.setInterval(2000);
        vpPager.setCycle(true);
        vpPager.setStopScrollWhenTouch(true);
        DotsIndicator extensiblePageIndicator = (DotsIndicator) findViewById(R.id.flexibleIndicator);
        extensiblePageIndicator.setViewPager(vpPager);

        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("data", "jsadlj");
            }

            @Override
            public void onPageSelected(int position) {
                selectPage = position;


            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.e("sjlkj", "sjahdal");
            }
        });
    }

    @OnClick({R.id.btn_login, R.id.btn_signup})
    public void onClick(View view) {
        if (view.getId() == R.id.btn_login) {
            startActivity(new Intent(this, LoginActivity.class));
            vpPager.stopAutoScroll();
        }
        if (view.getId() == R.id.btn_signup) {
            startActivity(new Intent(this, SignUpActivity.class));
            vpPager.stopAutoScroll();

        }
    }


    public class MyPagerAdapter extends FragmentPagerAdapter {
        private int numItems = 3;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return numItems;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return Info1Fragment.newInstance();
                case 1:
                    return Info2Fragment.newInstance();
                case 2:
                    return Info3Fragment.newInstance();
                default:
                    return null;
            }

        }

        @Override
        public CharSequence getPageTitle(int position) {
            Log.e("page", "" + position);
            return "Page " + position;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            return fragment;
        }

    }
}
