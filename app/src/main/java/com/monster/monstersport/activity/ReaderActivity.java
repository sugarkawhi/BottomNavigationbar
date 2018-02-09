package com.monster.monstersport.activity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.monster.monstersport.R;
import com.monster.monstersport.base.BaseActivity;
import com.monster.monstersport.bean.ChapterBean;
import com.monster.monstersport.bean.ChapterListBean;
import com.monster.monstersport.dao.bean.BookRecordBean;
import com.monster.monstersport.dialog.ReaderSettingDialog;
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
    }

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
                        if (record != null) {
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
        Observable<ChapterBean> cur = api.getChapterReadById(chapterId);
        Observable<ChapterBean> pre = api.getPreChapterReadById(chapterId);
        Observable<ChapterBean> next = api.getNextChapterReadById(chapterId);
        Observable.zip(cur, pre, next, new Function3<ChapterBean, ChapterBean, ChapterBean, ZipChapter>() {
            @Override
            public ZipChapter apply(ChapterBean cur, ChapterBean pre, ChapterBean next) throws Exception {
                ZipChapter zipChapter = new ZipChapter();
                zipChapter.cur = cur;
                zipChapter.pre = pre;
                zipChapter.next = next;
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
    }

    private void getNextChapter(String chapterId) {
        HttpUtils.getApiInstance()
                .getNextChapterReadById(chapterId)
                .compose(RxUtils.<ChapterBean>defaultSchedulers())
                .subscribe(new Observer<ChapterBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ChapterBean chapterBean) {
                        readerView.setNextChapter(chapterBean);
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
                .getPreChapterReadById(chapterId)
                .compose(RxUtils.<ChapterBean>defaultSchedulers())
                .subscribe(new Observer<ChapterBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ChapterBean chapterBean) {
                        readerView.setPreChapter(chapterBean);
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

}
