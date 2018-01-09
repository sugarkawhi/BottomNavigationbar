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
        List<String> list1 = new ArrayList<>();
        list1.add("http://bookbk.img.ireader.com/group6/M00/16/18/CmQUOVaxXIiEJY9AAAAAAFHYwBQ342446061.jpg?v=IYhDlqxY");
        List<String> list2 = new ArrayList<>();
        list2.add("https://img.xhhread.cn/images/covers/20171031113315649.jpg");
        list2.add("https://img.xhhread.cn/images/covers/20170321195717153.png");
        List<String> list3 = new ArrayList<>();
        list3.add("https://img.xhhread.cn/images/covers/20170330215335077.jpg");
        list3.add("https://img.xhhread.cn/images/covers/20171116231600840.jpg");
        list3.add("https://img.xhhread.cn/images/covers/20171031113315649.jpg");
        List<String> list4 = new ArrayList<>();
        list4.add("https://img.xhhread.cn/images/covers/20170406113455220.png");
        list4.add("http://bookbk.img.ireader.com/group6/M00/D2/E4/CmQUN1jdGHuEQpZIAAAAAH6skjU284582432.jpg?v=yGsjoK9L");
        list4.add("http://bookbk.img.ireader.com/group6/M00/14/DB/CmQUOVanMEiEJ7PvAAAAAJWt4y4554842635.jpg?v=CauWCGkY");
        list4.add("https://img.xhhread.cn/images/covers/20171103182113202.jpg");
        List<String> list5 = new ArrayList<>();
        list5.add("https://img.xhhread.cn/images/covers/20170406113455220.png");
        list5.add("https://img.xhhread.cn/images/covers/20171103182113202.jpg");
        list5.add("http://book.img.ireader.com/group6/M00/6C/A9/CmQUN1avNDSEQkbGAAAAAK4hmko417445595.jpg?v=49pVi6zE");
        list5.add("http://book.img.ireader.com/group6/M00/6C/A9/CmQUN1avNDSEQkbGAAAAAK4hmko417445595.jpg?v=49pVi6zE");
        list5.add("http://bookbk.img.ireader.com/group6/M00/6B/16/CmQUN1agju6EGM01AAAAALskY8Y213298834.jpg?v=eyExaQKZ");
        List<String> list6 = new ArrayList<>();
        list6.add("https://img.xhhread.cn/images/covers/20170406113455220.png");
        list6.add("http://bookbk.img.ireader.com/group6/M00/D2/E4/CmQUN1jdGHuEQpZIAAAAAH6skjU284582432.jpg?v=yGsjoK9L");
        list6.add("http://bookbk.img.ireader.com/group6/M00/14/DB/CmQUOVanMEiEJ7PvAAAAAJWt4y4554842635.jpg?v=CauWCGkY");
        list6.add("https://img.xhhread.cn/images/covers/20171103182113202.jpg");
        list6.add("http://bookbk.img.ireader.com/group6/M00/6B/16/CmQUN1agju6EGM01AAAAALskY8Y213298834.jpg?v=eyExaQKZ");
        list6.add("https://img.xhhread.cn/images/covers/20171116231600840.jpg");
        List<String> list7 = new ArrayList<>();
        list7.add("http://bookbk.img.ireader.com/group6/M00/16/18/CmQUOVaxXIiEJY9AAAAAAFHYwBQ342446061.jpg?v=IYhDlqxY");
        list7.add("http://bookbk.img.ireader.com/group6/M00/D2/E4/CmQUN1jdGHuEQpZIAAAAAH6skjU284582432.jpg?v=yGsjoK9L");
        list7.add("http://bookbk.img.ireader.com/group6/M00/14/DB/CmQUOVanMEiEJ7PvAAAAAJWt4y4554842635.jpg?v=CauWCGkY");
        list7.add("http://bookbk.img.ireader.com/group6/M00/D2/E4/CmQUN1jdGHuEQpZIAAAAAH6skjU284582432.jpg?v=yGsjoK9L");
        list7.add("http://bookbk.img.ireader.com/group6/M00/14/DB/CmQUOVanMEiEJ7PvAAAAAJWt4y4554842635.jpg?v=CauWCGkY");
        list7.add("https://img.xhhread.cn/images/covers/20171107134554014.jpg");
        list7.add("https://img.xhhread.cn/images/covers/20171128181718203164.jpg");
        List<String> list8 = new ArrayList<>();
        list8.add("http://book.img.ireader.com/group6/M00/6C/A9/CmQUN1avNDSEQkbGAAAAAK4hmko417445595.jpg?v=49pVi6zE");
        list8.add("http://book.img.ireader.com/group6/M00/63/B9/CmQUNlZubLGEA8SHAAAAAKe3INY156227192.jpg?v=fTn49eo8");
        list8.add("http://bookbk.img.ireader.com/group6/M00/16/18/CmQUOVaxXIiEJY9AAAAAAFHYwBQ342446061.jpg?v=IYhDlqxY");
        list8.add("http://bookbk.img.ireader.com/group6/M00/D2/E4/CmQUN1jdGHuEQpZIAAAAAH6skjU284582432.jpg?v=yGsjoK9L");
        list8.add("http://bookbk.img.ireader.com/group6/M00/D2/E4/CmQUN1jdGHuEQpZIAAAAAH6skjU284582432.jpg?v=yGsjoK9L");
        list8.add("http://bookbk.img.ireader.com/group6/M00/14/DB/CmQUOVanMEiEJ7PvAAAAAJWt4y4554842635.jpg?v=CauWCGkY");
        list8.add("https://img.xhhread.cn/images/covers/20171107134554014.jpg");
        list8.add("https://img.xhhread.cn/images/covers/20171128181718203164.jpg");
        List<String> list9 = new ArrayList<>();
        list9.add("http://book.img.ireader.com/group6/M00/6C/A9/CmQUN1avNDSEQkbGAAAAAK4hmko417445595.jpg?v=49pVi6zE");
        list9.add("http://book.img.ireader.com/group6/M00/63/B9/CmQUNlZubLGEA8SHAAAAAKe3INY156227192.jpg?v=fTn49eo8");
        list9.add("http://bookbk.img.ireader.com/group6/M00/16/18/CmQUOVaxXIiEJY9AAAAAAFHYwBQ342446061.jpg?v=IYhDlqxY");
        list9.add("http://bookbk.img.ireader.com/group6/M00/D2/E4/CmQUN1jdGHuEQpZIAAAAAH6skjU284582432.jpg?v=yGsjoK9L");
        list9.add("http://bookbk.img.ireader.com/group6/M00/D2/E4/CmQUN1jdGHuEQpZIAAAAAH6skjU284582432.jpg?v=yGsjoK9L");
        list9.add("http://bookbk.img.ireader.com/group6/M00/14/DB/CmQUOVanMEiEJ7PvAAAAAJWt4y4554842635.jpg?v=CauWCGkY");
        list9.add("https://img.xhhread.cn/images/covers/20171107134554014.jpg");
        list9.add("https://img.xhhread.cn/images/covers/20171128181718203164.jpg");
        list9.add("http://bookbk.img.ireader.com/group6/M00/14/DB/CmQUOVanMEiEJ7PvAAAAAJWt4y4554842635.jpg?v=CauWCGkY");
        mList.add(list1);
        mList.add(list2);
        mList.add(list3);
        mList.add(list4);
        mList.add(list5);
        mList.add(list6);
        mList.add(list7);
        mList.add(list8);
        mList.add(list9);
        mList.add(list6);
        mList.add(list6);
        mList.add(list6);
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
