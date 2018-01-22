package me.sugarkawhi.mreader.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import me.sugarkawhi.mreader.R;
import me.sugarkawhi.mreader.anim.CoverAnimController;
import me.sugarkawhi.mreader.anim.CoverPageAnim;
import me.sugarkawhi.mreader.anim.PageAnimController;
import me.sugarkawhi.mreader.anim.PageAnimation;
import me.sugarkawhi.mreader.anim.SlideAnimController;
import me.sugarkawhi.mreader.bean.Battery;
import me.sugarkawhi.mreader.bean.ChapterBean;
import me.sugarkawhi.mreader.config.IReaderConfig;
import me.sugarkawhi.mreader.config.IReaderPageMode;
import me.sugarkawhi.mreader.data.PageData;
import me.sugarkawhi.mreader.element.PageElement;
import me.sugarkawhi.mreader.listener.IReaderTouchListener;
import me.sugarkawhi.mreader.manager.PageManager;
import me.sugarkawhi.mreader.utils.ScreenUtils;

/**
 * Base
 * Created by ZhaoZongyao on 2018/1/11.
 */
public class MReaderView extends View {

    private static final String TAG = "MReaderView";

    public PageElement mPageElement;
    public ChapterBean mCurChapter, mPreChapter, mNextChapter;

    //View 宽 强制全屏
    private int mWidth;
    //View 高 强制全屏
    private int mHeight;
    //内容文字大小
    private float mContentSize;
    //字间距
    private float mLetterSpacing;
    //行间距
    private float mLineSpacing;
    //段落间距
    private float mParagraphSpacing;


    //章节名 Paint
    private Paint mChapterNamePaint;
    //内容 Paint
    private Paint mContentPaint;
    //头 底 Paint
    private Paint mHeaderPaint;
    //分页
    PageManager mPageManager;

    private PageAnimController mAnimController;

    public MReaderView(Context context) {
        this(context, null);
    }

    public MReaderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MReaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MReaderView);
        float headerHeight = array.getDimensionPixelSize(R.styleable.MReaderView_mReader_headerHeight, 50);
        float footerHeight = array.getDimensionPixelSize(R.styleable.MReaderView_mReader_footerHeight, 50);
        float padding = array.getDimensionPixelSize(R.styleable.MReaderView_mReader_padding, 8);
        int batteryWidth = array.getDimensionPixelSize(R.styleable.MReaderView_mReader_batteryWidth, IReaderConfig.DEFAULT_BATTERY_WIDTH);
        int batteryHeight = array.getDimensionPixelSize(R.styleable.MReaderView_mReader_batteryHeight, IReaderConfig.DEFAULT_BATTERY_HEIGHT);
        int batteryHead = array.getDimensionPixelSize(R.styleable.MReaderView_mReader_batteryHead, IReaderConfig.DEFAULT_BATTERY_HEAD);
        int batteryGap = array.getDimensionPixelSize(R.styleable.MReaderView_mReader_batteryGap, IReaderConfig.DEFAULT_BATTERY_GAP);
        array.recycle();
        mContentSize = IReaderConfig.DEFAULT_CONTENT_TEXTSIZE;
        mLineSpacing = IReaderConfig.DEFAULT_CONTENT_LINE_SPACING;
        mLetterSpacing = IReaderConfig.DEFAULT_CONTENT_LETTER_SPACING;
        mParagraphSpacing = IReaderConfig.DEFAULT_CONTENT_PARAGRAPH_SPACING;
        Battery battery = new Battery(batteryHead, batteryWidth, batteryHeight, batteryGap);
        int screenSize[] = ScreenUtils.getScreenSize(context);
        mWidth = screenSize[0];
        mHeight = screenSize[1];
        mContentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mContentPaint.setTextSize(IReaderConfig.DEFAULT_CONTENT_TEXTSIZE);
        mContentPaint.setColor(Color.parseColor("#404040"));
        mHeaderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHeaderPaint.setTextSize(IReaderConfig.DEFAULT_HEADER_TEXTSIZE);

        mChapterNamePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mChapterNamePaint.setTextSize(IReaderConfig.DEFAULT_CONTENT_TEXTSIZE * 1.3f);
        mChapterNamePaint.setColor(Color.parseColor("#A0522D"));
        mPageElement = new PageElement(mWidth, mHeight,
                headerHeight, footerHeight, padding, mLineSpacing, battery,
                mHeaderPaint, mContentPaint, mChapterNamePaint);
        mPageManager = new PageManager(mWidth - padding - padding, mHeight - headerHeight - footerHeight,
                mLetterSpacing, mLineSpacing, mParagraphSpacing,
                20, 50, mContentPaint, mChapterNamePaint);
        init();
    }

    private void init() {
        setMode(IReaderPageMode.SLIDE);

    }

    public void setMode(int mode) {
        switch (mode) {
            case IReaderPageMode.COVER:
                mAnimController = new CoverAnimController(this, mWidth, mHeight, mPageElement);
                break;
            case IReaderPageMode.SLIDE:
                mAnimController = new SlideAnimController(this, mWidth, mHeight, mPageElement);
                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mAnimController.dispatchDrawPage(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mAnimController.dispatchTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        mAnimController.computeScroll();
        super.computeScroll();
    }

    /**
     * set reader touch listener
     */
    public void setReaderTouchListener(IReaderTouchListener readerTouchListener) {
        mAnimController.setIReaderTouchListener(readerTouchListener);
    }

    public void setTime(String time) {
        mPageElement.setTime(time);
        mAnimController.invalidate();
        invalidate();

    }

    public void setElectric(float electric) {
        mPageElement.setElectric(electric);
        mAnimController.invalidate();
        invalidate();
    }

    public void setChapters(ChapterBean preChapter, ChapterBean curChapter, ChapterBean nextChapter) {
        this.mPreChapter = preChapter;
        this.mCurChapter = curChapter;
        this.mNextChapter = nextChapter;
    }


    public void setChapter(ChapterBean curChapter) {
        this.mCurChapter = curChapter;
        chapterHandler(curChapter);
    }

    private void chapterHandler(ChapterBean chapter) {
        if (chapter == null) return;
        // convert String into InputStream
        InputStream is = new ByteArrayInputStream(chapter.getChapterContent().getBytes());
        // read it with BufferedReader
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        List<PageData> pages = mPageManager.generatePages(chapter, br);
        PageData pageData = pages.get(0);
        mAnimController.setCurrentPageData(pageData);
        mAnimController.setNextPageData(pages.get(1));
    }

}
