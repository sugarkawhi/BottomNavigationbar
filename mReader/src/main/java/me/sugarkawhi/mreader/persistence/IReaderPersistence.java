package me.sugarkawhi.mreader.persistence;

import android.content.Context;
import android.content.SharedPreferences;

import me.sugarkawhi.mreader.config.IReaderConfig;

/**
 * 持久化保存
 * Created by ZhaoZongyao on 2018/1/30.
 */

public class IReaderPersistence {
    public static final String TAG = "IReaderPersistence";

    private static final String SP_NAME = "me.sugarkawhi.mreader.persistence.IReaderPersistence";
    //文字大小
    private static final String READER_FONT_SIZE = "READER_FONT_SIZE";
    //翻页模式
    private static final String READER_PAGE_MODE = "READER_PAGE_MODE";
    //背景
    protected static final String READER_BACKGROUND = "READER_BACKGROUND";
    //字体颜色
    protected static final String READER_FONT_COLOR = "READER_FONT_COLOR";


    protected static SharedPreferences getSP(Context context) {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 获取当前文字大小
     */
    public static int getFontSize(Context context) {
        return getSP(context).getInt(READER_FONT_SIZE, IReaderConfig.FontSize.DEFAULT);
    }

    /**
     * 保存阅读器文字大小
     */
    public static void saveFontSize(Context context, int fontSize) {
        getSP(context).edit().putInt(READER_FONT_SIZE, fontSize).apply();
    }

    /**
     * 获取当前翻页模式
     */
    public static int getPageMode(Context context) {
        return getSP(context).getInt(READER_PAGE_MODE, IReaderConfig.PageMode.COVER);
    }

    /**
     * 保存翻页模式
     */
    public static void savePageMode(Context context, int mode) {
        getSP(context).edit().putInt(READER_PAGE_MODE, mode).apply();
    }

}
