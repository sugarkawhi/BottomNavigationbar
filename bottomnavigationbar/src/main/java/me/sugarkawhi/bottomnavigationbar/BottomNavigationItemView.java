package me.sugarkawhi.bottomnavigationbar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private String mText;
    private int mTextSelectedColor;
    private int mTextUnSelectedColor;
    private int mGap;

    private ImageView mItemIcon;
    private TextView mItemText;

    private boolean isAnim;
    private float scaleRatio;
    private int mLayoutId;

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


    /**
     * //set item layout
     *
     * @param layoutId 布局ID
     */
    public void setLayoutId(int layoutId) {
        mLayoutId = layoutId;
        LayoutInflater.from(getContext()).inflate(mLayoutId, this, true);
        mItemIcon = findViewById(R.id.bnb_item_icon);
        mItemText = findViewById(R.id.bnb_item_text);
        setDefaultState();
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


    public void setTextSelectedColor(int textSelectedColor) {
        this.mTextSelectedColor = textSelectedColor;
    }

    public void setTextUnSelectedColor(int textUnSelectedColor) {
        this.mTextUnSelectedColor = textUnSelectedColor;
    }

    public void setText(String text) {
        this.mText = text;
        if (mItemText != null) {
            mItemText.setText(mText);
        }
    }


    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        rendingItemText(selected);
        rendingItemIcon(selected);
        if (isAnim) {
            if (selected) {
                scale(1f, scaleRatio > SCALE_MAX ? scaleRatio : SCALE_MAX);
            } else {
                scale(scaleRatio > SCALE_MAX ? scaleRatio : SCALE_MAX, 1f);
            }
        }

    }

    /**
     * 设置为初始状态
     * 默认未选中
     */
    public void setDefaultState() {
        rendingItemText(false);
        rendingItemIcon(false);
    }

    /**
     * rendind ICON only
     */
    private void rendingItemText(boolean select) {
        if (mItemText == null)
            return;
        if (TextUtils.isEmpty(mText)) {
            mItemText.setVisibility(GONE);
        } else {
            mItemText.setText(View.VISIBLE);
            if (select) {
                mItemText.setTextColor(mTextSelectedColor);
            } else {
                mItemText.setTextColor(mTextUnSelectedColor);
            }
        }
    }

    private void rendingItemIcon(boolean select) {
        if (mItemIcon == null)
            return;
        if (select) {
            mItemIcon.setImageResource(mSelectedIcon);
        } else {
            mItemIcon.setImageResource(mUnSelectedIcon);
        }
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
}
