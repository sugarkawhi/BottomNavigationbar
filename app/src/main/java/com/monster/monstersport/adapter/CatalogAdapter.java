package com.monster.monstersport.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.monster.monstersport.R;
import com.monster.monstersport.bean.ChapterBean;
import com.monster.monstersport.http.api.ApiConfig;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 阅读器内 目录适配器
 *
 * @author zhzy
 * @date 2017/11/6
 */
public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.CatalogItemHolder> {

    private Context mContext;
    private List<ChapterBean> mData;
    private IChapterItemClickListener mItemClickListener;

    public CatalogAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<ChapterBean> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void setItemClickListener(IChapterItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @Override
    public CatalogItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_item_book_chapter, parent, false);
        return new CatalogItemHolder(v);
    }

    @Override
    public void onBindViewHolder(CatalogItemHolder holder, int position) {
        final ChapterBean chapter = mData.get(position);
        if (chapter.getIsfree() == ApiConfig.YES_NO.Y) {
            holder.mTvChapter.setTextColor(ContextCompat.getColor(mContext, R.color.letter_color1));
        } else {
            holder.mTvChapter.setTextColor(ContextCompat.getColor(mContext, R.color.letter_color4));
        }
        holder.mTvChapter.setText(chapter.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) mItemClickListener.onChapterItemClick(chapter);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    static class CatalogItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_chapter)
        TextView mTvChapter;

        CatalogItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface IChapterItemClickListener {

        void onChapterItemClick(ChapterBean chapter);
    }

}
