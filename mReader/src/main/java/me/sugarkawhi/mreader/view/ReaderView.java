package me.sugarkawhi.mreader.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import me.sugarkawhi.mreader.R;
import me.sugarkawhi.mreader.anim.CoverAnimController;
import me.sugarkawhi.mreader.anim.PageAnimController;
import me.sugarkawhi.mreader.anim.SlideAnimController;
import me.sugarkawhi.mreader.bean.Battery;
import me.sugarkawhi.mreader.bean.BookBean;
import me.sugarkawhi.mreader.bean.ChapterBean;
import me.sugarkawhi.mreader.config.IReaderConfig;
import me.sugarkawhi.mreader.config.IReaderPageMode;
import me.sugarkawhi.mreader.data.PageData;
import me.sugarkawhi.mreader.element.PageElement;
import me.sugarkawhi.mreader.listener.IReaderTouchListener;
import me.sugarkawhi.mreader.manager.PageManager;
import me.sugarkawhi.mreader.persistence.IReaderPersistence;
import me.sugarkawhi.mreader.utils.BitmapUtils;
import me.sugarkawhi.mreader.utils.L;
import me.sugarkawhi.mreader.utils.ScreenUtils;

/**
 * Base
 * Created by ZhaoZongyao on 2018/1/11.
 */
public class ReaderView extends View {

    private static final String TAG = "MReaderView";

    public PageElement mPageElement;
    public ChapterBean mCurChapter, mPreChapter, mNextChapter;

    //背景图
    private Bitmap mReaderBackgroundBitmap;

    //View 宽 强制全屏
    private int mWidth;
    //View 高 强制全屏
    private int mHeight;
    //字间距
    private float mLetterSpacing;
    //行间距
    private float mLineSpacing;
    //段落间距
    private float mParagraphSpacing;


    //封面 Paint
    private Paint mCoverPaint;
    //章节名 Paint
    private Paint mChapterNamePaint;
    //内容 Paint
    private Paint mContentPaint;
    //头 底 Paint
    private Paint mHeaderPaint;
    //分页
    PageManager mPageManager;

    //承载封面内容的View
    private View mCoverView;

    private PageAnimController mAnimController;

    public ReaderView(Context context) {
        this(context, null);
    }

    public ReaderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ReaderView);
        float headerHeight = array.getDimensionPixelSize(R.styleable.ReaderView_mReader_headerHeight, 50);
        float footerHeight = array.getDimensionPixelSize(R.styleable.ReaderView_mReader_footerHeight, 50);
        float padding = array.getDimensionPixelSize(R.styleable.ReaderView_mReader_padding, 8);
        array.recycle();
        int contentFontSize = IReaderPersistence.getFontSize(context);
        mLineSpacing = IReaderConfig.LineSpacing.DEFAULT;
        mLetterSpacing = IReaderConfig.LetterSpacing.DEFAULT;
        mParagraphSpacing = IReaderConfig.ParagraphSpacing.DEFAULT;
        Battery battery = new Battery(IReaderConfig.Battery.HEAD, IReaderConfig.Battery.WIDTH,
                IReaderConfig.Battery.HEIGHT, IReaderConfig.Battery.GAP);
        int screenSize[] = ScreenUtils.getScreenSize(context);
        mWidth = screenSize[0];
        mHeight = screenSize[1];
        //
        mCoverPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //内容
        mContentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mContentPaint.setTextSize(contentFontSize);
        //头部
        mHeaderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHeaderPaint.setTextSize(IReaderConfig.DEFAULT_HEADER_TEXTSIZE);

        mChapterNamePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mChapterNamePaint.setTextSize(contentFontSize * IReaderConfig.RATIO_CHAPTER_CONTENT);
        mChapterNamePaint.setColor(Color.parseColor("#A0522D"));
        mPageElement = new PageElement(mWidth, mHeight,
                headerHeight, footerHeight, padding, mLineSpacing, battery,
                mHeaderPaint, mContentPaint, mChapterNamePaint);
        mPageManager = new PageManager(mWidth - padding - padding, mHeight - headerHeight - footerHeight,
                mLetterSpacing, mLineSpacing, mParagraphSpacing,
                20, 50,
                mCoverPaint, mContentPaint, mChapterNamePaint);
        init();
        Bitmap cover_bly = BitmapUtils.sampling(getResources(), R.drawable.cover_zljts, mWidth, mHeight);
        Bitmap bitmap = BitmapUtils.scaleBitmap(cover_bly, IReaderConfig.Cover.IMG_WIDTH, IReaderConfig.Cover.IMG_HEIGHT);
        mPageManager.setCover(bitmap);
        BookBean book = new BookBean();
        book.setName("战略级天使");
        book.setAuthorName("白伯欢 / 作品");
        mPageManager.setBook(book);
    }

    private void init() {
        setMode(IReaderPageMode.SLIDE);
    }

    public void setMode(int mode) {
        switch (mode) {
            case IReaderPageMode.COVER:
                mAnimController = new CoverAnimController(this, mWidth, mHeight, mPageElement, mPageChangeListener);
                break;
            case IReaderPageMode.SLIDE:
                mAnimController = new SlideAnimController(this, mWidth, mHeight, mPageElement, mPageChangeListener);
                break;
        }
    }

    private PageAnimController.IPageChangeListener mPageChangeListener = new PageAnimController.IPageChangeListener() {
        @Override
        public void onCancel() {

        }

        @Override
        public void cancelPre() {
            mCurrentIndex--;
            mAnimController.setCurrentPageData(mPageDataList.get(mCurrentIndex));
            invalidate();
        }

        @Override
        public void cancelNext() {

        }

        @Override
        public boolean hasPre() {
            return mCurrentIndex > 0;
        }

        @Override
        public boolean hasNext() {
            return mCurrentIndex < mPageDataList.size() - 1;
        }

        @Override
        public PageData getPrePageData() {
            return mPageDataList.get(mCurrentIndex - 1);
        }


        @Override
        public void onSelectPre() {
            mCurrentIndex--;
            mAnimController.setCurrentPageData(mPageDataList.get(mCurrentIndex));
            invalidate();
        }

        @Override
        public PageData getNextPageData() {
            return mPageDataList.get(mCurrentIndex + 1);
        }


        @Override
        public void onSelectNext() {
            mCurrentIndex++;
            mAnimController.setCurrentPageData(mPageDataList.get(mCurrentIndex));
            invalidate();
        }


    };

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

    //阅读器维护一个页面的队列
    private List<PageData> mPageDataList = new ArrayList<>();
    private int mCurrentIndex = 0;

    private void chapterHandler(ChapterBean chapter) {
        if (chapter == null) return;
        // convert String into InputStream
        InputStream is = new ByteArrayInputStream(chapter.getChapterContent().getBytes());
        // read it with BufferedReader
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        List<PageData> pages = mPageManager.generatePages(chapter, br);
        mPageDataList.clear();
        mPageDataList.addAll(pages);

        mAnimController.setCurrentPageData(mPageDataList.get(mCurrentIndex));
    }

    public Bitmap getReaderBackgroundBitmap() {
        return mReaderBackgroundBitmap;
    }


    /**
     * 背景与文字颜色 1对1
     *
     * @param bitmap    绘制背景bitmap
     * @param fontColor 背景对应字体颜色
     */
    public void setReaderBackground(Bitmap bitmap, int fontColor) {
        mReaderBackgroundBitmap = BitmapUtils.scaleBitmap(bitmap, mWidth, mHeight);
        mPageElement.setBackgroundBitmap(mReaderBackgroundBitmap);
        mContentPaint.setColor(fontColor);
        mHeaderPaint.setColor(fontColor);
        generateCover();
        mAnimController.invalidate();
        invalidate();
    }


    /**
     * 设置当前进度
     * TODO
     */
    public void setProgress(int progress) {
        //
    }

    /**
     * 设置文字大小
     * TODO 1.需要重新分页2.分页后保持当前进度
     */
    public void setFontSize(float fontSize) {
        if (fontSize < IReaderConfig.FontSize.MIN) {
            L.w(TAG, "font size is too small");
            return;
        }
        if (fontSize > IReaderConfig.FontSize.MAX) {
            L.w(TAG, "font size is too large");
            return;
        }
        mContentPaint.setTextSize(fontSize);
        mChapterNamePaint.setTextSize(fontSize * IReaderConfig.RATIO_CHAPTER_CONTENT);
        chapterHandler(mCurChapter);
        mAnimController.invalidate();
        invalidate();
    }

    /**
     * 如果是第一章 生成封面
     */
    private void generateCover() {
        if (mCoverView == null) return;
        Bitmap coverBitmap = BitmapUtils.getCoverBitmap(mCoverView, getReaderBackgroundBitmap());
        mPageElement.setCoverBitmap(coverBitmap);
        mAnimController.invalidate();
        invalidate();
    }

    /**
     * 封面View
     *
     * @param coverView
     */
    public void setCoverView(View coverView) {
        mCoverView = coverView;
        generateCover();
    }

}
