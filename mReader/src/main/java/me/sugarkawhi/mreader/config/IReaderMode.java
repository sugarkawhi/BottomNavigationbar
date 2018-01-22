package me.sugarkawhi.mreader.config;


import android.graphics.Bitmap;

/**
 * 配套设置
 * 需要配置： 1.标题颜色2.文字颜色3.背景图Bitmap
 * <p>
 * Created by ZhaoZongyao on 2018/1/11.
 */

public class IReaderMode {

    private int mChapterNameColor;
    private int mContentTextColor;
    private Bitmap mBackground;


    public IReaderMode() {

    }

    public IReaderMode(int chapterNameColor, int contentTextColor, Bitmap background) {
        mChapterNameColor = chapterNameColor;
        mContentTextColor = contentTextColor;
        mBackground = background;
    }

    public class IReaderNightMode extends IReaderMode {
        public IReaderNightMode(int chapterNameColor, int contentTextColor, Bitmap background) {
            super(chapterNameColor, contentTextColor, background);
        }
    }
}
