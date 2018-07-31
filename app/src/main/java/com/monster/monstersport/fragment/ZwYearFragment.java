package com.monster.monstersport.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.monster.monstersport.R;
import com.monster.monstersport.adapter.ZwHistoryAdapter;
import com.monster.monstersport.base.BaseLazyFragment;

/**
 * Created by ZhaoZongyao on 2017/10/16.
 */

public class ZwYearFragment extends BaseLazyFragment {

    private RecyclerView mRecyclerView;
    private ZwHistoryAdapter mZwHistoryAdapter;

    public static ZwYearFragment newInstance() {
        return new ZwYearFragment();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_zwyear;
    }

    @Override
    protected void init(View view) {
        mZwHistoryAdapter = new ZwHistoryAdapter(getContext());
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mZwHistoryAdapter);
    }


    @Override
    protected void loadData() {
    }


}
