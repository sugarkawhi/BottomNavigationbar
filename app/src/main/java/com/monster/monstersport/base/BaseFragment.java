package com.monster.monstersport.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.monster.monstersport.R;

/**
 * Created by ZhaoZongyao on 2017/12/22.
 */

public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        init(view);
        loadData();
        return view;
    }

    protected abstract int getLayoutId();

    protected  abstract void init(View view);

    protected  abstract void loadData();

}
