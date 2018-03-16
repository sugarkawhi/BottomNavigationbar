package com.monster.monstersport.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.monster.monstersport.R;
import com.monster.monstersport.bean.ChapterBean;
import com.monster.monstersport.dao.bean.BookMarkBean;
import com.monster.monstersport.http.api.ApiConfig;
import com.monster.monstersport.util.TimeFormatUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 阅读器内 目录适配器
 *
 * @author zhzy
 * @date 2017/11/6
 */
public class BookMarkAdapter extends RecyclerView.Adapter<BookMarkAdapter.CatalogItemHolder> {

    private Context mContext;
    private List<BookMarkBean> mData;
    private IBookMarkItemClickListener mItemClickListener;

    public BookMarkAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<BookMarkBean> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void setItemClickListener(IBookMarkItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @Override
    public CatalogItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_item_book_mark, parent, false);
        return new CatalogItemHolder(v);
    }

    @Override
    public void onBindViewHolder(CatalogItemHolder holder, int position) {
        final BookMarkBean bookMark = mData.get(position);

        holder.mTvChapterName.setText(bookMark.getChapterName());
        holder.mTvContent.setText(bookMark.getContent());
        holder.mTvTime.setText(TimeFormatUtils.yyyyMMddHHmmss(bookMark.getTime()));
        holder.mTvProgress.setText(bookMark.getProgress() + "%");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) mItemClickListener.onBookMarkItemClick(bookMark);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    static class CatalogItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_chapterName)
        TextView mTvChapterName;
        @BindView(R.id.tv_content)
        TextView mTvContent;
        @BindView(R.id.tv_time)
        TextView mTvTime;
        @BindView(R.id.tv_progress)
        TextView mTvProgress;

        CatalogItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface IBookMarkItemClickListener {
        void onBookMarkItemClick(BookMarkBean bookMark);
    }

}
