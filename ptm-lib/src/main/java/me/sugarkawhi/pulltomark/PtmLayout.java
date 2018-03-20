package me.sugarkawhi.pulltomark;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

/**
 * Pull To Mark Layout
 * 下拉添加书签
 * Created by ZhaoZongyao on 2018/3/20.
 */

public class PtmLayout extends ViewGroup {

    //滑动产生距离
    private int mScaledTouchSlop;
    //起始点坐标
    private int mStartX, mStartY;
    //实时触摸点
    private int mTouchX, mTouchY;
    //
    private int mMoveX, mMoveY;
    private boolean isMoveState;

    public PtmLayout(Context context) {
        this(context, null);
    }

    public PtmLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PtmLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 初始化
     */
    private void init() {
        mScaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        mTouchX = x;
        mTouchY = y;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = x;
                mStartY = y;
                mMoveX = 0;
                mMoveY = 0;
                return true;
            case MotionEvent.ACTION_MOVE:
                if (!isMoveState) isMoveState = Math.abs(mTouchY - mStartY) > mScaledTouchSlop;
                if (!isMoveState) return false;
                //第一次构成滑动 分辨方向
                if (mMoveX == 0 && mMoveY == 0) {

                }
                mMoveX = x;
                mMoveY = y;
                return true;
        }
        return super.onTouchEvent(event);
    }
}
