package com.monster.monstersport.dialog;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.monster.monstersport.R;

import me.sugarkawhi.mreader.utils.ScreenUtils;

/**
 * 底部弹出Dialog
 * Created by ZhaoZongyao on 2018/1/30.
 */

public abstract class BottomSheetDialog extends Dialog {
    private static final int DURATION = 200;
    private View mContentView;
    private int mContentHeight;

    PopupWindow mPopupWindow;

    public BottomSheetDialog(Context context) {
        super(context);

    }

    private void createView() {
        FrameLayout frameLayout = new FrameLayout(getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        frameLayout.setLayoutParams(params);
        inflateContentView();
        FrameLayout.LayoutParams contentParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        contentParams.gravity = Gravity.BOTTOM;
        frameLayout.addView(mContentView);

    }

    private void inflateContentView() {

    }

    public void show() {
    }

    public abstract int getLayoutId();

}
