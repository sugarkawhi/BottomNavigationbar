package com.monster.monstersport.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.monster.monstersport.R;
import com.monster.monstersport.fragment.DaoQueryFragment;
import com.monster.monstersport.fragment.JikeViewFragment;
import com.monster.monstersport.view.SViewPager;

import java.util.ArrayList;
import java.util.List;

import me.sugarkawhi.bottomnavigationbar.BottomNavigationBar;
import me.sugarkawhi.bottomnavigationbar.BottomNavigationEntity;


public class MainActivity extends AppCompatActivity {

//    private SViewPager mSViewPager;

    private List<BottomNavigationEntity> mEntities;
//    private List<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mSViewPager = findViewById(R.id.viewPager);
//        mSViewPager.setCanScroll(false);
//        mSViewPager.setOffscreenPageLimit(4);
        BottomNavigationBar bottomNavigationBar = findViewById(R.id.bottomNavigationBar);

        mEntities = new ArrayList<>();
//        mFragments = new ArrayList<>();
//        mFragments.add(JikeViewFragment.newInstance());
//        mFragments.add(JikeViewFragment.newInstance());
//        mFragments.add(JikeViewFragment.newInstance());
//        mFragments.add(DaoQueryFragment.newInstance());

        mEntities.add(new BottomNavigationEntity(
                R.drawable.ic_tab_album_default,
                R.drawable.ic_tab_album_selected));
        mEntities.add(new BottomNavigationEntity(
                R.drawable.ic_tab_img_default,
                R.drawable.ic_tab_img_selected));
        mEntities.add(new BottomNavigationEntity(
                R.drawable.ic_tab_news_default,
                R.drawable.ic_tab_news_selected));
        mEntities.add(new BottomNavigationEntity(
                R.drawable.ic_tab_avatar_default,
                R.drawable.ic_tab_avatar_selected, 10));
        bottomNavigationBar.setEntities(mEntities);
        bottomNavigationBar.setBnbItemSelectListener(new BottomNavigationBar.IBnbItemSelectListener() {

            @Override
            public void onBnbItemSelect(int position) {
//                mSViewPager.setCurrentItem(position);
            }
        });

        bottomNavigationBar.setBnbItemDoubleClickListener(new BottomNavigationBar.IBnbItemDoubleClickListener() {
            @Override
            public void onBnbItemDoubleClick(int position) {
            }
        });

        bottomNavigationBar.setCurrentPosition(0);

//        mSViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
//            @Override
//            public Fragment getItem(int position) {
//                return mFragments.get(position);
//            }
//
//            @Override
//            public int getCount() {
//                return mFragments.size();
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
