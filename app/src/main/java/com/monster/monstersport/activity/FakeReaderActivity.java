package com.monster.monstersport.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.monster.monstersport.R;
import com.monster.monstersport.fragment.ImageFragment;
import com.monster.monstersport.view.PtmViewPager;

public class FakeReaderActivity extends AppCompatActivity {

    PtmViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake_reader);
        mViewPager = findViewById(R.id.viewPager);

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return ImageFragment.newInstance();
            }

            @Override
            public int getCount() {
                return 2;
            }
        });
    }
}
