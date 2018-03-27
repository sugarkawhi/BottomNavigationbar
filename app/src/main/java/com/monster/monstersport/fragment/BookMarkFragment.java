package com.monster.monstersport.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.monster.monstersport.R;
import com.monster.monstersport.activity.ReaderActivity;
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

    private String mStoryId;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private BookMarkAdapter mBookMarkAdapter;
    private BookMarkAdapter.IBookMarkItemClickListener mBookMarkItemClickListener;

    public static BookMarkFragment newInstance() {
        return new BookMarkFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BookMarkAdapter.IBookMarkItemClickListener) {
            mBookMarkItemClickListener = (BookMarkAdapter.IBookMarkItemClickListener) context;
        } else {
            try {
                throw new Exception("未实现IBookMarkItemClickListener");
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
        mBookMarkAdapter = new BookMarkAdapter(getContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mBookMarkAdapter);
        mBookMarkAdapter.setItemClickListener(new BookMarkAdapter.IBookMarkItemClickListener() {
            @Override
            public void onBookMarkItemClick(BookMarkBean bookMark) {
                if (mBookMarkItemClickListener != null)
                    mBookMarkItemClickListener.onBookMarkItemClick(bookMark);
            }
        });
        Bundle bundle = getArguments();
        if (bundle == null) return;
        mStoryId = bundle.getString(ReaderActivity.PARAM_STORY_ID);
    }


    @Override
    protected void loadData() {
        if (TextUtils.isEmpty(mStoryId)) return;
        Observable.create(new ObservableOnSubscribe<List<BookMarkBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<BookMarkBean>> e) throws Exception {
                e.onNext(HyReaderPersistence.queryBookMarkList(mStoryId));
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
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 刷新书签页面
     */
    public void refresh() {
        loadData();
    }
}
