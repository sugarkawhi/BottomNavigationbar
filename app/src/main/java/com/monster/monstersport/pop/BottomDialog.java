package com.monster.monstersport.pop;

import android.animation.Animator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.monster.monstersport.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.view.WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;

/**
 * Created by ZhaoZongyao on 2018/1/3.
 */

public class BottomDialog {

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowLayoutParams;
    private View mContentView;
    private int mHeight;

    public BottomDialog(Activity activity) {
        mContentView = LayoutInflater.from(activity).inflate(R.layout.pop_bottom, null);
        mContentView.measure(0, 0);
        mHeight = mContentView.getMeasuredHeight();
        mContentView.setTranslationY(mHeight);
        initWindowManager(activity);
    }

    private void initWindowManager(Activity activity) {
        mWindowManager = activity.getWindowManager();
        mWindowLayoutParams = new WindowManager.LayoutParams();
        mWindowLayoutParams.width = MATCH_PARENT;
        mWindowLayoutParams.height = mHeight;
        mWindowLayoutParams.flags =
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mWindowLayoutParams.gravity = Gravity.BOTTOM;
//        mWindowLayoutParams.type = WindowManager.LayoutParams.TYPE_BASE_APPLICATION;
        mWindowLayoutParams.dimAmount = 0f;
        mWindowManager.addView(mContentView, mWindowLayoutParams);
        mContentView.setTranslationY(mHeight);
    }

    public void show() {
        mContentView.animate()
                .translationY(0)
                .setDuration(500)
                .setListener(null)
                .start();
    }

    public void dismiss() {
        mContentView.animate()
                .translationY(mHeight)
                .setDuration(500)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                })
                .start();
    }
}
