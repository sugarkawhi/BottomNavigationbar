package com.monster.monstersport.view.ptrlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * pull to refresh layout
 * Created by ZhaoZongyao on 2017/10/31.
 */

public class PtrLayout extends ViewGroup {

    private View mHeaderView;

    public PtrLayout(Context context) {
        this(context, null);
    }

    public PtrLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PtrLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
