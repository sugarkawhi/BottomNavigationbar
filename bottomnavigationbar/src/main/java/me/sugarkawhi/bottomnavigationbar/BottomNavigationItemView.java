package me.sugarkawhi.bottomnavigationbar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by ZhaoZongyao on 2017/8/25.
 */

public class BottomNavigationItemView extends LinearLayout {
    private int mSelectedIcon;
    private int mUnSelectedIcon;
    private int mTextSize;
    private String mText;
    private int mTextSelectedColor;
    private int mTextUnSelectedColor;

    private ImageView mItemIcon;
    private TextView mItemText;

    private static final float SCALE_MAX = 1.1f;

    public BottomNavigationItemView(Context context) {
        this(context, null);
    }

    public BottomNavigationItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomNavigationItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        LayoutInflater.from(getContext()).inflate(R.layout.bottom_navigation_item, this, true);
        mItemIcon = findViewById(R.id.navigation_item_icon);
        mItemText = findViewById(R.id.navigation_item_text);
        defaultState();
    }

    public void setSelectedIcon(int selectedIcon) {
        this.mSelectedIcon = selectedIcon;
    }

    public void setUnSelectedIcon(int unSelectedIcon) {
        this.mUnSelectedIcon = unSelectedIcon;
    }

    public void setTextSize(int textSize) {
        this.mTextSize = textSize;
    }

    public void setTextSelectedColor(int textSelectedColor) {
        this.mTextSelectedColor = textSelectedColor;
    }

    public void setTextUnSelectedColor(int textUnSelectedColor) {
        this.mTextUnSelectedColor = textUnSelectedColor;
    }

    public void setText(String text) {
        this.mText = text;
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        defaultState();
        if (selected) {
            if (mItemIcon != null)
                mItemIcon.setImageResource(mSelectedIcon);
            if (mItemText != null)
                mItemText.setTextColor(mTextSelectedColor);
            scale(1f, SCALE_MAX);
        } else {
            if (mItemIcon != null)
                mItemIcon.setImageResource(mUnSelectedIcon);
            if (mItemText != null)
                mItemText.setTextColor(mTextUnSelectedColor);
            scale(SCALE_MAX, 1f);
        }
    }

    public void defaultState() {
        if (mItemText != null) {
            mItemText.setText(mText);
            mItemText.setTextSize(px2dip(getContext(), mTextSize));
        }
    }


    private void scale(float from, float to) {
        ValueAnimator animator = ValueAnimator.ofFloat(from, to);
        animator.setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                setScaleX(value);
                setScaleY(value);
            }
        });
        animator.start();
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
