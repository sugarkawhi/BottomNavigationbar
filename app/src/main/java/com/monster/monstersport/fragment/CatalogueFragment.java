package com.monster.monstersport.fragment;

import android.view.View;

import com.monster.monstersport.R;
import com.monster.monstersport.base.BaseFragment;


/**
 * 目录
 * Created by ZhaoZongyao on 2018/1/10.
 */

public class CatalogueFragment extends BaseFragment implements View.OnClickListener {

    String TAG = "CatalogueFragment";

    public static CatalogueFragment newInstance() {
        return new CatalogueFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tts;
    }

    @Override
    protected void init(View view) {

    }

    @Override
    protected void loadData() {
    }

    @Override
    public void onClick(View v) {

    }
}
