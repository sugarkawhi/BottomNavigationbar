package com.monster.monstersport.activity;

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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.monster.monstersport.R;
import com.monster.monstersport.base.BaseActivity;
import com.monster.monstersport.dialog.BottomSheetDialog;
import com.monster.monstersport.persistence.HyReaderPersistence;
import com.monster.monstersport.util.Constant;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import me.sugarkawhi.mreader.bean.BookBean;
import me.sugarkawhi.mreader.bean.ChapterBean;
import me.sugarkawhi.mreader.listener.IReaderTouchListener;
import me.sugarkawhi.mreader.persistence.IReaderPersistence;
import me.sugarkawhi.mreader.utils.ScreenUtils;
import me.sugarkawhi.mreader.view.ReaderView;

import static com.monster.monstersport.persistence.HyReaderPersistence.Background.COLOR_MATCHA;
import static com.monster.monstersport.persistence.HyReaderPersistence.Background.DEFAULT;
import static com.monster.monstersport.persistence.HyReaderPersistence.Background.IMAGE_BLUE;
import static com.monster.monstersport.persistence.HyReaderPersistence.Background.IMAGE_PURPLE;

/**
 * 阅读页
 * Created by ZhaoZongyao on 2018/1/12.
 */

public class ReaderActivity extends BaseActivity {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer_layout;
    @BindView(R.id.iv_drawer)
    ImageView ivDrawer;
    @BindView(R.id.readerView)
    ReaderView readerView;
    @BindView(R.id.reader_seekBar)
    SeekBar readerSeekBar;
    @BindView(R.id.reader_bottom)
    View readerBottomView;

    @BindView(R.id.reader_fontSize)
    TextView readerFontSize;
    private int readerBottomHeight;
    private boolean isShow;

    private int mScreenWidth, mScreenHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_reader);
        ButterKnife.bind(this);
        init();

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
            public boolean onTouchRight() {
                return false;
            }

            @Override
            public boolean onTouchLeft() {
                return false;
            }
        });
        hideSystemUI();
    }

    /**
     * 初始化readerView
     */
    private void initReaderView() {
        //当前文字大小
        int fontSize = HyReaderPersistence.getFontSize(this);
        readerFontSize.setText(String.valueOf(fontSize));
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
        readerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                readerView.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        setChapter();
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
     * <p>
     * TODO 章节划分
     */
    @OnClick(R.id.reader_pre)
    public void preChapter() {

    }

    /**
     * 下一章
     * TODO 章节划分
     */
    @OnClick(R.id.reader_next)
    public void nextChapter() {

    }

    /**
     * 文字变大
     */
    @OnClick(R.id.reader_font_big)
    public void fontBig() {
        int currentFont = IReaderPersistence.getFontSize(this);
        currentFont++;
        IReaderPersistence.saveFontSize(this, currentFont);
        readerView.setFontSize(currentFont);
        readerFontSize.setText(String.valueOf(currentFont));
    }

    @OnClick(R.id.tv_setting)
    public void setting() {
        hide();
    }

    /**
     * 文字变小
     */
    @OnClick(R.id.reader_font_small)
    public void fontSmall() {
        int currentFont = IReaderPersistence.getFontSize(this);
        currentFont--;
        IReaderPersistence.saveFontSize(this, currentFont);
        readerView.setFontSize(currentFont);
        readerFontSize.setText(String.valueOf(currentFont));
    }

    /**
     * 选择背景
     */
    @OnCheckedChanged({R.id.reader_bg_blue, R.id.reader_bg_purple, R.id.reader_bg_default, R.id.reader_bg_matcha})
    public void selectBg(CompoundButton view, boolean isChecked) {
        if (!isChecked) return;
        switch (view.getId()) {
            case R.id.reader_bg_blue:
                initReaderBackground(IMAGE_BLUE);
                HyReaderPersistence.saveBackground(this, IMAGE_BLUE);
                break;
            case R.id.reader_bg_purple:
                initReaderBackground(IMAGE_PURPLE);
                HyReaderPersistence.saveBackground(this, IMAGE_PURPLE);
                break;
            case R.id.reader_bg_default:
                initReaderBackground(DEFAULT);
                HyReaderPersistence.saveBackground(this, DEFAULT);
                break;
            case R.id.reader_bg_matcha:
                initReaderBackground(COLOR_MATCHA);
                HyReaderPersistence.saveBackground(this, COLOR_MATCHA);
                break;
        }
    }

    @OnClick(R.id.tv_catalog)
    public void openDrawer() {
        hide();
        drawer_layout.openDrawer(Gravity.START);
    }

    /**
     * 选择翻页模式
     */
    @OnCheckedChanged({R.id.reader_mode_cover, R.id.reader_mode_slide, R.id.reader_mode_simulation, R.id.reader_mode_none})
    public void selectMode(CompoundButton view, boolean isChecked) {
        switch (view.getId()) {
            case R.id.reader_mode_cover:
                if (isChecked) showToast("reader_mode_cover");
                break;
            case R.id.reader_mode_slide:
                if (isChecked) showToast("reader_mode_slide");
                break;
            case R.id.reader_mode_simulation:
                if (isChecked) showToast("reader_mode_simulation");
                break;
            case R.id.reader_mode_none:
                if (isChecked) showToast("reader_mode_none");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isShow) {
            hide();
            return;
        }
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

    private void setChapter() {
        readerView.setElectric(1f);
        readerView.setTime("PM 3:20");

        ChapterBean chapter1 = new ChapterBean();
        chapter1.setBookName("《绝代双骄》");
        chapter1.setFirstChapter(true);
        chapter1.setChapterName("正文 第一章 名剑香花");
        chapter1.setChapterContent(Constant.TEST1);

        ChapterBean chapter2 = new ChapterBean();
        chapter2.setBookName("《绝代双骄》");
        chapter2.setFirstChapter(false);
        chapter2.setChapterName("正文 第二章 刀下遗孤");
        chapter2.setChapterContent(Constant.TEST2);

        ChapterBean chapter3 = new ChapterBean();
        chapter3.setBookName("《绝代双骄》");
        chapter3.setFirstChapter(false);
        chapter3.setChapterName("正文 第三章 第一神剑");
        chapter3.setChapterContent(Constant.TEST2);

        readerView.setChapters(chapter1, chapter2, chapter3);

    }

}
