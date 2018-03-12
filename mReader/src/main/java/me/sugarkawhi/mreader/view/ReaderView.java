package me.sugarkawhi.mreader.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

import me.sugarkawhi.mreader.R;
import me.sugarkawhi.mreader.anim.CoverAnimController;
import me.sugarkawhi.mreader.anim.NoneAnimController;
import me.sugarkawhi.mreader.anim.PageAnimController;
import me.sugarkawhi.mreader.anim.SlideAnimController;
import me.sugarkawhi.mreader.bean.BaseChapterBean;
import me.sugarkawhi.mreader.config.IReaderConfig;
import me.sugarkawhi.mreader.data.LetterData;
import me.sugarkawhi.mreader.data.PageData;
import me.sugarkawhi.mreader.element.PageElement;
import me.sugarkawhi.mreader.listener.IReaderChapterChangeListener;
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
    private static final String TAG = "MReaderView";
    //语音合成播放
    private boolean isSpeaking = false;

    //加载中
    private static final int STATE_LOADING = 1;
    //打开书籍成功
    private static final int STATE_OPEN = 2;
    //当前状态
    private int mCurrentState = STATE_LOADING;

    //生成页面
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

    private Handler mHandler;

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
        int pageMode = IReaderPersistence.getPageMode(getContext());
        setPageMode(pageMode);
        mHandler = new Handler();
    }

    /**
     * 设置翻页模式
     * {@link #(IReaderConfig.PageMode.COVER)} 覆盖模式
     * {@link #(IReaderConfig.PageMode.SLIDE)}  平移模式
     * {@link #(IReaderConfig.PageMode.NONE)}  无动画
     * {@link #(IReaderConfig.PageMode.SIMULATION)} 仿真
     *
     * @param mode 翻页模式
     */
    public void setPageMode(int mode) {
        switch (mode) {
            case IReaderConfig.PageMode.COVER:
                mAnimController = new CoverAnimController(this, mWidth, mHeight, mPageElement, mPageChangeListener);
                break;
            case IReaderConfig.PageMode.SLIDE:
                mAnimController = new SlideAnimController(this, mWidth, mHeight, mPageElement, mPageChangeListener);
                break;
            case IReaderConfig.PageMode.NONE:
            default:
                mAnimController = new NoneAnimController(this, mWidth, mHeight, mPageElement, mPageChangeListener);
                break;
        }
        IReaderPersistence.savePageMode(getContext(), mode);
        mAnimController.setIReaderTouchListener(mReaderTouchListener);
    }

    private PageAnimController.IPageChangeListener mPageChangeListener = new PageAnimController.IPageChangeListener() {

        @Override
        public void onCancel(int direction) {
            mRespository.cancel(direction);
        }

        @Override
        public void onSelectPage(int direction, boolean isCancel) {
            mRespository.onSelectPage(direction, isCancel);
            drawCurrentPage();
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
        if (isSpeaking) {
            if (mReaderTouchListener != null)
                mReaderTouchListener.onTouchSpeaking();
            return false;
        }
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

    /**
     * mRespository
     *
     * @param readerChapterChangeListener
     */
    public void setReaderChapterChangeListener(IReaderChapterChangeListener readerChapterChangeListener) {
        mRespository.setChapterChangeListener(readerChapterChangeListener);
    }

    public void setTime(String time) {
        mPageElement.setTime(time);

        invalidate();
    }

    public void setElectric(float electric) {
        mPageElement.setBatteryLevel(electric);
    }


    /**
     * 设置当前章节
     * 阅读器维护一个页面的队列
     *
     * @param curChapter 当前章节
     * @param progress   进度
     */
    public void setCurrentChapter(final BaseChapterBean curChapter, final float progress) {
        mRespository.setCurChapter(curChapter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<PageData> list = mPageManager.generatePages(curChapter);
                mCurrentState = STATE_OPEN;
                mRespository.setCurPageList(list);
                mRespository.setCurPage(progress);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        drawCurrentPage();
                    }
                });
            }
        }).start();
    }

    /**
     * 设置下一章节
     * todo 判断是否是下一章 准确判断
     *
     * @param nextChapter 当前章节的下一章
     */
    public void setNextChapter(final BaseChapterBean nextChapter) {
        mRespository.setNextChapter(nextChapter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<PageData> list = mPageManager.generatePages(nextChapter);
                mRespository.setNextPageList(list);
            }
        }).start();
    }

    /**
     * 设置当前章节
     */
    public void setPreChapter(final BaseChapterBean preChapter) {
        mRespository.setPreChapter(preChapter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<PageData> list = mPageManager.generatePages(preChapter);
                mRespository.setPrePageList(list);
            }
        }).start();
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
        drawCurrentPage();
    }


    /**
     * 设置文字大小
     * TODO 1.需要重新分页2.分页后保持当前进度
     */
    public void setFontSize(int fontSize) {
        if (fontSize < IReaderConfig.FontSize.MIN) {
            L.w(TAG, "font size is too small");
            return;
        }
        if (fontSize > IReaderConfig.FontSize.MAX) {
            L.w(TAG, "font size is too large");
            return;
        }
        IReaderPersistence.saveFontSize(getContext(), fontSize);
        mContentPaint.setTextSize(fontSize);
        mChapterNamePaint.setTextSize(fontSize * IReaderConfig.RATIO_CHAPTER_CONTENT);
        //需要将 当前、前、后 章节重新分页
        dispose();
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

    /**
     * 重新刷新
     */
    private void dispose() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<PageData> list = mPageManager.generatePages(mRespository.getCurChapter());
                mRespository.setCurPageList(list);
                float curProgress = mRespository.getReadingProgress();
                mRespository.setCurPage(curProgress);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        drawCurrentPage();
                    }
                });
                List<PageData> preList = mPageManager.generatePages(mRespository.getPreChapter());
                mRespository.setPrePageList(preList);
                List<PageData> nextList = mPageManager.generatePages(mRespository.getNextChapter());
                mRespository.setNextPageList(nextList);
            }
        });
        thread.start();
    }

    /**
     * 设置当前章节的进度
     * 1.首先要知道当前章节的总页数
     * 2.用总页数乘以这百分比 得出当前页数
     *
     * @param progress 进度 >=0 并且<=1
     */
    public void setChapterProgress(float progress) {
        mRespository.setChapterProgress(progress);
        drawCurrentPage();
    }

    /**
     * 直接跳转到下一章
     */
    public void directNextChapter() {
        mRespository.directNextChapter();
        drawCurrentPage();
    }

    /**
     * 直接跳转到上一章
     */
    public void directPreChapter() {
        mRespository.directPreChapter();
        drawCurrentPage();
    }


    /**
     * 设置书名 用于绘制章节首页头部
     *
     * @param bookName 书名
     */
    public void setBookName(String bookName) {
        mPageManager.setBookName(bookName);
    }

    /**
     * 获取当前的chapter
     * 为了保存当前进度
     */
    public BaseChapterBean getCurrentChapter() {
        return mRespository.getCurChapter();
    }

    /**
     * 获取当前阅读进度
     *
     * @return 章节阅读进度
     */
    public float getReadingProgress() {
        return mRespository.getReadingProgress();
    }

    /**
     * 获取当前页
     *
     * @return
     */
    public PageData getCurrentPage() {
        return mRespository.getCurPage();
    }


    public PageData getNextPage() {
        return mRespository.getNextPage();
    }

    //开始
    private int tmpBeginPos = -1;

    /**
     * 设置语音合成进度
     */
    public void setTtsProgress(int beginPos, int endPos) {
        if (tmpBeginPos == beginPos) return;
        tmpBeginPos = beginPos;
        mPageElement.setTtsProgress(beginPos, endPos);
        drawCurrentPage();
    }

    /**
     * 设置语音合成进度
     */
    public void setTtsLetters(List<LetterData> list) {
        mPageElement.setTtsLetters(list);
        drawCurrentPage();
    }

    /**
     * 语音合成
     * 自动翻到下一页
     * TODO 暂时没有动画
     */
    public PageData ttsNextPage() {
        PageData nextPage = mRespository.nextPage();
        if (nextPage != null) {
            drawCurrentPage();
        }
        return nextPage;
    }


    public boolean isSpeaking() {
        return isSpeaking;
    }

    public void setSpeaking(boolean speaking) {
        isSpeaking = speaking;
    }

    /**
     * 开始语音合成
     * 设置标志位
     */
    public void startTts() {
        isSpeaking = true;
    }

    /**
     * 退出语音合成
     */
    public void stopTts() {
        isSpeaking = false;
        mPageElement.stopTts();
        drawCurrentPage();
    }

    /**
     * 电量变化
     * level为整数 PageElement需要使用百分数
     * TODO  实时更新到页面
     *
     * @param level 电量
     */
    public void batteryChange(int level) {
        mPageElement.setBatteryLevel(level / 100f);
    }

    /**
     * 当前时间变化
     * TODO 实时更新到页面
     */
    public void timeChange(String time) {
        mPageElement.setTime(time);
    }
}
