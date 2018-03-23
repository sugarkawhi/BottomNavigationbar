package com.monster.monstersport.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import me.sugarkawhi.mreader.utils.L;

public class PtmViewPager extends ViewPager {

    String TAG = "PtmViewPager";

    public PtmViewPager(@NonNull Context context) {
        this(context, null);
    }

    public PtmViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "dispatchTouchEvent: ACTION_DOWN");
                return super.dispatchTouchEvent(ev);
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "dispatchTouchEvent: ACTION_MOVE");
                return super.dispatchTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }
}
