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
 * BottomNavigationItemView
 * Created by sugarkawhi on 2017/8/25.
 */
class BottomNavigationItemView extends LinearLayout {
    private int mSelectedIcon;
    private int mUnSelectedIcon;
    private int mTextSize;
    private String mText;
    private int mTextSelectedColor;
    private int mTextUnSelectedColor;

    private ImageView mItemIcon;
    private TextView mItemText;

    private boolean isAnim;
    private float scaleRatio;

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
        LayoutInflater.from(getContext()).inflate(R.layout.bnb_item_view, this, true);
        mItemIcon = findViewById(R.id.bnb_item_icon);
        mItemText = findViewById(R.id.bnb_item_text);
        setDefaultState();
    }

    public void setScaleRatio(float scaleRatio) {
        this.scaleRatio = Math.abs(scaleRatio);
    }

    /*unused*/
    public float getScaleRatio() {
        return scaleRatio;
    }

    public void setAnim(boolean anim) {
        isAnim = anim;
    }

    /*unused*/
    public boolean isAnim() {
        return isAnim;
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
        if (selected) {
            if (mItemIcon != null)
                mItemIcon.setImageResource(mSelectedIcon);
            if (mItemText != null)
                mItemText.setTextColor(mTextSelectedColor);
            if (isAnim) {
                scale(1f, scaleRatio > SCALE_MAX ? scaleRatio : SCALE_MAX);
            }
        } else {
            if (mItemIcon != null)
                mItemIcon.setImageResource(mUnSelectedIcon);
            if (mItemText != null)
                mItemText.setTextColor(mTextUnSelectedColor);
            if (isAnim) {
                scale(scaleRatio > SCALE_MAX ? scaleRatio : SCALE_MAX, 1f);
            }
        }
    }

    /**
     * 设置为初始状态
     * 默认未选中
     */
    public void setDefaultState() {
        if (mItemText != null) {
            mItemText.setText(mText);
            mItemText.setTextColor(mTextUnSelectedColor);
            mItemText.setTextSize(px2dip(getContext(), mTextSize));
        }
        if (mItemIcon != null)
            mItemIcon.setImageResource(mUnSelectedIcon);
    }

    private ValueAnimator valueAnimator;

    private void scale(float from, float to) {
        valueAnimator = ValueAnimator.ofFloat(from, to);
        valueAnimator.setDuration(200);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                setScaleX(value);
                setScaleY(value);
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        if (null != valueAnimator) valueAnimator.cancel();
        super.onDetachedFromWindow();
    }

    private static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
