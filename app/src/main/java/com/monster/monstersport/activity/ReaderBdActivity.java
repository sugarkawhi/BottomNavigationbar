package com.monster.monstersport.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.baidu.tts.auth.AuthInfo;
import com.baidu.tts.chainofresponsibility.logger.LoggerProxy;
import com.baidu.tts.client.SpeechSynthesizeBag;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.monster.monstersport.R;
import com.monster.monstersport.base.BaseActivity;
import com.monster.monstersport.bean.BookBean;
import com.monster.monstersport.bean.ChapterBean;
import com.monster.monstersport.bean.ChapterListBean;
import com.monster.monstersport.bean.TtsBean;
import com.monster.monstersport.dao.bean.BookRecordBean;
import com.monster.monstersport.dialog.ReaderSettingDialog;
import com.monster.monstersport.dialog.ReaderSpacingDialog;
import com.monster.monstersport.dialog.ReaderTtsDialog;
import com.monster.monstersport.fragment.CatalogueFragment;
import com.monster.monstersport.http.BaseHttpResult;
import com.monster.monstersport.http.HttpUtils;
import com.monster.monstersport.http.RxUtils;
import com.monster.monstersport.http.api.IHyangApi;
import com.monster.monstersport.http.observer.DefaultObserver;
import com.monster.monstersport.persistence.HyReaderPersistence;
import com.monster.monstersport.util.OfflineResource;
import com.monster.monstersport.util.TimeFormatUtils;

import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function3;
import me.sugarkawhi.mreader.bean.BaseChapterBean;
import me.sugarkawhi.mreader.config.IReaderConfig;
import me.sugarkawhi.mreader.config.IReaderDirection;
import me.sugarkawhi.mreader.data.LetterData;
import me.sugarkawhi.mreader.data.PageData;
import me.sugarkawhi.mreader.listener.IReaderChapterChangeListener;
import me.sugarkawhi.mreader.listener.IReaderTouchListener;
import me.sugarkawhi.mreader.persistence.IReaderPersistence;
import me.sugarkawhi.mreader.utils.L;
import me.sugarkawhi.mreader.utils.ScreenUtils;
import me.sugarkawhi.mreader.view.ReaderView;

import static com.monster.monstersport.config.ReaderConfig.BaiduTts.APP_ID;
import static com.monster.monstersport.config.ReaderConfig.BaiduTts.APP_KEY;
import static com.monster.monstersport.config.ReaderConfig.BaiduTts.SECRET_KEY;
import static com.monster.monstersport.persistence.HyReaderPersistence.Background.COLOR_MATCHA;
import static com.monster.monstersport.persistence.HyReaderPersistence.Background.DEFAULT;
import static com.monster.monstersport.persistence.HyReaderPersistence.Background.IMAGE_BLUE;
import static com.monster.monstersport.persistence.HyReaderPersistence.Background.IMAGE_PURPLE;
import static com.monster.monstersport.persistence.HyReaderPersistence.Background.NIGHT;

/**
 * 阅读页
 * Created by ZhaoZongyao on 2018/1/12.
 */

public class ReaderBdActivity extends BaseActivity {

    public static final String TAG = "ReaderActivity";
    public static final String PARAM_STORY_ID = "PARAM_STORY_ID";
    private static final char[] TAIL_CHAR = {'，', '。', ';', '”', '！', '？', '\n', '…', '：'};
    private static final int ANIM_DURATION_BAR = 200;

    private String mStoryId;

    @BindView(R.id.app_bar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer_layout;
    @BindView(R.id.readerView)
    ReaderView readerView;
    @BindView(R.id.reader_seekBar_chapter)
    SeekBar readerSeekBarChapter;
    @BindView(R.id.reader_bottom)
    View readerBottomView;
    @BindView(R.id.tv_progress)
    TextView readerProgress;

    @BindView(R.id.tv_retry)
    TextView mTvRetry; //点击重试
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;//加载


    private int mAppBarHeight;
    private int readerBottomHeight;
    private boolean isShow;

    private int mScreenWidth, mScreenHeight;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ButterKnife.bind(this);
        mStoryId = getIntent().getStringExtra("storyid");
        init();
        addCatalog();
    }

    /**
     * 初始化操作
     */
    private void init() {
        mScreenWidth = ScreenUtils.getScreenWidth(this);
        mScreenHeight = ScreenUtils.getScreenHeight(this);
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        registerReaderReceiver();
        initReaderBar();
        initReaderView();
        getData();
    }

    /**
     * 初始化设置栏
     * TOP：1、添加书签 2、分享
     * BOTTOM： 1、目录 2、夜间/日间 3、翻页 4、设置
     */
    private void initReaderBar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mAppBarLayout.measure(0, 0);
        int statusBarHeight = ScreenUtils.getStatusBarHeight(this);
        mAppBarHeight = mAppBarLayout.getMeasuredHeight() + statusBarHeight;
        mAppBarLayout.setTranslationY(-mAppBarHeight);
        readerBottomView.measure(0, 0);
        readerBottomHeight = readerBottomView.getMeasuredHeight();
        readerBottomView.setTranslationY(readerBottomHeight);
        readerSeekBarChapter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                readerProgress.setText(progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                readerProgress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                float progress = seekBar.getProgress() / 100f;
                readerView.setChapterProgress(progress);
                readerProgress.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 初始化阅读器
     */
    private void initReaderView() {
        //设置背景颜色(ps:也设置对应字体颜色)
        int background = HyReaderPersistence.getBackground(this);
        initReaderBackground(background);
        readerView.timeChange(TimeFormatUtils.hhmm(System.currentTimeMillis()));
        readerView.setReaderTouchListener(new IReaderTouchListener() {
            @Override
            public boolean canTouch() {
                return !isShow;
            }

            @Override
            public void onTouchCenter() {
                if (isShow) hideReaderBar();
                else showReaderBar();
            }

            @Override
            public void onTouchSpeaking() {
                if (mReaderTtsDialog != null && mReaderTtsDialog.isShowing()) {
                    mReaderTtsDialog.dismiss();
                } else {
                    showTtsDialog();
                }
            }

        });

        readerView.setReaderChapterChangeListener(new IReaderChapterChangeListener() {
            @Override
            public void onChapterChange(BaseChapterBean curChapter, int direction) {
                if (curChapter == null) return;
                switch (direction) {
                    case IReaderDirection.NEXT:
                        getNextChapter(curChapter.getChapterid());
                        break;
                    case IReaderDirection.PRE:
                        getPreChapter(curChapter.getChapterid());
                        break;
                }
            }

            @Override
            public void onNoPrePage(BaseChapterBean curChapter) {
                showToast("WAIT : pre");
            }

            @Override
            public void onNoNextPage(BaseChapterBean curChapter) {
                showToast("WAIT : next");
            }

            @Override
            public void onReachFirstChapter() {
                showToast("已经是第一章了");
            }

            @Override
            public void onReachLastChapter() {
                showToast("已经是最后一章了");
            }

            @Override
            public void onProgressChange(float progress) {
                L.e("TAG", "onProgressChange > " + progress);
                readerSeekBarChapter.setProgress((int) (progress * 100));
            }

            @Override
            public void onChapterChangeError(int direction) {
                if (direction == IReaderDirection.NEXT) {
                    L.e("TAG", "onChapterChangeError > NEXT");
                } else {
                    L.e("TAG", "onChapterChangeError > PRE");
                }
            }
        });
    }

    // 接收电池信息更新的广播
    // 接收时间变化的广播
    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                int level = intent.getIntExtra("level", 0);
                Log.e(TAG, "ACTION_BATTERY_CHANGED  LEVEL=" + level);
                readerView.batteryChange(level);
            } else if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
                Log.e(TAG, Intent.ACTION_TIME_TICK);
                readerView.timeChange(TimeFormatUtils.hhmm(System.currentTimeMillis()));
            }
        }
    };

    /**
     * 注册阅读器广播接收器
     */
    private void registerReaderReceiver() {
        //注册广播接受器
        IntentFilter mfilter = new IntentFilter();
        mfilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        mfilter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(myReceiver, mfilter);
    }

    /**
     * 添加目录
     */
    private void addCatalog() {
        CatalogueFragment catalogueFragment = CatalogueFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString("storyid", mStoryId);
        catalogueFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, catalogueFragment)
                .commit();
    }


    /**
     * 初始化阅读器背景
     *
     * @param background 背景
     */
    private void initReaderBackground(int background) {
        try {
            Bitmap bitmap = null;
            InputStream is = null;
            int fontColor = Color.BLACK;
            int bgColor;
            switch (background) {
                case DEFAULT:
                    fontColor = ContextCompat.getColor(this, R.color.reader_font_default);
                    is = getAssets().open("background/kraft_paper_new.jpg");
                    break;
                case IMAGE_BLUE:
                    fontColor = ContextCompat.getColor(this, R.color.reader_font_blue);
                    is = getAssets().open("background/dandelion.jpg");
                    break;
                case IMAGE_PURPLE:
                    fontColor = ContextCompat.getColor(this, R.color.reader_font_purple);
                    is = getAssets().open("background/butterfly.jpg");
                    break;
                case NIGHT:
                    fontColor = ContextCompat.getColor(this, R.color.reader_font_night);
                    is = getAssets().open("background/alone.jpg");
                    break;
                case COLOR_MATCHA:
                    fontColor = ContextCompat.getColor(this, R.color.reader_font_matcha);
                    bgColor = ContextCompat.getColor(this, R.color.reader_bg_matcha);
                    bitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.RGB_565);
                    Canvas canvas = new Canvas(bitmap);
                    canvas.drawColor(bgColor);
            }
            if (is != null) {
                bitmap = BitmapFactory.decodeStream(is);
            }
            readerView.setReaderBackground(bitmap, fontColor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据 包括 书籍详情  章节列表 章节详情
     */
    private void getData() {
        getBookDetail();
        getChapterList();
    }


    /**
     * 获取书籍详情
     */
    private void getBookDetail() {
        HttpUtils.getApiInstance()
                .getLongStoryInfoByIdNew(mStoryId)
                .compose(RxUtils.<BaseHttpResult<BookBean>>defaultSchedulers())
                .subscribe(new DefaultObserver<BookBean>() {
                    @Override
                    protected void onSuccess(BookBean bookBean) {
                        readerView.setBookName(bookBean.getName());
                        generateCover(bookBean);
                    }
                });
    }

    /**
     * 获取章节
     */
    private void getChapterList() {
        mProgressBar.setVisibility(View.VISIBLE);
        mTvRetry.setVisibility(View.GONE);
        HttpUtils.getApiInstance()
                .searchChapterListVO(mStoryId)
                .compose(RxUtils.<BaseHttpResult<ChapterListBean>>defaultSchedulers())
                .subscribe(new DefaultObserver<ChapterListBean>() {
                    @Override
                    protected void onSuccess(ChapterListBean chapterListBean) {
                        BookRecordBean record = HyReaderPersistence.queryBookRecord(mStoryId);
                        if (record != null && record.getChapterId() != null) {
                            getChapterById(record.getChapterId(), record.getProgress());
                        } else {
                            getChapterById(chapterListBean.getDatas().get(0).getChapterid(), 0);
                        }
                    }

                    @Override
                    protected void onFail(BaseHttpResult<ChapterListBean> result) {
                        super.onFail(result);

                    }

                    @Override
                    protected void onException(ExceptionReason reason) {
                        super.onException(reason);
                        mProgressBar.setVisibility(View.GONE);
                        mTvRetry.setVisibility(View.VISIBLE);
                    }
                });

    }

    /**
     * 根据chapter id 来 获取章节内容
     *
     * @param chapterId 当前章节id
     */
    private void getChapterById(String chapterId, final float progress) {
        IHyangApi api = HttpUtils.getApiInstance();
        Observable<BaseHttpResult<ChapterBean>> cur = api.getChapterReadByIdV2(chapterId);
        Observable<BaseHttpResult<ChapterBean>> pre = api.getPreChapterReadByIdV2(chapterId);
        Observable<BaseHttpResult<ChapterBean>> next = api.getNextChapterReadByIdV2(chapterId);
        Observable.zip(cur, pre, next, new Function3<BaseHttpResult<ChapterBean>, BaseHttpResult<ChapterBean>, BaseHttpResult<ChapterBean>, ZipChapter>() {
            @Override
            public ZipChapter apply(BaseHttpResult<ChapterBean> result, BaseHttpResult<ChapterBean> result2, BaseHttpResult<ChapterBean> result3) throws Exception {
                ZipChapter zipChapter = new ZipChapter();
                zipChapter.cur = result.getData();
                zipChapter.pre = result2.getData();
                zipChapter.next = result3.getData();
                return zipChapter;
            }
        })
                .compose(RxUtils.<ZipChapter>defaultSchedulers())
                .subscribe(new Observer<ZipChapter>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ZipChapter zipChapter) {
                        readerView.setCurrentChapter(zipChapter.cur, progress);
                        readerView.setNextChapter(zipChapter.next);
                        readerView.setPreChapter(zipChapter.pre);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mProgressBar.setVisibility(View.GONE);
                        mTvRetry.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onComplete() {
                        mProgressBar.setVisibility(View.GONE);
                        mTvRetry.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * 获取章节下一章
     *
     * @param chapterId
     */
    private void getNextChapter(String chapterId) {
        HttpUtils.getApiInstance()
                .getNextChapterReadByIdV2(chapterId)
                .compose(RxUtils.<BaseHttpResult<ChapterBean>>defaultSchedulers())
                .subscribe(new DefaultObserver<ChapterBean>() {
                    @Override
                    protected void onSuccess(ChapterBean chapterBean) {
                        readerView.setNextChapter(chapterBean);
                    }
                });
    }

    /**
     * 获取章节的前一章
     *
     * @param chapterId 当前章节id
     */
    private void getPreChapter(String chapterId) {
        HttpUtils.getApiInstance()
                .getPreChapterReadByIdV2(chapterId)
                .compose(RxUtils.<BaseHttpResult<ChapterBean>>defaultSchedulers())
                .subscribe(new DefaultObserver<ChapterBean>() {
                    @Override
                    protected void onSuccess(ChapterBean chapterBean) {
                        readerView.setPreChapter(chapterBean);
                    }
                });
    }

    /**
     * 显示 APP_BAR 和 Bottom_Bar 和状态栏
     */
    private void showReaderBar() {
        isShow = true;
        mAppBarLayout.animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_BAR)
                .start();
        readerBottomView.animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_BAR)
                .start();
        showSystemUI();
    }

    /**
     * 隐藏 APP_BAR 和 Bottom_Bar 和 状态栏
     */
    private void hideReaderBar() {
        isShow = false;
        mAppBarLayout.animate()
                .translationY(-mAppBarHeight)
                .setDuration(ANIM_DURATION_BAR)
                .start();
        readerBottomView.animate()
                .translationY(readerBottomHeight)
                .setDuration(ANIM_DURATION_BAR)
                .start();
        hideSystemUI();
    }


    /**
     * 隐藏系统状态栏
     */
    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hideReaderBar status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    /**
     * 显示状态栏
     */
    private void showSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    /**
     * 点击重试
     */
    @OnClick(R.id.tv_retry)
    public void retry() {
        getChapterList();
    }

    /**
     * 上一章
     */
    @OnClick(R.id.reader_pre)
    public void preChapter() {
        readerView.directPreChapter();
    }

    /**
     * 下一章
     */
    @OnClick(R.id.reader_next)
    public void nextChapter() {
        readerView.directNextChapter();
    }

    /**
     * 点击设置 弹框
     */
    @OnClick(R.id.tv_setting)
    public void setting() {
        hideReaderBar();
        ReaderSettingDialog dialog = new ReaderSettingDialog(this);
        dialog.setSettingListener(new ReaderSettingDialog.IReaderSettingListener() {
            @Override
            public void onFontSizeChange(int fontSize) {
                readerView.setFontSize(fontSize);
            }

            @Override
            public void onBackgroundChange(int background) {
                initReaderBackground(background);
            }

            @Override
            public void onPageModeChange(int pageMode) {
                readerView.setPageMode(pageMode);
            }
        })
                .show();
        dialog.setOnDismissListener(mDismissListener);
    }


    /**
     * 设置间距
     */
    @OnClick(R.id.iv_spacing)
    public void setSpacing() {
        hideReaderBar();
        showSpacingDialog();
    }

    /**
     * 显示间距对话框
     */
    private void showSpacingDialog() {
        ReaderSpacingDialog dialog = new ReaderSpacingDialog(this);
        dialog.setLetterSpacing(10)
                .setLineSpacing(20)
                .setParagraphSpacing(30)
                .setSpacingChangeListener(new ReaderSpacingDialog.IReaderSpacingChangeListener() {
                    @Override
                    public void onLetterSpacingChange(int progress) {
                        int offset = IReaderConfig.LetterSpacing.MAX - IReaderConfig.LetterSpacing.MIN;
                        readerView.setLetterSpacing((int) (IReaderConfig.LetterSpacing.MIN + offset * progress / 100f));
                    }

                    @Override
                    public void onLineSpacingChange(int progress) {
                        int offset = IReaderConfig.LineSpacing.MAX - IReaderConfig.LineSpacing.MIN;
                        readerView.setLineSpacing((int) (IReaderConfig.LineSpacing.MIN + offset * progress / 100f));
                    }

                    @Override
                    public void onParagraphSpacingChange(int progress) {
                        int offset = IReaderConfig.ParagraphSpacing.MAX - IReaderConfig.ParagraphSpacing.MIN;
                        readerView.setParagraphSpacing((int) (IReaderConfig.ParagraphSpacing.MIN + offset * progress / 100f));
                    }
                })
                .show();
        dialog.setOnDismissListener(mDismissListener);
    }

    /**
     * 隐藏状态栏
     */
    private DialogInterface.OnDismissListener mDismissListener = new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
            showToast("HIDE SYS UI");
            hideSystemUI();
            //L.e(TAG,"OnDismissListener HIDE SYS UI");
        }
    };


    /**
     * 打开目录页
     */
    @OnClick(R.id.tv_catalog)
    public void openDrawer() {
        hideReaderBar();
        drawer_layout.openDrawer(Gravity.START);
    }


    @Override
    public void onBackPressed() {
        if (isShow) {
            hideReaderBar();
            return;
        }
        if (readerView.isSpeaking()) {
            stopTts();
            return;
        }
        saveReadingProgress();
        super.onBackPressed();
    }


    /**
     * 如果是第一章 生成封面
     */
    public void generateCover(BookBean book) {
        final View view = getLayoutInflater().inflate(R.layout.layout_reader_cover, null);
        final ImageView cover_img = view.findViewById(R.id.reader_cover_img);
        TextView cover_bookName = view.findViewById(R.id.reader_cover_bookName);
        TextView cover_authorName = view.findViewById(R.id.reader_cover_authorName);
        cover_bookName.setText(book.getName());
        cover_authorName.setText(book.getAuthorName());
        int width = ScreenUtils.getScreenWidth(this);
        int height = ScreenUtils.getScreenHeight(this);
        view.layout(0, 0, width, height);
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST);
        /** 当然，measure完后，并不会实际改变View的尺寸，需要调用View.layout方法去进行布局。
         * 按示例调用layout函数后，View的大小将会变成你想要设置成的大小。
         */
        view.measure(measuredWidth, measuredHeight);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        readerView.setCoverView(view);
        Glide.with(this)
                .load(book.getCover())
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        cover_img.setImageDrawable(resource);
                        readerView.setCoverView(view);
                    }
                });
    }

    private class ZipChapter {
        ChapterBean cur;
        ChapterBean pre;
        ChapterBean next;
    }

    /**
     * 保存阅读进度
     */
    private void saveReadingProgress() {
        BaseChapterBean chapter = readerView.getCurrentChapter();
        if (chapter == null) return;
        HyReaderPersistence.saveBookRecord(mStoryId, chapter.getChapterid(), readerView.getReadingProgress());
    }

    /****************************语音合成*****************************
     * **************************语音合成*****************************
     * **************************语音合成*****************************
     * **************************语音合成*****************************/

    //开启语音合成
    @OnClick(R.id.iv_tts)
    public void tts() {
        if (null == mSpeechSynthesizer) {
            initialTts();
        } else {
            startTts();
        }
        hideReaderBar();
    }

    /**
     * 开始语音合成
     */
    private void startTts() {
        PageData pageData = readerView.getCurrentPage();
        if (pageData == null) {
            showToast("当前页内容为空");
            return;
        }
        speak(pageData);
    }

    /**
     * 语音合成计算
     *
     * @param letters 所给字符
     */
    public List<TtsBean> calculate(List<LetterData> letters) {
        List<TtsBean> ttsList = new ArrayList<>();
        if (letters == null || letters.size() == 0) {
            return ttsList;
        }
        List<LetterData> letterList = new ArrayList<>();
        int utteranceId = 0;
        for (int i = 0; i < letters.size(); i++) {
            LetterData letter = letters.get(i);
            letterList.add(letter);
            char c = letter.getLetter();
            if (ArrayUtils.contains(TAIL_CHAR, c)) {
                TtsBean ttsBean = new TtsBean();
                ttsBean.setUtteranceId(utteranceId);
                ttsBean.setLetterList(new ArrayList<>(letterList));
                utteranceId++;
                letterList.clear();
                ttsList.add(ttsBean);
            }
        }
        if (letterList.size() > 0) {
            TtsBean ttsBean = new TtsBean();
            ttsBean.setUtteranceId(utteranceId);
            ttsBean.setLetterList(new ArrayList<>(letterList));
            ttsList.add(ttsBean);
        }

        for (TtsBean tts : ttsList) {
            Log.e(TAG, "utteranceId > " + tts.getUtteranceId() + " content > " + tts.getContent());
        }
        return ttsList;
    }


    // ================== 初始化参数设置开始 ==========================
    /**
     * 发布时请替换成自己申请的appId appKey 和 secretKey。注意如果需要离线合成功能,请在您申请的应用中填写包名。
     * 本demo的包名是com.baidu.tts.sample，定义在build.gradle中。
     */

    // TtsMode.MIX; 离在线融合，在线优先； TtsMode.ONLINE 纯在线； 没有纯离线
    protected TtsMode ttsMode = TtsMode.MIX;
    //
//    // 离线发音选择，VOICE_FEMALE即为离线女声发音。
    // assets目录下bd_etts_common_speech_m15_mand_eng_high_am-mix_v3.0.0_20170505.dat为离线男声模型；
    // assets目录下bd_etts_common_speech_f7_mand_eng_high_am-mix_v3.0.0_20170512.dat为离线女声模型

    // ===============初始化参数设置完毕，更多合成参数请至getParams()方法中设置 =================

    // 主控制类，所有合成控制方法从这个类开始

    protected SpeechSynthesizer mSpeechSynthesizer;

    /**
     * 初始化引擎，需要的参数均在InitConfig类里
     * <p>
     * DEMO中提供了3个SpeechSynthesizerListener的实现
     * MessageListener 仅仅用log.i记录日志，在logcat中可以看见
     * UiMessageListener 在MessageListener的基础上，对handler发送消息，实现UI的文字更新
     * FileSaveListener 在UiMessageListener的基础上，使用 onSynthesizeDataArrived回调，获取音频流
     */
    protected void initialTts() {
        //TODO 需要弹框表示正在初始化 并且不能取消 否则可能导致报错
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                LoggerProxy.printable(true); // 日志打印在logcat中
                // 设置初始化参数
                // appId appKey secretKey 网站上您申请的应用获取。注意使用离线合成功能的话，需要应用中填写您app的包名。包名在build.gradle中获取。
                mSpeechSynthesizer = SpeechSynthesizer.getInstance();
                mSpeechSynthesizer.setContext(ReaderBdActivity.this);
                mSpeechSynthesizer.setSpeechSynthesizerListener(mSynthesizerListener);
                mSpeechSynthesizer.auth(ttsMode);
                mSpeechSynthesizer.setAppId(APP_ID);
                mSpeechSynthesizer.setApiKey(APP_KEY, SECRET_KEY);

                if (true) {
                    // 授权检测接口(只是通过AuthInfo进行检验授权是否成功。选择纯在线可以不必调用auth方法。
                    AuthInfo authInfo = mSpeechSynthesizer.auth(ttsMode);
                    if (!authInfo.isSuccess()) {
                        // 离线授权需要网站上的应用填写包名。本demo的包名是com.baidu.tts.sample，定义在build.gradle中
                        String errorMsg = authInfo.getTtsError().getDetailMessage();
                    }
                }
                setParams();
                // 初始化tts TODO 这儿报错的可能 在初始化Tts的时候销毁了activity 会空指针
                int result = mSpeechSynthesizer.initTts(ttsMode);
                if (result != 0) {
                    e.onError(new Throwable("【error】initTts 初始化失败 + errorCode：" + result));
                } else {
                    // 此时可以调用 speak和synthesize方法
                    e.onNext("合成引擎初始化成功,此时可以调用 speak和synthesize方法");
                    e.onComplete();
                }

            }
        }).compose(RxUtils.<String>defaultSchedulers())
                .compose(this.<String>bindToLifecycle())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.e(TAG, "s > " + s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        startTts();
                    }
                });

    }

    private SpeechSynthesizerListener mSynthesizerListener = new SpeechSynthesizerListener() {
        @Override
        public void onSynthesizeStart(String s) {

        }

        @Override
        public void onSynthesizeDataArrived(String s, byte[] bytes, int i) {

        }

        @Override
        public void onSynthesizeFinish(String s) {

        }

        @Override
        public void onSpeechStart(String utteranceId) {

        }

        @Override
        public void onSpeechProgressChanged(String utteranceId, int i) {
            final int index = Integer.parseInt(utteranceId);
            if (mTtsList == null || index >= mTtsList.size())
                return;
            final List<LetterData> letterDataList = mTtsList.get(index).getLetterList();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    readerView.setTtsLetters(letterDataList);
                }
            });
        }

        @Override
        public void onSpeechFinish(String utteranceId) {
            final int index = Integer.parseInt(utteranceId);
            if (index == mTtsList.size() - 1) {//最后一段读完了 切换到下一页
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        PageData pageData = readerView.ttsNextPage();
                        speak(pageData);
                    }
                });
            }
        }

        @Override
        public void onError(String s, com.baidu.tts.client.SpeechError speechError) {

        }
    };

    public void setParams() {
        // 以下参数均为选填
        // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, String.valueOf(IReaderPersistence.getTtsSpeaker(this)));
        // 设置合成的音量，0-9 ，默认 5
        // mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "5");
        // 设置合成的语速，0-9 ，默认 5
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, String.valueOf(IReaderPersistence.getTtsSpeed(this)));
        // 设置合成的语调，0-9 ，默认 5
        // mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, "5");

        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI);
        // 该参数设置为TtsMode.MIX生效。即纯在线模式不生效。
        // MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线

        // 离线资源文件
        OfflineResource offlineResource = createOfflineResource(IReaderPersistence.getTtsSpeaker(this));
        // 声学模型文件路径 (离线引擎使用), 请确认下面两个文件存在
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, offlineResource.getTextFilename());
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, offlineResource.getModelFilename());


    }


    protected OfflineResource createOfflineResource(int speaker) {
        OfflineResource offlineResource = null;
        try {
            offlineResource = new OfflineResource(this, speaker);
        } catch (IOException e) {
            // IO 错误自行处理
            e.printStackTrace();
            Log.e(TAG, "【error】:copy files from assets failed." + e.getMessage());
        }
        return offlineResource;
    }

    //语音合成列表
    private List<TtsBean> mTtsList;

    /**
     * speak 实际上是调用 synthesize后，获取音频流，然后播放。
     * 获取音频流的方式见SaveFileActivity及FileSaveListener
     * 需要合成的文本text的长度不能超过1024个GBK字节。
     */
    private void speak(PageData pageData) {
        if (pageData == null) {
            readerView.stopTts();
            return;
        }
        readerView.startTts();
        mTtsList = calculate(pageData.getLetters());
        // 合成前可以修改参数：
        // Map<String, String> params = getParams();
        // synthesizer.setParams(params);
        List<Pair<String, Integer>> texts = new ArrayList<>();

        int count = 0;
        for (TtsBean ttsBean : mTtsList) {
            texts.add(new Pair<>(ttsBean.getContent(), count++));
        }

        List<SpeechSynthesizeBag> bags = new ArrayList<>();
        for (Pair<String, Integer> pair : texts) {
            SpeechSynthesizeBag speechSynthesizeBag = new SpeechSynthesizeBag();
            speechSynthesizeBag.setText(pair.first);
            if (pair.second != null) {
                speechSynthesizeBag.setUtteranceId(pair.second + "");
            }
            bags.add(speechSynthesizeBag);

        }
        mSpeechSynthesizer.batchSpeak(bags);
    }


    /**
     * 切换离线发音。注意需要添加额外的判断：引擎在合成时该方法不能调用
     */
    private void loadModel(int speaker) {
        OfflineResource offlineResource = createOfflineResource(speaker);
        int result = mSpeechSynthesizer.loadModel(offlineResource.getModelFilename(), offlineResource.getTextFilename());
        checkResult(result, "loadModel");
    }

    private void checkResult(int result, String method) {
        if (result != 0) {
            Log.e(TAG, "error code :" + result + " method:" + method + ", 错误码文档:http://yuyin.baidu.com/docs/tts/122 ");
        }
    }


    /**
     * 暂停播放。仅调用speak后生效
     */
    private void pauseTts() {
        if (mSpeechSynthesizer != null) {
            int result = mSpeechSynthesizer.pause();
            checkResult(result, "pause");
        }
    }

    /**
     * 继续播放。仅调用speak后生效，调用pause生效
     */
    private void resumeTts() {
        if (mSpeechSynthesizer != null) {
            int result = mSpeechSynthesizer.resume();
            checkResult(result, "resume");
        }
    }

    /*
     * 停止合成引擎。即停止播放，合成，清空内部合成队列。
     */
    private void stopTts() {
        if (mSpeechSynthesizer != null) {
            int result = mSpeechSynthesizer.stop();
            checkResult(result, "stop");
        }
        if (mReaderTtsDialog != null && mReaderTtsDialog.isShowing())
            mReaderTtsDialog.dismiss();
        readerView.stopTts();
    }


    @Override
    protected void onPause() {
        super.onPause();
        pauseTts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeTts();
        hideSystemUI();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSpeechSynthesizer != null) {
            int result = mSpeechSynthesizer.release();
            Log.e(TAG, "释放资源成功");
        }
        if (myReceiver != null) {
            unregisterReceiver(myReceiver);
        }
    }


    private ReaderTtsDialog mReaderTtsDialog;

    /**
     * 显示语音合成设置Dialog
     */
    private void showTtsDialog() {
        mReaderTtsDialog = new ReaderTtsDialog(this);
        mReaderTtsDialog.setTtsSpeed(IReaderPersistence.getTtsSpeed(this))
                .setTtsSpeaker(IReaderPersistence.getTtsSpeaker(this))
                .setListener(new ReaderTtsDialog.IReaderTtsChangeListener() {
                    @Override
                    public void onTtsSpeedChange(int speed) {
                        IReaderPersistence.saveTtsSpeed(ReaderBdActivity.this, speed);
                        //语速变了 TODO 需要重新合成
                        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, String.valueOf(speed));
                    }

                    @Override
                    public void onTtsSpeakerChange(int speaker) {
                        IReaderPersistence.saveTtsSpeaker(ReaderBdActivity.this, speaker);
                        //发音人变了 TODO  需要重新合成
                        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, String.valueOf(speaker));
                    }


                    @Override
                    public void onTtsExit() {
                        stopTts();
                    }
                });
        mReaderTtsDialog.show();
        mReaderTtsDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                hideSystemUI();
                mSpeechSynthesizer.resume();//恢复朗读
            }
        });
        mSpeechSynthesizer.pause();
    }


}
