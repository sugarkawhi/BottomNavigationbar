package me.sugarkawhi.mreader.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import me.sugarkawhi.mreader.R;
import me.sugarkawhi.mreader.anim.CoverAnimController;
import me.sugarkawhi.mreader.anim.NoneAnimController;
import me.sugarkawhi.mreader.anim.PageAnimController;
import me.sugarkawhi.mreader.anim.SlideAnimController;
import me.sugarkawhi.mreader.bean.BaseChapterBean;
import me.sugarkawhi.mreader.config.IReaderConfig;
import me.sugarkawhi.mreader.config.IReaderDirection;
import me.sugarkawhi.mreader.config.IReaderPageMode;
import me.sugarkawhi.mreader.data.PageData;
import me.sugarkawhi.mreader.element.PageElement;
import me.sugarkawhi.mreader.listener.IReaderTouchListener;
import me.sugarkawhi.mreader.manager.PageGenerater;
import me.sugarkawhi.mreader.manager.PageManager;
import me.sugarkawhi.mreader.manager.PageRespository;
import me.sugarkawhi.mreader.persistence.IReaderPersistence;
import me.sugarkawhi.mreader.utils.BitmapUtils;
import me.sugarkawhi.mreader.utils.L;
import me.sugarkawhi.mreader.utils.ScreenUtils;

/**
 * Base
 * Created by ZhaoZongyao on 2018/1/11.
 */
public class ReaderView extends View {

    //加载中
    private static final int STATE_LOADING = 1;
    //打开书籍成功
    private static final int STATE_OPEN = 2;

    //当前状态
    private int mCurrentState = STATE_LOADING;

    private static final String TAG = "MReaderView";

    public PageElement mPageElement;

    //背景图
    private Bitmap mReaderBackgroundBitmap;

    //View 宽 强制全屏
    private int mWidth;
    //View 高 强制全屏
    private int mHeight;


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

    private IReaderTouchListener mReaderTouchListener;
    private PageAnimController mAnimController;

    private PageRespository mRespository;

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
        int lineSpacing = IReaderConfig.LineSpacing.DEFAULT;
        int letterSpacing = IReaderConfig.LetterSpacing.DEFAULT;
        int paragraphSpacing = IReaderConfig.ParagraphSpacing.DEFAULT;
        mWidth = ScreenUtils.getScreenWidth(context);
        mHeight = ScreenUtils.getScreenHeight(context);
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
                headerHeight, footerHeight, padding,
                mHeaderPaint, mContentPaint, mChapterNamePaint);
        mPageManager = new PageManager(mWidth - padding - padding, mHeight - headerHeight - footerHeight,
                letterSpacing, lineSpacing, paragraphSpacing,
                20, mContentPaint, mChapterNamePaint);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mRespository = new PageRespository(mPageElement);
        setPageMode(IReaderPageMode.COVER);
    }

    /**
     * 设置翻页模式
     * {@link #(IReaderPageMode.COVER)} 覆盖模式
     * {@link #(IReaderPageMode.SLIDE)}  平移模式
     * {@link #(IReaderPageMode.NONE)}  无动画
     * {@link #(IReaderPageMode.SIMULATION)} 仿真
     *
     * @param mode 翻页模式
     */
    public void setPageMode(int mode) {
        switch (mode) {
            case IReaderPageMode.COVER:
                mAnimController = new CoverAnimController(this, mWidth, mHeight, mPageElement, mPageChangeListener);
                break;
            case IReaderPageMode.SLIDE:
                mAnimController = new SlideAnimController(this, mWidth, mHeight, mPageElement, mPageChangeListener);
                break;
            case IReaderPageMode.NONE:
                mAnimController = new NoneAnimController(this, mWidth, mHeight, mPageElement, mPageChangeListener);
                break;
        }
        mAnimController.setIReaderTouchListener(mReaderTouchListener);
    }

    private PageAnimController.IPageChangeListener mPageChangeListener = new PageAnimController.IPageChangeListener() {

        @Override
        public void onCancel(int direction) {
            mRespository.cancel(direction);
        }

        @Override
        public void onSelectPage(int direction, boolean isCancel) {
            if (direction == IReaderDirection.NEXT) {
                if (!isCancel) {
                    PageGenerater.generate(mPageElement, mRespository.getCurPage(), mAnimController.getCurrentBitmap());
                } else {
                    PageGenerater.generate(mPageElement, mRespository.getCancelPage(), mAnimController.getCurrentBitmap());
                }
            }
        }

        @Override
        public boolean hasPre() {
            boolean hasPre = mRespository.pre(mAnimController.getCurrentBitmap(), mAnimController.getNextBitmap());
            return hasPre;
        }

        @Override
        public boolean hasNext() {
            boolean hasNext = mRespository.next(mAnimController.getCurrentBitmap(), mAnimController.getNextBitmap());
            return hasNext;
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        if (mCurrentState == STATE_LOADING) {
            if (mReaderBackgroundBitmap != null)
                canvas.drawBitmap(mReaderBackgroundBitmap, 0, 0, null);
        } else {
            mAnimController.dispatchDrawPage(canvas);
        }
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
        this.mReaderTouchListener = readerTouchListener;
        mAnimController.setIReaderTouchListener(mReaderTouchListener);
    }


    public void setTime(String time) {
        mPageElement.setTime(time);

        invalidate();
    }

    public void setElectric(float electric) {
        mPageElement.setElectric(electric);

        invalidate();
    }


    private BaseChapterBean mCurChapter, mPreChapter, mNextChapter;

    /**
     * 设置当前章节
     * 阅读器维护一个页面的队列
     *
     * @param preChapter
     * @param curChapter
     * @param nextChapter
     */
    public void setChapters(BaseChapterBean preChapter, BaseChapterBean curChapter, BaseChapterBean nextChapter) {
        mPreChapter = preChapter;
        mCurChapter = curChapter;
        mNextChapter = nextChapter;
        new AsyncTask<BaseChapterBean, Void, List<List<PageData>>>() {
            @Override
            protected List<List<PageData>> doInBackground(BaseChapterBean... chapters) {
                List<List<PageData>> lists = new ArrayList<>();
                lists.add(mPageManager.generatePages(chapters[0]));
                return lists;
            }

            @Override
            protected void onPostExecute(List<List<PageData>> lists) {
                super.onPostExecute(lists);
                mCurrentState = STATE_OPEN;
                mRespository.setCurPageList(lists.get(0));
                drawCurrentPage();
                invalidate();
            }
        }.execute(curChapter, preChapter, curChapter);
    }

    /**
     * 设置当前章节
     * 阅读器维护一个页面的队列
     *
     * @param preChapter
     * @param curChapter
     * @param nextChapter
     */
    public void setChapter(BaseChapterBean curChapter, final float progress) {
        mCurChapter = curChapter;
        new AsyncTask<BaseChapterBean, Void, List<PageData>>() {

            @Override
            protected List<PageData> doInBackground(BaseChapterBean... chapterBeans) {
                return mPageManager.generatePages(chapterBeans[0]);
            }

            @Override
            protected void onPostExecute(List<PageData> list) {
                super.onPostExecute(list);
                mCurrentState = STATE_OPEN;
                mRespository.setCurPageList(list);
                mRespository.setCurPage(progress);
                drawCurrentPage();
                invalidate();
            }
        }.execute(curChapter);
    }

    public Bitmap getReaderBackgroundBitmap() {
        return mReaderBackgroundBitmap;
    }

    /**
     * 绘制当前页面
     */
    public void drawCurrentPage() {
        PageGenerater.generate(mPageElement, mRespository.getCurPage(), mAnimController.getCurrentBitmap());
        invalidate();
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
        //重新绘制封面扉页
        createCover();
        //需要重绘当前页面
        PageGenerater.generate(mPageElement, mRespository.getCurPage(), mAnimController.getCurrentBitmap());
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

        new AsyncTask<BaseChapterBean, Void, List<List<PageData>>>() {
            @Override
            protected List<List<PageData>> doInBackground(BaseChapterBean... chapters) {
                List<List<PageData>> lists = new ArrayList<>();
                lists.add(mPageManager.generatePages(chapters[0]));
                return lists;
            }

            @Override
            protected void onPostExecute(List<List<PageData>> lists) {
                super.onPostExecute(lists);
                mRespository.setCurPageList(lists.get(0));
                drawCurrentPage();
                invalidate();
            }
        }.execute(mCurChapter);

        invalidate();
    }

    /**
     * 如果是第一章 生成封面
     */
    private void createCover() {
        if (mCoverView == null) return;
        Bitmap coverBitmap = BitmapUtils.getCoverBitmap(mCoverView, getReaderBackgroundBitmap());
        mPageElement.setCoverBitmap(coverBitmap);
        invalidate();
    }

    /**
     * 封面View
     *
     * @param coverView
     */
    public void setCoverView(View coverView) {
        mCoverView = coverView;
        createCover();
    }


    /**
     * 设置文字间距 TODO 需要重新分页
     *
     * @param letterSpacing 文字间距
     */
    public void setLetterSpacing(int letterSpacing) {
        if (letterSpacing > IReaderConfig.LetterSpacing.MAX)
            letterSpacing = IReaderConfig.LetterSpacing.MAX;
        else if (letterSpacing < IReaderConfig.LetterSpacing.MIN)
            letterSpacing = IReaderConfig.LetterSpacing.MIN;
        mPageManager.setLetterSpacing(letterSpacing);
        dispose();
    }

    /**
     * 设置行间距 TODO 需要重新分页
     *
     * @param lineSpacing 行间距
     */
    public void setLineSpacing(int lineSpacing) {
        if (lineSpacing > IReaderConfig.LineSpacing.MAX)
            lineSpacing = IReaderConfig.LineSpacing.MAX;
        else if (lineSpacing < IReaderConfig.LineSpacing.MIN)
            lineSpacing = IReaderConfig.LineSpacing.MIN;
        mPageManager.setLineSpacing(lineSpacing);
        dispose();
    }

    /**
     * 设置文字间距 TODO 需要重新分页
     *
     * @param paragraphSpacing 段间距
     */
    public void setParagraphSpacing(int paragraphSpacing) {
        if (paragraphSpacing > IReaderConfig.ParagraphSpacing.MAX)
            paragraphSpacing = IReaderConfig.ParagraphSpacing.MAX;
        else if (paragraphSpacing < IReaderConfig.ParagraphSpacing.MIN)
            paragraphSpacing = IReaderConfig.ParagraphSpacing.MIN;
        mPageManager.setParagraphSpacing(paragraphSpacing);
        dispose();
    }

    private void dispose() {
        new AsyncTask<BaseChapterBean, Void, List<List<PageData>>>() {
            @Override
            protected List<List<PageData>> doInBackground(BaseChapterBean... chapters) {
                List<List<PageData>> lists = new ArrayList<>();
                lists.add(mPageManager.generatePages(chapters[0]));
//                lists.add(mPageManager.generatePages(chapters[1]));
//                lists.add(mPageManager.generatePages(chapters[2]));
                return lists;
            }

            @Override
            protected void onPostExecute(List<List<PageData>> lists) {
                super.onPostExecute(lists);
                mRespository.setCurPageList(lists.get(0));
                drawCurrentPage();
                invalidate();
            }
        }.execute(mCurChapter, mPreChapter, mNextChapter);

    }
}
