package com.monster.monstersport.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.monster.monstersport.R;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by ZhaoZongyao on 2018/1/9.
 */

public class HyTabLayout extends HorizontalScrollView {
    private static final String TAG = "HyTabLayout";

    private static final float SCALE_MAX = 4 / 3f;
    private static final int DURATION = 200;

    private static final int TAB_VIEW_TEXT_SIZE_SP = 12;

    private float mDefaultTabTextSize;
    private int mDefaultTabTextColor;
    private int mTabMinWidth;
    private int mTabPadding;
    private LinearLayout mTabContainer;
    private ViewPager mViewPager;

    private int mCurrentTabIndex = -1;
    private int mLastTabIndex = 0;

    public HyTabLayout(Context context) {
        this(context, null);
    }

    public HyTabLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HyTabLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final DisplayMetrics dm = getResources().getDisplayMetrics();
        float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, TAB_VIEW_TEXT_SIZE_SP, dm);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.HyTabLayout);
        mDefaultTabTextSize = array.getDimension(R.styleable.HyTabLayout_xhh_tl_defaultTabTextSize, textSize);
        mDefaultTabTextColor = array.getColor(R.styleable.HyTabLayout_xhh_tl_defaultTabTextColor, Color.BLACK);
        mTabMinWidth = array.getDimensionPixelOffset(R.styleable.HyTabLayout_xhh_tl_tabMinWidth, 80);
        mTabPadding = array.getDimensionPixelOffset(R.styleable.HyTabLayout_xhh_tl_tabPadding, 10);
        array.recycle();
        init();
    }

    private void init() {
        mTabContainer = new LinearLayout(getContext());
        mTabContainer.setOrientation(LinearLayout.HORIZONTAL);
        mTabContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mTabContainer.setGravity(Gravity.CENTER_VERTICAL);
        addView(mTabContainer);
    }

    private TextView createDefaultTabView(String title, final int index) {
        TextView textView = new TextView(getContext());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mDefaultTabTextSize);
        textView.setTextColor(mDefaultTabTextColor);
        textView.setText(title);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(mTabPadding, 0, mTabPadding, 0);
        textView.setSingleLine();
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTab(index);
            }
        });
        return textView;
    }

    private void addTab(int position, TextView tab) {
        tab.measure(0, 0);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        params.width = Math.max(tab.getMeasuredWidth(), mTabMinWidth);
        params.height = MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        mTabContainer.addView(tab, position, params);
    }

    public void setTabs(List<String> tabTitles) {
        mTabContainer.removeAllViews();
        for (int i = 0; i < tabTitles.size(); i++) {
            addTab(i, createDefaultTabView(tabTitles.get(i), i));
        }
        selectTab(0);
    }

    public void selectTab(int index) {
        if (mCurrentTabIndex == index || index >= mTabContainer.getChildCount()) return;
        mLastTabIndex = mCurrentTabIndex;
        mCurrentTabIndex = index;
        mTabContainer.getChildAt(mCurrentTabIndex)
                .animate()
                .scaleX(SCALE_MAX)
                .scaleY(SCALE_MAX)
                .setDuration(DURATION)
                .start();
        if (mLastTabIndex >= 0) {
            mTabContainer.getChildAt(mLastTabIndex)
                    .animate()
                    .scaleX(1f)
                    .setDuration(DURATION)
                    .scaleY(1)
                    .start();
            if (mViewPager != null && mViewPager.getCurrentItem() != index) {
                mViewPager.setCurrentItem(index);
            }
        }
    }

    public void setViewPager(ViewPager viewPager) {
        mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e(TAG, "onPageScrolled position =" + position + " positionOffset=" + positionOffset + " positionOffsetPixels=" + positionOffsetPixels);

            }

            @Override
            public void onPageSelected(int position) {
                Log.e(TAG, "onPageSelected position=" + position);
                selectTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.e(TAG, "onPageScrollStateChanged state=" + state);
            }
        });
    }


}
