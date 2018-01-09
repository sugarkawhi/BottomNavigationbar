package com.monster.monstersport.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

/**
 * Created by ZhaoZongyao on 2018/1/9.
 */

public class HyTabLayout extends HorizontalScrollView {

    private int mDefaultTabTextSize;
    private int mDefaultTabTextColor;

    public HyTabLayout(Context context) {
        this(context, null);
    }

    public HyTabLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HyTabLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void createDefaultTabView(String title) {
        TextView textView = new TextView(getContext());
        textView.setTextSize(mDefaultTabTextSize);
        textView.setText(title);
    }

    public void setTabs() {

    }
}
