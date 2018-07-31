package com.monster.monstersport.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.monster.monstersport.R;
import com.monster.monstersport.view.ZWHistoryView;
import com.monster.monstersport.view.ZWTimeLineView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by ZhaoZongyao on 2018/1/8.
 */

public class ZwHistoryAdapter extends RecyclerView.Adapter<ZwHistoryAdapter.ZwHistoryHolder> {

    private String TAG = "ZwHistoryAdapter";

    private Context mContext;
    private List<List<String>> mList;
    private ZwItemAdapter mZwItemAdapter;

    public ZwHistoryAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<>();
        List<String> list = new ArrayList<>();
        list.add("https://img.xhhread.cn/images/covers/20170406113455220.png");
        list.add("https://img.xhhread.cn/images/covers/20171103182113202.jpg");
        list.add("https://img.xhhread.cn/images/covers/20171116231600840.jpg");
        list.add("https://img.xhhread.cn/images/covers/20171107134554014.jpg");
        list.add("https://img.xhhread.cn/images/covers/20170330215335077.jpg");
        list.add("https://img.xhhread.cn/images/covers/20171221234956665176.jpg");
        list.add("https://img.xhhread.cn/images/covers/20171207174902717447.jpg");
        list.add("https://img.xhhread.cn/images/covers/20170707165223373.jpg");
        list.add("https://img.xhhread.cn/images/covers/20171114170046408.jpg");
        list.add("https://img.xhhread.cn/images/covers/20171221232543194145.jpg");
        list.add("https://img.xhhread.cn/images/covers/20161212170634252.jpg");
        Collections.shuffle(list);
        for (int i = 0; i < 12; i++) {
            mList.add(list.subList(0, 6));
        }
    }

    @Override
    public ZwHistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_zw_history, parent, false);
        return new ZwHistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(final ZwHistoryHolder holder, int position) {
        Log.e(TAG, "onBindViewHolder position = " + position);
        final List<String> list = mList.get(holder.getAdapterPosition());
        mZwItemAdapter = new ZwItemAdapter(list);
        holder.zwHistoryView.setAdapter(mZwItemAdapter);
        holder.mTextView.setText((12 - position) + "月");
        holder.mZWTimeLineView.setFirstNode(position == 0);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ZwHistoryHolder extends RecyclerView.ViewHolder {
        ZWHistoryView zwHistoryView;
        ZWTimeLineView mZWTimeLineView;
        TextView mTextView;

        public ZwHistoryHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.tv_month);
            zwHistoryView = itemView.findViewById(R.id.zwHistoryView);
            mZWTimeLineView = itemView.findViewById(R.id.zwTimeLineView);
        }
    }

    private class ZwItemAdapter extends ZWHistoryView.Adapter {
        List<String> list;

        ZwItemAdapter(List<String> list) {
            this.list = list;
        }

        @Override
        protected View getView(int position) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_zw, null);
            ImageView imageView = view.findViewById(R.id.iv);
            Glide.with(mContext).load(list.get(position)).into(imageView);
            return view;
        }

        @Override
        protected int getCount() {
            return list.size();
        }
    }
}
