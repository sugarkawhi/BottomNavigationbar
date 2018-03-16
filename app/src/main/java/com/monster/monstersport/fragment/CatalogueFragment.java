package com.monster.monstersport.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.monster.monstersport.R;
import com.monster.monstersport.activity.ReaderActivity;
import com.monster.monstersport.adapter.CatalogAdapter;
import com.monster.monstersport.base.BaseFragment;
import com.monster.monstersport.bean.ChapterBean;
import com.monster.monstersport.bean.ChapterListBean;
import com.monster.monstersport.http.BaseHttpResult;
import com.monster.monstersport.http.HttpUtils;
import com.monster.monstersport.http.RxUtils;
import com.monster.monstersport.http.observer.DefaultObserver;

import java.util.List;

import butterknife.BindView;


/**
 * 目录
 * Created by ZhaoZongyao on 2018/1/10.
 */

public class CatalogueFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private CatalogAdapter mCatalogAdapter;
    private CatalogAdapter.IChapterItemClickListener mChapterItemClickListener;

    public static CatalogueFragment newInstance() {
        return new CatalogueFragment();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CatalogAdapter.IChapterItemClickListener) {
            mChapterItemClickListener = (CatalogAdapter.IChapterItemClickListener) context;
        } else {
            try {
                throw new Exception("未实现IChapterItemClickListener");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_reader;
    }

    @Override
    protected void init(View view) {
        mCatalogAdapter = new CatalogAdapter(getContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mCatalogAdapter);
        mCatalogAdapter.setItemClickListener(new CatalogAdapter.IChapterItemClickListener() {
            @Override
            public void onChapterItemClick(ChapterBean chapter) {
                if (mChapterItemClickListener != null)
                    mChapterItemClickListener.onChapterItemClick(chapter);
            }
        });
    }


    @Override
    protected void loadData() {
        Bundle bundle = getArguments();
        if (bundle == null) return;
        String id = bundle.getString(ReaderActivity.PARAM_STORY_ID);
        HttpUtils.getApiInstance()
                .searchChapterListVO(id)
                .compose(RxUtils.<BaseHttpResult<ChapterListBean>>defaultSchedulers())
                .subscribe(new DefaultObserver<ChapterListBean>() {
                    @Override
                    protected void onSuccess(ChapterListBean chapterListBean) {
                        List<ChapterBean> list = chapterListBean.getDatas();
                        mCatalogAdapter.setData(list);
                    }
                });
    }


}
