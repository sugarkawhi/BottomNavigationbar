package com.monster.monstersport.dialog;

import android.content.Context;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.monster.monstersport.R;
import com.monster.monstersport.persistence.HyReaderPersistence;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import me.sugarkawhi.mreader.config.IReaderConfig;
import me.sugarkawhi.mreader.persistence.IReaderPersistence;

import static com.monster.monstersport.persistence.HyReaderPersistence.Background.COLOR_MATCHA;
import static com.monster.monstersport.persistence.HyReaderPersistence.Background.DEFAULT;
import static com.monster.monstersport.persistence.HyReaderPersistence.Background.IMAGE_BLUE;
import static com.monster.monstersport.persistence.HyReaderPersistence.Background.IMAGE_PURPLE;
import static com.monster.monstersport.persistence.HyReaderPersistence.Background.NIGHT;
import static me.sugarkawhi.mreader.config.IReaderConfig.PageMode.COVER;
import static me.sugarkawhi.mreader.config.IReaderConfig.PageMode.SIMULATION;
import static me.sugarkawhi.mreader.config.IReaderConfig.PageMode.SLIDE;
import static me.sugarkawhi.mreader.config.IReaderConfig.PageMode.NONE;

/**
 * 设置字体
 * Created by ZhaoZongyao on 2018/1/30.
 */

public class ReaderSettingDialog extends BottomPopDialog {

    private IReaderSettingListener mSettingListener;
    @BindView(R.id.reader_fontSize)
    TextView readerFontSize;
    //background
    @BindView(R.id.reader_bg_blue)
    RadioButton readerBgBlue;
    @BindView(R.id.reader_bg_purple)
    RadioButton readerBgPurple;
    @BindView(R.id.reader_bg_default)
    RadioButton readerBgDefault;
    @BindView(R.id.reader_bg_matcha)
    RadioButton readerBgMatcha;
    @BindView(R.id.reader_bg_night)
    RadioButton readerBgNight;
    //page mode
    @BindView(R.id.reader_mode_cover)
    RadioButton readerCover;
    @BindView(R.id.reader_mode_slide)
    RadioButton readerSlide;
    @BindView(R.id.reader_mode_simulation)
    RadioButton readerSimulation;
    @BindView(R.id.reader_mode_none)
    RadioButton readerNone;

    public ReaderSettingDialog(Context context) {
        super(context);
        ButterKnife.bind(this, getContentView());
        init();
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_reader_setting;
    }

    /**
     * 初始化操作
     */
    private void init() {
        int fontSize = IReaderPersistence.getFontSize(getContext());
        readerFontSize.setText(String.valueOf(fontSize));
        int mode = IReaderPersistence.getPageMode(getContext());
        switch (mode) {
            case IReaderConfig.PageMode.COVER:
                readerCover.setChecked(true);
                break;
            case IReaderConfig.PageMode.SLIDE:
                readerSlide.setChecked(true);
                break;
            case IReaderConfig.PageMode.SIMULATION:
                readerSimulation.setChecked(true);
                break;
            case IReaderConfig.PageMode.NONE:
                readerNone.setChecked(true);
                break;
        }
        int background = HyReaderPersistence.getBackground(getContext());
        switch (background) {
            case HyReaderPersistence.Background.IMAGE_BLUE:
                readerBgBlue.setChecked(true);
                break;
            case HyReaderPersistence.Background.IMAGE_PURPLE:
                readerBgPurple.setChecked(true);
                break;
            case HyReaderPersistence.Background.DEFAULT:
                readerBgDefault.setChecked(true);
                break;
            case HyReaderPersistence.Background.COLOR_MATCHA:
                readerBgMatcha.setChecked(true);
                break;
            case HyReaderPersistence.Background.NIGHT:
                readerBgNight.setChecked(true);
                break;
        }
    }

    public ReaderSettingDialog setSettingListener(IReaderSettingListener settingListener) {
        mSettingListener = settingListener;
        return this;
    }

    /**
     * 文字变大
     */
    @OnClick(R.id.reader_font_big)
    public void fontBig() {
        int currentFont = IReaderPersistence.getFontSize(getContext());
        currentFont++;

        readerFontSize.setText(String.valueOf(currentFont));
        if (mSettingListener != null) mSettingListener.onFontSizeChange(currentFont);
    }

    /**
     * 文字变小
     */
    @OnClick(R.id.reader_font_small)
    public void fontSmall() {
        int currentFont = IReaderPersistence.getFontSize(getContext());
        currentFont--;
        readerFontSize.setText(String.valueOf(currentFont));
        if (mSettingListener != null) mSettingListener.onFontSizeChange(currentFont);
    }

    /**
     * 选择背景
     */
    @OnCheckedChanged({R.id.reader_bg_blue, R.id.reader_bg_purple, R.id.reader_bg_default, R.id.reader_bg_matcha, R.id.reader_bg_night})
    public void selectBg(CompoundButton view, boolean isChecked) {
        if (!isChecked) return;
        int background = DEFAULT;
        switch (view.getId()) {
            case R.id.reader_bg_blue:
                background = (IMAGE_BLUE);
                break;
            case R.id.reader_bg_purple:
                background = (IMAGE_PURPLE);
                break;
            case R.id.reader_bg_default:
                background = (DEFAULT);
                break;
            case R.id.reader_bg_matcha:
                background = (COLOR_MATCHA);
                break;
            case R.id.reader_bg_night:
                background = (NIGHT);
                break;
        }
        HyReaderPersistence.saveBackground(getContext(), background);
        if (mSettingListener != null) mSettingListener.onBackgroundChange(background);

    }

    /**
     * 选择翻页模式
     */
    @OnCheckedChanged({R.id.reader_mode_cover, R.id.reader_mode_slide, R.id.reader_mode_simulation, R.id.reader_mode_none})
    public void selectMode(CompoundButton view, boolean isChecked) {
        if (!isChecked) return;
        int pageMode = COVER;
        switch (view.getId()) {
            case R.id.reader_mode_cover:
                pageMode = COVER;
                break;
            case R.id.reader_mode_slide:
                pageMode = SLIDE;
                break;
            case R.id.reader_mode_simulation:
                pageMode = SIMULATION;
                break;
            case R.id.reader_mode_none:
                pageMode = NONE;
                break;
        }
        if (mSettingListener != null) mSettingListener.onPageModeChange(pageMode);

    }


    public interface IReaderSettingListener {
        void onFontSizeChange(int fontSize);

        void onBackgroundChange(int background);

        void onPageModeChange(int pageMode);
    }
}
