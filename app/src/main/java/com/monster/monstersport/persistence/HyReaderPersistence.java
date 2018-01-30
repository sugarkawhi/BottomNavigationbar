package com.monster.monstersport.persistence;

import android.content.Context;
import android.graphics.Color;

import com.monster.monstersport.R;

import me.sugarkawhi.mreader.persistence.IReaderPersistence;

/**
 * Created by ZhaoZongyao on 2018/1/30.
 */

public class HyReaderPersistence extends IReaderPersistence {

    //背景
    public interface Background {
        //默认
        int DEFAULT = 1;
        //图片-蓝色
        int IMAGE_BLUE = 2;
        //图片-紫色
        int IMAGE_PURPLE = 3;
        //纯色-抹茶
        int COLOR_MATCHA = 4;
    }

    //字体颜色 对应于背景
    public interface FontColor {
        //默认
        int DEFAULT = R.color.reader_font_default;
        //对应于蓝色背景
        int BLUE = R.color.reader_font_blue;
        //对应于紫色背景
        int PURPLE = R.color.reader_font_purple;
        //对应于抹茶色背景
        int MATCHA = R.color.reader_font_matcha;
    }

    /**
     * 获取背景
     */
    public static int getBackground(Context context) {
        return getSP(context).getInt(READER_BACKGROUND, Background.DEFAULT);
    }

    /**
     * 保存背景
     */
    public static void saveBackground(Context context, int background) {
        getSP(context).edit().putInt(READER_BACKGROUND, background).apply();
    }

    /**
     * 获取字体颜色
     */
    public static int getFontColor(Context context) {
        return getSP(context).getInt(READER_FONT_COLOR, FontColor.DEFAULT);
    }

    /**
     * 保存字体颜色
     */
    public static void saveFontColor(Context context, int fontColor) {
        getSP(context).edit().putInt(READER_FONT_COLOR, fontColor).apply();
    }


}
