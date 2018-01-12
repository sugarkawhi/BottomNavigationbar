package com.monster.monstersport.fragment;

import android.view.View;

import com.monster.monstersport.R;
import com.monster.monstersport.base.BaseFragment;

import me.sugarkawhi.mreader.bean.ChapterBean;
import me.sugarkawhi.mreader.config.Config;
import me.sugarkawhi.mreader.view.BaseReaderView;
import me.sugarkawhi.mreader.view.MReaderView;


/**
 * Created by ZhaoZongyao on 2018/1/10.
 */

public class ReaderFragment extends BaseFragment {


    public static ReaderFragment newInstance() {
        return new ReaderFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_reader;
    }

    @Override
    protected void init(View view) {
    }


    @Override
    protected void loadData() {


    }


}
