package com.monster.monstersport.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.monster.monstersport.R;
import com.monster.monstersport.base.BaseLazyFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhaoZongyao on 2017/10/16.
 */

public class ZwFragment extends BaseLazyFragment {

    public static ZwFragment newInstance() {
        return new ZwFragment();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_zw;
    }

    @Override
    protected void init(View view) {
        ViewPager viewPager = view.findViewById(R.id.viewPager);
        final List<Fragment> fragments = new ArrayList<>();
        fragments.add(ZwPreFragment.newInstance());
        fragments.add(ZwFinalFragment.newInstance());
        fragments.add(ZwHistoryFragment.newInstance());
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
    }


    @Override
    protected void loadData() {
    }


}
