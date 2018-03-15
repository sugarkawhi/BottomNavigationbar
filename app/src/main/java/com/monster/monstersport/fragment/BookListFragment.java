package com.monster.monstersport.fragment;

import android.support.v7.widget.GridLayoutManager;
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
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    @Override
    protected void loadData() {
        List<BookBean> bookList = new ArrayList<>();
        BookBean book0= new BookBean("http://192.168.1.2:18011/images/covers/20161216121247595.png",
                "绝声",
                "8aada63958aee2640158af5d272100f2");
        BookBean book1 = new BookBean("https://img.xhhread.cn/images/covers/20180126104842802671.jpg",
                "名门盛宠：新妻太美味",
                "8aada63960e363070161086c00df5be4");
        BookBean book2 = new BookBean("https://img.xhhread.cn/images/covers/20180207103125472374.jpg",
                "总裁大叔，带我飞",
                "8aada639611bba5c01616ac7e202752f");
        BookBean book3 = new BookBean("https://img.xhhread.cn/images/covers/20170426163547664.png",
                "宠我，老板大人不要停",
                "8aada6395b89549e015b895793db000b");
        BookBean book4 = new BookBean("https://img.xhhread.cn/images/covers/20170401234510133.png",
                "主播的致命诱惑",
                "8aada6395ab2985c015afaeb0a9c0619");
//        bookList.add(book0);
        bookList.add(book1);
        bookList.add(book2);
        bookList.add(book3);
        bookList.add(book4);
        BookListAdapter adapter = new BookListAdapter(getContext(), bookList);
        mRecyclerView.setAdapter(adapter);
    }


}
