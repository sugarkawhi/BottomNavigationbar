package com.monster.monstersport.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.monster.monstersport.R;
import com.monster.monstersport.activity.ReaderBdActivity;
import com.monster.monstersport.adapter.BookMarkAdapter;
import com.monster.monstersport.base.BaseFragment;
import com.monster.monstersport.dao.bean.BookMarkBean;
import com.monster.monstersport.http.RxUtils;
import com.monster.monstersport.persistence.HyReaderPersistence;

import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;


/**
 * 目录
 * Created by ZhaoZongyao on 2018/1/10.
 */

public class BookMarkFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private BookMarkAdapter mBookMarkAdapter;

    public static BookMarkFragment newInstance() {
        return new BookMarkFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_reader;
    }

    @Override
    protected void init(View view) {
        mBookMarkAdapter = new BookMarkAdapter(getContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mBookMarkAdapter);
    }


    @Override
    protected void loadData() {
        Bundle bundle = getArguments();
        if (bundle == null) return;
        final String id = bundle.getString(ReaderBdActivity.PARAM_STORY_ID);
        Observable.create(new ObservableOnSubscribe<List<BookMarkBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<BookMarkBean>> e) throws Exception {
                e.onNext(HyReaderPersistence.queryBookMarkList(id));
                e.onComplete();
            }
        })
                .compose(RxUtils.<List<BookMarkBean>>defaultSchedulers())
                .compose(this.<List<BookMarkBean>>bindToLifecycle())
                .subscribe(new io.reactivex.observers.DefaultObserver<List<BookMarkBean>>() {
                    @Override
                    public void onNext(List<BookMarkBean> bookMarkBeans) {
                        mBookMarkAdapter.setData(bookMarkBeans);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}
