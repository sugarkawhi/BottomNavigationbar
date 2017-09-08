package me.sugarkawhi.bottomnavigationbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import java.util.ArrayList;
import java.util.List;

/**
 * 自定义顶部导航栏
 * Created by ZhaoZongyao on 2017/8/25.
 */

public class BottomNavigationBar extends LinearLayout implements View.OnClickListener {

    private String TAG = "BottomNavigationBar";

    private OnBottomNavigationBarItemClickListener onBottomNavigationBarItemClickListener;
    private OnBottomNavigationBarItemDoubleClickListener onBottomNavigationBarItemDoubleClickListener;
    private List<BottomNavigationEntity> entities = new ArrayList<>();

    //这里是-1主要是为了第一次比较
    private int mCurrentPosition = -1;
    //这里是0 是因为第一次进入默认是0
    private int mLastPosition = 0;

    private int mTextSelectedColor;
    private int mTextUnSelectedColor;
    //dot 用于实现提醒的功能
    private int mDotColor;
    private int mTextSize;
    private static final String DEFAULT_SELECTED_COLOR = "#000000";
    private static final String DEFAULT_UNSELECTED_COLOR = "#999999";
    private static final String DEFAULT_DOT_COLOR = "#ff0000";

    public BottomNavigationBar(Context context) {
        this(context, null);
    }

    public BottomNavigationBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomNavigationBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, 0);
        init(context, attrs);
    }

    public void setEntities(List<BottomNavigationEntity> list) {
        entities.clear();
        entities.addAll(list);
        addItems();
    }

    /**
     * 初始化
     */
    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.BottomNavigationBar);
        mTextSelectedColor = array.getColor(R.styleable.BottomNavigationBar_selectedColor, Color.parseColor(DEFAULT_SELECTED_COLOR));
        mTextUnSelectedColor = array.getColor(R.styleable.BottomNavigationBar_unSelectedColor, Color.parseColor(DEFAULT_UNSELECTED_COLOR));
        mDotColor = array.getColor(R.styleable.BottomNavigationBar_dotColor, Color.parseColor(DEFAULT_DOT_COLOR));
        mTextSize = array.getDimensionPixelSize(R.styleable.BottomNavigationBar_textSize, 12);

        array.recycle();
    }

    /**
     * 添加item
     */
    private void addItems() {
        if (entities.isEmpty())
            return;
        LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        for (int i = 0; i < entities.size(); i++) {
            BottomNavigationEntity entity = entities.get(i);
            BottomNavigationItemView item = new BottomNavigationItemView(getContext());
            item.setText(entity.text);
            item.setTextSize(mTextSize);
            item.setSelectedIcon(entity.selectedIcon);
            item.setUnSelectedIcon(entity.unSelectIcon);
            item.setTextSelectedColor(mTextSelectedColor);
            item.setTextUnSelectedColor(mTextUnSelectedColor);
            item.setTag(i);
            addView(item, params);
            item.setOnClickListener(this);
            if (i == 0) {
                setCurrentPosition(0);
            } else {
                item.setSelected(false);
            }
        }

    }

    @Override
    public void onClick(View view) {
        int position = (int) view.getTag();
        Log.e(TAG, "onClick: position=" + position);
        if (onBottomNavigationBarItemClickListener != null)
            onBottomNavigationBarItemClickListener.onBottomNavigationBarItemClick((Integer) view.getTag());
    }


    public void setOnBottomNavigationBarItemClickListener(OnBottomNavigationBarItemClickListener onBottomNavigationBarItemClickListener) {
        this.onBottomNavigationBarItemClickListener = onBottomNavigationBarItemClickListener;
    }

    public void setOnBottomNavigationBarItemDoubleClickListener(OnBottomNavigationBarItemDoubleClickListener onBottomNavigationBarItemDoubleClickListener) {
        this.onBottomNavigationBarItemDoubleClickListener = onBottomNavigationBarItemDoubleClickListener;
    }

    /**
     * 设置当前选中位置
     */
    public void setCurrentPosition(int position) {
        int count = getChildCount();
        if (position == mCurrentPosition && onBottomNavigationBarItemDoubleClickListener != null) {
            onBottomNavigationBarItemDoubleClickListener.onBottomNavigationBarItemDoubleClick(position);
            return;
        }
        if (count == 0 || position > count)
            return;
        mLastPosition = mCurrentPosition;
        mCurrentPosition = position;
        BottomNavigationItemView lastItem = (BottomNavigationItemView) getChildAt(mLastPosition);
        BottomNavigationItemView currentItem = (BottomNavigationItemView) getChildAt(mCurrentPosition);
        if (lastItem != null) {
            lastItem.setSelected(false);
        }
        if (currentItem != null) {
            currentItem.setSelected(true);
        }
    }

    public static class BottomNavigationEntity {
        String text;
        int selectedIcon;
        int unSelectIcon;

        public BottomNavigationEntity(String text, int unSelectIcon, int selectedIcon) {
            this.text = text;
            this.unSelectIcon = unSelectIcon;
            this.selectedIcon = selectedIcon;
        }
    }

    public interface OnBottomNavigationBarItemClickListener {
        void onBottomNavigationBarItemClick(int position);
    }

    public interface OnBottomNavigationBarItemDoubleClickListener {
        void onBottomNavigationBarItemDoubleClick(int position);
    }


}
