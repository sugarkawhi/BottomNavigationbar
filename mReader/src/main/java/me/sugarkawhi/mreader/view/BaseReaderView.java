package me.sugarkawhi.mreader.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import me.sugarkawhi.mreader.R;
import me.sugarkawhi.mreader.anim.PageAnimation;
import me.sugarkawhi.mreader.bean.Battery;
import me.sugarkawhi.mreader.bean.ChapterBean;
import me.sugarkawhi.mreader.config.Config;
import me.sugarkawhi.mreader.element.PageElement;
import me.sugarkawhi.mreader.listener.IReaderTouchListener;
import me.sugarkawhi.mreader.utils.ScreenUtils;

/**
 * Base
 * Created by ZhaoZongyao on 2018/1/11.
 */
public class BaseReaderView extends View {
    public PageElement mPageElement;
    public ChapterBean mCurChapter, mPreChapter, mNextChapter;

    //View 宽 强制全屏
    private float mWidth;
    //View 高 强制全屏
    private float mHeight;
    //内容文字代销
    private float mContentSize;
    //行间距
    private float mLineSpacing;
    //段落间距
    private float mParagraphSpacing;
    //当前页 Bitmap/Canvas
    private Bitmap mCurrentBitmap;
    //下一页 Bitmap/Canvas
    private Bitmap mNextBitmap;

    private PageAnimation mPageAnimation;

    private IReaderTouchListener mReaderTouchListener;

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
        int batteryWidth = array.getDimensionPixelSize(R.styleable.BaseReaderView_mReader_batteryWidth, Config.DEFAULT_BATTERY_WIDTH);
        int batteryHeight = array.getDimensionPixelSize(R.styleable.BaseReaderView_mReader_batteryHeight, Config.DEFAULT_BATTERY_HEIGHT);
        int batteryHead = array.getDimensionPixelSize(R.styleable.BaseReaderView_mReader_batteryHead, Config.DEFAULT_BATTERY_HEAD);
        int batteryGap = array.getDimensionPixelSize(R.styleable.BaseReaderView_mReader_batteryGap, Config.DEFAULT_BATTERY_GAP);
        array.recycle();
        mContentSize = Config.DEFAULT_CONTENT_TEXTSIZE;
        mLineSpacing = Config.DEFAULT_CONTENT_LINE_SPACING;
        mParagraphSpacing = Config.DEFAULT_CONTENT_PARAGRAPH_SPACING;
        Battery battery = new Battery(batteryHead, batteryWidth, batteryHeight, batteryGap);
        int screenSize[] = ScreenUtils.getScreenSize(context);
        mWidth = screenSize[0];
        mHeight = screenSize[1];
        mPageElement = new PageElement(mWidth, mHeight,
                headerHeight, footerHeight, padding,
                mLineSpacing, mParagraphSpacing,
                battery);
        init();
    }

    private void init() {
        mCurrentBitmap = Bitmap.createBitmap((int) mWidth, (int) mHeight, Bitmap.Config.RGB_565);
        mNextBitmap = Bitmap.createBitmap((int) mWidth, (int) mHeight, Bitmap.Config.RGB_565);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPageElement.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = (int) event.getX();
                y = (int) event.getY();
                if (x > mWidth / 3 && x < mWidth / 3 * 2) {
                    //点击了中心区域
                    if (mReaderTouchListener != null) mReaderTouchListener.onTouchCenter();
                }
                performClick();
                return true;
            case MotionEvent.ACTION_MOVE:
                return true;
            case MotionEvent.ACTION_UP:
                x = (int) event.getX();
                y = (int) event.getY();
                return true;
            case MotionEvent.ACTION_CANCEL:
                return true;

        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }


    /**
     * set reader touch listener
     */
    public void setReaderTouchListener(IReaderTouchListener readerTouchListener) {
        mReaderTouchListener = readerTouchListener;
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
        mPageElement.setChapter(curChapter);
    }

    public void setNextChapter(ChapterBean nextChapter) {
        this.mNextChapter = nextChapter;
    }

}
