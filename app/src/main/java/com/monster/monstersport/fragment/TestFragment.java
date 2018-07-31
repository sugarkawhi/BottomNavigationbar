package com.monster.monstersport.fragment;

import android.view.View;

import com.monster.monstersport.R;
import com.monster.monstersport.base.BaseLazyFragment;

/**
 * test
 * Created by ZhaoZongyao on 2017/10/16.
 */

public class TestFragment extends BaseLazyFragment {
    private String TAG = "TestFragment";


    public static TestFragment newInstance() {
        return new TestFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test;
    }

    @Override
    protected void init(View view) {
    }


    @Override
    protected void loadData() {

    }


}
