package me.sugarkawhi.pulltomark;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 下拉添加书签头部
 * Created by ZhaoZongyao on 2018/3/22.
 */

public class PtmHeader extends FrameLayout {

    private View mPtmHeader;
    private TextView mTitleTextView;
    private ImageView mBookMarkView;
    private ImageView mArrowView;
    private boolean isDown = true;//朝下

    public PtmHeader(@NonNull Context context) {
        this(context, null);
    }

    public PtmHeader(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PtmHeader(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(attrs);
    }

    protected void initViews(AttributeSet attrs) {
        View header = LayoutInflater.from(getContext()).inflate(R.layout.ptm_header, this);
        mTitleTextView = header.findViewById(R.id.tv_mark);
        mBookMarkView = header.findViewById(R.id.iv_mark);
        mPtmHeader = header.findViewById(R.id.ptm_header);
        mArrowView = header.findViewById(R.id.iv_arrow);
    }

    public int getPtmHeaderHeight() {
        return mPtmHeader.getMeasuredHeight();
    }

    /**
     * 准备添加书签
     */
    protected void prepareAdd() {
        mTitleTextView.setText("下拉添加书签");
        mBookMarkView.setImageResource(R.drawable.book_unmark);
        arrowDown();
    }

    /**
     * 松手添加书签
     */
    protected void releaseAdd() {
        mTitleTextView.setText("松手添加书签");
        mBookMarkView.setImageResource(R.drawable.book_mark);
    }

    /**
     * 准备删除书签
     */
    protected void prepareDelete() {
        mTitleTextView.setText("下拉删除书签");
        mBookMarkView.setImageResource(R.drawable.book_mark);
        arrowDown();
    }

    /**
     * 松手删除书签
     */
    protected void releaseDelete() {
        mTitleTextView.setText("松手删除书签");
        mBookMarkView.setImageResource(R.drawable.book_unmark);
    }

    /**
     * 箭头往上
     */
    protected void arrowUp() {
        if (!isDown) return;
        isDown = false;
        mArrowView.animate()
                .rotation(180)
                .setDuration(100)
                .start();
    }

    /**
     * 箭头往下
     */
    protected void arrowDown() {
        if (isDown) return;
        isDown = true;
        mArrowView.animate()
                .rotation(0)
                .setDuration(100)
                .start();
    }
}
