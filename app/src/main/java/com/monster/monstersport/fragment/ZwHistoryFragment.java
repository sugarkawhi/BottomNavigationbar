package com.monster.monstersport.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.monster.monstersport.R;
import com.monster.monstersport.adapter.ZwHistoryAdapter;
import com.monster.monstersport.base.BaseLazyFragment;
import com.monster.monstersport.view.HyTabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhaoZongyao on 2017/10/16.
 */

public class ZwHistoryFragment extends BaseLazyFragment {

    private List<String> titles;
    private HyTabLayout mTabLayout;
    private ViewPager mViewPager;


    public static ZwHistoryFragment newInstance() {
        return new ZwHistoryFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_zwhistory;
    }

    @Override
    protected void init(View view) {
        mTabLayout = view.findViewById(R.id.hyTabLayout);
        mViewPager = view.findViewById(R.id.viewPager);
        titles = new ArrayList<>();
        titles.add("2018");
        titles.add("2017");
        titles.add("2016");
        mTabLayout.setTabs(titles);
        final List<Fragment> fragments = new ArrayList<>();
        fragments.add(ZwYearFragment.newInstance());
        fragments.add(ZwYearFragment.newInstance());
        fragments.add(ZwYearFragment.newInstance());
        mTabLayout.setViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
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
