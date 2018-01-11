package com.monster.monstersport.fragment;

import android.view.View;

import com.monster.monstersport.R;
import com.monster.monstersport.base.BaseFragment;

import me.sugarkawhi.mreader.bean.ChapterBean;
import me.sugarkawhi.mreader.view.BaseReaderView;
import me.sugarkawhi.mreader.view.MReaderView;


/**
 * Created by ZhaoZongyao on 2018/1/10.
 */

public class ReaderFragment extends BaseFragment {

    BaseReaderView mReaderView;

    public static ReaderFragment newInstance() {
        return new ReaderFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_reader;
    }

    @Override
    protected void init(View view) {
        mReaderView = view.findViewById(R.id.readerView);
        ChapterBean pre = new ChapterBean();
        pre.setChapterName("第一章 XXX");
        pre.setChapterContent(getString(R.string.text_long_text));
//        mReaderView.setChapters(pre, pre, pre);
//        mReaderView.setElectric(0.6f);
//        mReaderView.setTime("18:00");
    }


    @Override
    protected void loadData() {


    }


}
