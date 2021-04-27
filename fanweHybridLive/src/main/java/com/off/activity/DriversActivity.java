package com.off.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.live.R;
import com.off.fragment.CarsMallFragment;
import com.off.fragment.CarsMyFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2018/12/27 0027
 * 座驾
 */
public class DriversActivity extends BaseTitleActivity {

    private TabLayout tabCars;
    private ViewPager vpCars;
    private String[] titles = new String[]{"座驾商城", "我的座驾"};
    private List<Fragment> tabFragments;
    private ContentPagerAdapter contentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drivers);

        tabCars = findViewById(R.id.tabCars);
        vpCars = findViewById(R.id.vpCars);
        tabFragments = new ArrayList<>();

        tabFragments.add(new CarsMallFragment());
        tabFragments.add(new CarsMyFragment());

        contentAdapter = new ContentPagerAdapter(getSupportFragmentManager());
        vpCars.setAdapter(contentAdapter);

        initTitle();
        initTab();
    }

    private void initTitle() {
        mTitle.setMiddleTextTop("座驾商城");
    }

    private void initTab() {
        tabCars.setupWithViewPager(vpCars);
    }


    class ContentPagerAdapter extends FragmentPagerAdapter {

        public ContentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return tabFragments.get(position);
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

}