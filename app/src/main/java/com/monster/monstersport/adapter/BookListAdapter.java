package com.monster.monstersport.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.monster.monstersport.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.monster.monstersport.activity.ReaderActivity;
import com.monster.monstersport.activity.ReaderBdActivity;
import com.monster.monstersport.bean.BookBean;

/**
 * 阅读器内 目录适配器
 *
 * @author zhzy
 * @date 2017/11/6
 */
public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.CatalogItemHolder> {

    private Context mContext;
    private List<BookBean> mData;

    public BookListAdapter(Context context, List<BookBean> data) {
        mContext = context;
        mData = data;
    }


    @Override
    public CatalogItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_item_book, parent, false);
        return new CatalogItemHolder(v);
    }

    @Override
    public void onBindViewHolder(CatalogItemHolder holder, int position) {
        final BookBean book = mData.get(position);

        Glide.with(mContext).load(book.getCover()).into(holder.mImageView);
//        holder.mTextView.setText(book.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ReaderBdActivity.class);
                intent.putExtra("storyid", book.getStoryid());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    static class CatalogItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv)
        ImageView mImageView;
//        @BindView(R.id.text)
//        TextView mTextView;

        CatalogItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
