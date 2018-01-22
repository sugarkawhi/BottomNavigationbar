package com.monster.monstersport.fragment;

import android.util.Log;
import android.view.View;

import com.monster.monstersport.R;
import com.monster.monstersport.base.BaseFragment;
import com.monster.monstersport.view.CbView;


/**
 * Created by ZhaoZongyao on 2018/1/10.
 */

public class CbFragment extends BaseFragment {

    String TAG = "TtsFragment";
    CbView mCbView;

    public static CbFragment newInstance() {
        return new CbFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_cb;
    }

    @Override
    protected void init(View view) {
        mCbView = view.findViewById(R.id.cbView);
    }


    @Override
    protected void loadData() {


    }

}
