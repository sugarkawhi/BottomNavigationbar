package com.monster.monstersport.dialog;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

/**
 * 底部弹出Dialog
 * Created by ZhaoZongyao on 2018/1/30.
 */

public abstract class BottomPopDialog extends Dialog {

    private Interpolator mInterceptor;
    private View mContentView;
    private int mContentHeight;

    public BottomPopDialog(Context context) {
        super(context);
        mInterceptor = new LinearInterpolator();
        init();
    }

    private void init() {
        Window window = getWindow();
        if (window != null) {
            window.requestFeature(Window.FEATURE_NO_TITLE);
            mContentView = getLayoutInflater().inflate(getLayoutId(), null);
            mContentView.measure(0, 0);
            mContentHeight = mContentView.getMeasuredHeight();
            setContentView(mContentView);
            mContentView.setTranslationY(mContentHeight);
            window.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.windowAnimations = -1;
            lp.gravity = Gravity.BOTTOM;
            lp.dimAmount = 0f;
            window.setAttributes(lp);
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
    }


    public abstract int getLayoutId();

    public View getContentView() {
        return mContentView;
    }

    @Override
    public void show() {
        super.show();
        translateContentView(0, null);
    }

    @Override
    public void dismiss() {
        translateContentView(mContentHeight, mAnimatorListener);
    }

    private void translateContentView(int y, Animator.AnimatorListener listener) {
        mContentView.animate()
                .setDuration(200)
                .translationY(y)
                .setInterpolator(mInterceptor)
                .setListener(listener)
                .start();
    }

    private Animator.AnimatorListener mAnimatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            BottomPopDialog.super.dismiss();
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };
}
