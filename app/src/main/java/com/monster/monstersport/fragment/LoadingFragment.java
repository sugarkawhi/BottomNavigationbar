package com.monster.monstersport.fragment;

import android.view.View;

import com.monster.monstersport.R;
import com.monster.monstersport.base.BaseLazyFragment;

import me.sugarkawhi.hyloadingview.SimpleHyLoadingView;

/**
 * Created by ZhaoZongyao on 2017/10/16.
 */

public class LoadingFragment extends BaseLazyFragment {

    private SimpleHyLoadingView mLoadingView;

    public static LoadingFragment newInstance() {
        return new LoadingFragment();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_loading;
    }

    @Override
    protected void init(View view) {
        mLoadingView = view.findViewById(R.id.loadingView);
        view.findViewById(R.id.btn_loading)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mLoadingView.loading();
                    }
                });
        view.findViewById(R.id.btn_success)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mLoadingView.success();
                    }
                });
        view.findViewById(R.id.btn_error)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mLoadingView.error();
                    }
                });
    }


    @Override
    protected void loadData() {
    }


}
