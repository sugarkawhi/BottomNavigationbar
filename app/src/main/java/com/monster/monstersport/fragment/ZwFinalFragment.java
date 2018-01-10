package com.monster.monstersport.fragment;

import android.view.View;
import android.widget.TextView;

import com.monster.monstersport.R;
import com.monster.monstersport.base.BaseLazyFragment;

/**
 * Created by ZhaoZongyao on 2017/10/16.
 */

public class ZwFinalFragment extends BaseLazyFragment {


    public static ZwFinalFragment newInstance() {
        return new ZwFinalFragment();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_text;
    }

    @Override
    protected void init(View view) {
        TextView textView = view.findViewById(R.id.text);
        textView.setText("决赛区");
    }


    @Override
    protected void loadData() {
    }


}
