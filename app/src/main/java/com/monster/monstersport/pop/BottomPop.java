package com.monster.monstersport.pop;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.monster.monstersport.R;

/**
 * Created by ZhaoZongyao on 2018/1/3.
 */

public class BottomPop extends PopupWindow {

    private View mContentView;
    private int mHeight;

    public BottomPop(Context context) {
//        mContentView = LayoutInflater.from(context).inflate(R.layout.pop_bottom, null);
        setContentView(mContentView);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mContentView.measure(0, 0);
        mHeight = mContentView.getMeasuredHeight();
        mContentView.setY(mHeight);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        mContentView.animate()
                .translationY(0)
                .setDuration(200)
                .setListener(null)
                .start();
    }

    @Override
    public void dismiss() {
        mContentView.animate()
                .translationY(mHeight)
                .setDuration(200)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        BottomPop.super.dismiss();
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                })
                .start();
//        super.dismiss();
    }
}
