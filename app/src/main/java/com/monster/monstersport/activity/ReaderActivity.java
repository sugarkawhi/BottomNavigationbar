package com.monster.monstersport.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.sunflower.FlowerCollector;
import com.monster.monstersport.R;
import com.monster.monstersport.base.BaseActivity;
import com.monster.monstersport.bean.ChapterBean;
import com.monster.monstersport.bean.ChapterListBean;
import com.monster.monstersport.dao.bean.BookRecordBean;
import com.monster.monstersport.dialog.ReaderSettingDialog;
import com.monster.monstersport.dialog.ReaderTtsDialog;
import com.monster.monstersport.dialog.SpacingSettingDialog;
import com.monster.monstersport.fragment.CatalogueFragment;
import com.monster.monstersport.http.BaseHttpResult;
import com.monster.monstersport.http.HttpUtils;
import com.monster.monstersport.http.RxUtils;
import com.monster.monstersport.http.api.IHyangApi;
import com.monster.monstersport.http.observer.DefaultObserver;
import com.monster.monstersport.persistence.HyReaderPersistence;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function3;
import me.sugarkawhi.mreader.bean.BaseChapterBean;

import com.monster.monstersport.bean.BookBean;

import me.sugarkawhi.mreader.config.IReaderConfig;
import me.sugarkawhi.mreader.config.IReaderDirection;
import me.sugarkawhi.mreader.data.PageData;
import me.sugarkawhi.mreader.listener.IReaderChapterChangeListener;
import me.sugarkawhi.mreader.listener.IReaderTouchListener;
import me.sugarkawhi.mreader.utils.L;
import me.sugarkawhi.mreader.utils.ScreenUtils;
import me.sugarkawhi.mreader.view.ReaderView;

import static com.monster.monstersport.persistence.HyReaderPersistence.Background.COLOR_MATCHA;
import static com.monster.monstersport.persistence.HyReaderPersistence.Background.DEFAULT;
import static com.monster.monstersport.persistence.HyReaderPersistence.Background.IMAGE_BLUE;
import static com.monster.monstersport.persistence.HyReaderPersistence.Background.IMAGE_PURPLE;
import static com.monster.monstersport.persistence.HyReaderPersistence.Background.NIGHT;

/**
 * 阅读页
 * Created by ZhaoZongyao on 2018/1/12.
 */

public class ReaderActivity extends BaseActivity {

    public static final String TAG = "ReaderActivity";
    public static final String PARAM_STORY_ID = "PARAM_STORY_ID";

    private String mStoryId;

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

    private int readerBottomHeight;
    private boolean isShow;

    private int mScreenWidth, mScreenHeight;
    //语音合成
    // 语音合成对象
    private SpeechSynthesizer mTts;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    //
    // 默认发音人
    private String voicer = "vixf";

    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_reader);
        ButterKnife.bind(this);
        mStoryId = getIntent().getStringExtra("storyid");
        init();
        readerView.setBookName("主播的致命诱惑");

        readerView.setReaderTouchListener(new IReaderTouchListener() {
            @Override
            public boolean canTouch() {
                return !isShow;
            }

            @Override
            public void onTouchCenter() {
                if (isShow) hide();
                else show();
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
                showToast("网络问题，没有上一页了，稍后重试");

            }

            @Override
            public void onNoNextPage(BaseChapterBean curChapter) {
                showToast("网络问题，没有下一页了，稍后重试");
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
        });
        hideSystemUI();
        addCatalog();
        initTts();
    }

    private void initTts() {
        mTts = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);
        mSharedPreferences = getSharedPreferences("com.monster.setting", MODE_PRIVATE);
    }

    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.e(TAG, "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                Log.e(TAG, "初始化失败,错误码：" + code);
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };

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
     * 初始化readerView
     */
    private void initReaderView() {
        //设置背景颜色(ps:也设置对应字体颜色)
        int background = HyReaderPersistence.getBackground(this);
        initReaderBackground(background);
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

    private void init() {
        mScreenWidth = ScreenUtils.getScreenWidth(this);
        mScreenHeight = ScreenUtils.getScreenHeight(this);
        initReaderView();
        generateCover();
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
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
        getChapterList();
    }

    /**
     * 获取章节
     */
    private void getChapterList() {
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
                });

    }

    /**
     * 根据chapter id 来 获取章节内容
     *
     * @param chapterId
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

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        HttpUtils.getApiInstance()
                .getChapterReadByIdV2(chapterId)
                .compose(RxUtils.<BaseHttpResult<ChapterBean>>defaultSchedulers())
                .subscribe(new Observer<BaseHttpResult<ChapterBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseHttpResult<ChapterBean> result) {
                        readerView.setCurrentChapter(result.getData(), 0);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void getNextChapter(String chapterId) {
        HttpUtils.getApiInstance()
                .getNextChapterReadByIdV2(chapterId)
                .compose(RxUtils.<BaseHttpResult<ChapterBean>>defaultSchedulers())
                .subscribe(new Observer<BaseHttpResult<ChapterBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseHttpResult<ChapterBean> chapterBean) {
                        readerView.setNextChapter(chapterBean.getData());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getPreChapter(String chapterId) {
        HttpUtils.getApiInstance()
                .getPreChapterReadByIdV2(chapterId)
                .compose(RxUtils.<BaseHttpResult<ChapterBean>>defaultSchedulers())
                .subscribe(new Observer<BaseHttpResult<ChapterBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseHttpResult<ChapterBean> chapterBean) {
                        readerView.setPreChapter(chapterBean.getData());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void show() {
        isShow = true;
        readerBottomView.animate()
                .translationY(0)
                .setDuration(200)
                .start();
    }

    private void hide() {
        isShow = false;
        readerBottomView.animate()
                .translationY(readerBottomHeight)
                .setDuration(200)
                .start();
        hideSystemUI();
    }


    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
    }

    /**
     * 隐藏菜单。沉浸式阅读
     */
    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    private void showSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
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
        hide();
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
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                hideSystemUI();
            }
        });
    }


    private void showSpacingDialog() {
        SpacingSettingDialog dialog = new SpacingSettingDialog(this);
        dialog.setLetterSpacing(10)
                .setLineSpacing(20)
                .setParagraphSpacing(30)
                .setSpacingChangeListener(new SpacingSettingDialog.IReaderSpacingChangeListener() {
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
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                hideSystemUI();
            }
        });
    }


    @OnClick(R.id.tv_catalog)
    public void openDrawer() {
        hide();
        drawer_layout.openDrawer(Gravity.START);
    }


    @Override
    public void onBackPressed() {
        if (isShow) {
            hide();
            return;
        }
        saveReadingProgress();
        super.onBackPressed();
    }

    public void generateCover() {
        BookBean bookBean = new BookBean();
        bookBean.setName("战略级天使");
        bookBean.setAuthorName("白伯欢");
        generateCover(bookBean);
    }


    /**
     * 如果是第一章 生成封面
     */
    public void generateCover(BookBean book) {
        View view = getLayoutInflater().inflate(R.layout.layout_reader_cover, null);
        ImageView cover_img = view.findViewById(R.id.reader_cover_img);
        TextView cover_bookName = view.findViewById(R.id.reader_cover_bookName);
        TextView cover_authorName = view.findViewById(R.id.reader_cover_authorName);
        cover_img.setImageResource(R.drawable.cover_zljts);
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

    //语音合成
    @OnClick(R.id.iv_tts)
    public void tts() {
        PageData pageData = readerView.getCurrentPage();
        if (pageData == null) {
            showToast("当前页内容为空");
            return;
        }
        hide();

        String content = pageData.getContent();
        // 设置参数
        setParam();
        startTts(content);
    }

    /**
     * 开始语音合成
     *
     * @param content
     */
    private void startTts(String content) {
        readerView.setSpeaking(true);
        // 移动数据分析，收集开始合成事件
        FlowerCollector.onEvent(this, "tts_play");
        int code = mTts.startSpeaking(content, mTtsListener);
        if (code != ErrorCode.SUCCESS) {
            showToast("语音合成失败,错误码: " + code);
        }
    }

    /**
     * 参数设置
     *
     * @return
     */
    private void setParam() {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
            //设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, mSharedPreferences.getString("speed_preference", "100"));
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, mSharedPreferences.getString("pitch_preference", "50"));
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, mSharedPreferences.getString("volume_preference", "50"));
        } else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            // 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");
            /**
             * TODO 本地合成不设置语速、音调、音量，默认使用语记设置
             * 开发者如需自定义参数，请参考在线合成参数设置
             */
        }
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, mSharedPreferences.getString("stream_preference", "3"));
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");
    }

    // 缓冲进度
    private int mPercentForBuffering = 0;
    // 播放进度
    private int mPercentForPlaying = 0;
    // 开始的索引位置
    private int mBeginPos;
    // 切换发音人拦截位置 TODO  在播放完成时 要置0
    private int mInterceptPos;

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            showToast("开始播放");
        }

        @Override
        public void onSpeakPaused() {
            showToast("暂停播放");
        }

        @Override
        public void onSpeakResumed() {
            showToast("继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
            mPercentForBuffering = percent;
        }

        @SuppressLint("DefaultLocale")
        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
            int newBeginPos = beginPos + mInterceptPos;
            int newEndPos = endPos + mInterceptPos;
            mBeginPos = newBeginPos;
            mPercentForPlaying = percent;
            //设置
            readerView.setTtsProgress(percent, newBeginPos, newEndPos);

//            showToast(String.format("缓存进度%d%%,播放进度%d%%", mPercentForBuffering, mPercentForPlaying)
//                    + " beginPos=" + beginPos
//                    + " endPos=" + endPos);
        }

        @Override
        public void onCompleted(SpeechError error) {
            mInterceptPos = 0;
            if (error == null) {
                showToast("播放完成");
                PageData nextPage = readerView.ttsNextPage();
                if (nextPage != null) {
                    startTts(nextPage.getContent());
                } else {
                    showToast("无下一页");
                }
            } else {
                showToast(error.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    private ReaderTtsDialog mReaderTtsDialog;

    private void showTtsDialog() {
        mReaderTtsDialog = new ReaderTtsDialog(this);
        mReaderTtsDialog.setListener(new ReaderTtsDialog.IReaderTtsChangeListener() {
            @Override
            public void onTtsTypeChange(String ttsType) {

            }

            @Override
            public void onTtsVoicerChange(String ttsType, String voicer) {
                ReaderActivity.this.voicer = voicer;
                // 设置在线合成发音人
                mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
                String content = readerView.getCurrentPage().getContent();
                mInterceptPos = mBeginPos;
                String lastContent = content.substring(mInterceptPos);
                mTts.startSpeaking(lastContent, mTtsListener);
            }

            @Override
            public void onTtsExit() {
                mTts.stopSpeaking();
                readerView.stopTts();
                mReaderTtsDialog.dismiss();
                readerView.setSpeaking(false);
            }
        });
        mReaderTtsDialog.show();
        mReaderTtsDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                hide();
                if (mTts.isSpeaking()) {
                    mTts.resumeSpeaking();//恢复朗读
                }
            }
        });
        mTts.pauseSpeaking();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != mTts) {
            mTts.stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
        }
    }
}
