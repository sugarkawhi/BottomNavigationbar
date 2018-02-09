package com.monster.monstersport.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.monster.monstersport.R;
import com.monster.monstersport.adapter.BookListAdapter;
import com.monster.monstersport.base.BaseLazyFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import com.monster.monstersport.bean.BookBean;

/**
 * Created by ZhaoZongyao on 2017/10/16.
 */

public class BookListFragment extends BaseLazyFragment {


    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    public static BookListFragment newInstance() {
        return new BookListFragment();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_booklist;
    }

    @Override
    protected void init(View view) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    @Override
    protected void loadData() {
        List<BookBean> bookList = new ArrayList<>();
        BookBean book1 = new BookBean("https://img.xhhread.cn/images/covers/20180126104842802671.jpg",
                "名门盛宠：新妻太美味",
                "8aada63960e363070161086c00df5be4");
        BookBean book2 = new BookBean("https://img.xhhread.cn/images/covers/20180207103125472374.jpg",
                "总裁大叔，带我飞",
                "8aada639611bba5c01616ac7e202752f");
        BookBean book3 = new BookBean("https://img.xhhread.cn/images/covers/20170426163547664.png",
                "宠我，老板大人不要停",
                "8aada6395b89549e015b895793db000b");
        BookBean book4 = new BookBean("https://img.xhhread.cn/images/covers/20171109170005896.jpg",
                "心慌慌：总裁半夜来敲门",
                "8aada6395f4c46b3015f824c24b4684c");
        bookList.add(book1);
        bookList.add(book2);
        bookList.add(book3);
        bookList.add(book4);
        BookListAdapter adapter = new BookListAdapter(getContext(), bookList);
        mRecyclerView.setAdapter(adapter);
    }


}
