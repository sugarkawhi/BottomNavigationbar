package com.monster.monstersport.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.monster.monstersport.R;
import com.monster.monstersport.base.BaseActivity;
import com.monster.monstersport.persistence.HyReaderPersistence;
import com.monster.monstersport.util.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import me.sugarkawhi.mreader.bean.ChapterBean;
import me.sugarkawhi.mreader.listener.IReaderTouchListener;
import me.sugarkawhi.mreader.persistence.IReaderPersistence;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_reader);
        ButterKnife.bind(this);
        init();
        ChapterBean chapter = new ChapterBean();
        chapter.setBookName("《白鹿原》");
        chapter.setFirstChapter(true);
        chapter.setChapterName("第1章 一波未平 一波又起");
        chapter.setChapterContent(Constant.TEST_CONTENT);
        readerView.setChapter(chapter);
        readerView.setElectric(0.6f);
        readerView.setTime("BJ TIME：19:20");
        readerView.setReaderTouchListener(new IReaderTouchListener() {
            @Override
            public boolean canTouch() {
                return !isShow;
            }

            @Override
            public void onTouchCenter() {
                if (isShow)
                    hide();
                else
                    show();
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
        //设置背景颜色
        int background = HyReaderPersistence.getBackground(this);
        initReaderBackground(background);
        //设置字体颜色

    }

    /**
     * 初始化阅读器背景
     *
     * @param background 背景
     */
    private void initReaderBackground(int background) {
        switch (background) {
            case DEFAULT:
                int fontDefault = ContextCompat.getColor(this, R.color.reader_font_default);
                readerView.setReaderBackground(R.drawable.reader_bg_default, fontDefault);
                break;
            case IMAGE_BLUE:
                int fontBlue = ContextCompat.getColor(this, R.color.reader_font_blue);
                readerView.setReaderBackground(R.drawable.reader_bg_blue, fontBlue);
                break;
            case IMAGE_PURPLE:
                int fontPurple = ContextCompat.getColor(this, R.color.reader_font_purple);
                readerView.setReaderBackground(R.drawable.reader_bg_purple, fontPurple);
                break;
            case COLOR_MATCHA:
                int bgMatcha = ContextCompat.getColor(this, R.color.reader_bg_matcha);
                int fontMatcha = ContextCompat.getColor(this, R.color.reader_font_matcha);
                readerView.setReaderBackgroundColor(bgMatcha, fontMatcha);
        }
    }

    private void init() {
        initReaderView();
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
}
