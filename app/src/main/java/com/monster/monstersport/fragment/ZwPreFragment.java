package com.monster.monstersport.fragment;

import android.view.View;
import android.widget.TextView;

import com.monster.monstersport.R;
import com.monster.monstersport.base.BaseLazyFragment;

/**
 * Created by ZhaoZongyao on 2017/10/16.
 */

public class ZwPreFragment extends BaseLazyFragment {


    public static ZwPreFragment newInstance() {
        return new ZwPreFragment();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_text;
    }

    @Override
    protected void init(View view) {
        TextView textView = view.findViewById(R.id.text);
        textView.setText("预选区");
    }


    @Override
    protected void loadData() {
    }


}
