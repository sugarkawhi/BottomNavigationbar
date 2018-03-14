package com.monster.monstersport.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 阅读器状态
 * 1.加载中
 * 2.加载失败
 * Created by ZhaoZongyao on 2018/3/14.
 */

public class ReaderStatusView extends LinearLayout {
    private static final int STATUS_NORMAL = 0;
    private static final int STATUS_LOADING = 1;
    private static final int STATUS_ERROR = 4;

    //当前状态
    private int mCurrentStatus = STATUS_LOADING;
    //点击重试
    private View.OnClickListener mRetryClickListener;

    public ReaderStatusView(Context context) {
        this(context, null);
    }

    public ReaderStatusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReaderStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //set child in center
        setGravity(Gravity.CENTER);
        switch (mCurrentStatus) {
            case STATUS_LOADING:
                loading();
                break;
            case STATUS_NORMAL:
                normal();
                break;
            case STATUS_ERROR:
                error();
                break;
            default:
                normal();
        }
    }

    /**
     * set status loading
     */
    public void loading() {
        if (mCurrentStatus == STATUS_LOADING) return;
        mCurrentStatus = STATUS_LOADING;
        if (getChildCount() > 0) removeAllViews();
        ProgressBar progressBar = new ProgressBar(getContext());
        addView(progressBar);
        setVisibility(VISIBLE);
    }

    /**
     * set status error
     */
    public void error() {
        if (mCurrentStatus == STATUS_ERROR) return;
        mCurrentStatus = STATUS_ERROR;
        if (getChildCount() > 0) removeAllViews();
        final TextView textView = new TextView(getContext());
        textView.setText("加载失败,点击重试");
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        textView.setTextColor(Color.GRAY);
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRetryClickListener != null) mRetryClickListener.onClick(textView);
            }
        });
        addView(textView);
        setVisibility(VISIBLE);
    }


    /**
     * nothing view
     */
    public void normal() {
        mCurrentStatus = STATUS_NORMAL;
        if (getChildCount() > 0) removeAllViews();
        setVisibility(GONE);
    }

    /**
     * net error click retry
     */
    public void setRetryClickListener(View.OnClickListener listener) {
        this.mRetryClickListener = listener;
    }


}
