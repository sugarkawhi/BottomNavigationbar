package me.sugarkawhi.mreader.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import me.sugarkawhi.mreader.R;
import me.sugarkawhi.mreader.bean.ChapterBean;
import me.sugarkawhi.mreader.config.Config;
import me.sugarkawhi.mreader.element.PageElement;

/**
 * Base
 * Created by ZhaoZongyao on 2018/1/11.
 */

public class BaseReaderView extends View {
    public PageElement mPageElement;
    public ChapterBean mCurChapter, mPreChapter, mNextChapter;

    public BaseReaderView(Context context) {
        this(context, null);
    }

    public BaseReaderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseReaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.BaseReaderView);
        float headerHeight = array.getDimensionPixelSize(R.styleable.BaseReaderView_mReader_headerHeight, 50);
        float footerHeight = array.getDimensionPixelSize(R.styleable.BaseReaderView_mReader_footerHeight, 50);
        float padding = array.getDimensionPixelSize(R.styleable.BaseReaderView_mReader_padding, 8);
        float chapterNameSize = array.getDimensionPixelSize(R.styleable.BaseReaderView_mReader_headerTextSize, 30);
        int batteryWidth = array.getDimensionPixelSize(R.styleable.BaseReaderView_mReader_batteryWidth, Config.DEFAULT_BATTERY_WIDTH);
        int batteryHeight = array.getDimensionPixelSize(R.styleable.BaseReaderView_mReader_batteryHeight, Config.DEFAULT_BATTERY_HEIGHT);
        int batteryHead = array.getDimensionPixelSize(R.styleable.BaseReaderView_mReader_batteryHead, Config.DEFAULT_BATTERY_HEAD);
        int batteryGap = array.getDimensionPixelSize(R.styleable.BaseReaderView_mReader_batteryGap, Config.DEFAULT_BATTERY_GAP);
        array.recycle();
        mPageElement = new PageElement(headerHeight, footerHeight, padding, chapterNameSize, batteryWidth, batteryHeight, batteryHead, batteryGap);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        mPageElement.setReaderSize(width, height);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPageElement.onDraw(canvas);
    }

    public void setTime(String time) {
        mPageElement.setTime(time);
        invalidate();
    }

    public void setElectric(float electric) {
        mPageElement.setElectric(electric);
        invalidate();
    }

    public void setChapters(ChapterBean preChapter, ChapterBean curChapter, ChapterBean nextChapter) {
        this.mPreChapter = preChapter;
        this.mCurChapter = curChapter;
        this.mNextChapter = nextChapter;
    }

    public void setPreChapter(ChapterBean preChapter) {
        this.mPreChapter = preChapter;
    }

    public void setChapter(ChapterBean curChapter) {
        this.mCurChapter = curChapter;

    }

    public void setNextChapter(ChapterBean nextChapter) {
        this.mNextChapter = nextChapter;
    }

}
