package me.sugarkawhi.mreader.data;

import android.graphics.Rect;

import java.util.List;

/**
 * 字符信息
 * <p>
 * Created by ZhaoZongyao on 2018/2/27.
 */

public class LetterData {
    private char letter;
    private float offsetX;
    private float offsetY;
    private boolean isChapterName;
    //所占的区域 - 用于语音阅读时绘制背景
    private Rect area;

    public LetterData() {
    }

    public char getLetter() {
        return letter;
    }

    public void setLetter(char letter) {
        this.letter = letter;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    public boolean isChapterName() {
        return isChapterName;
    }

    public void setChapterName(boolean chapterName) {
        isChapterName = chapterName;
    }

    public Rect getArea() {
        return area;
    }

    public void setArea(Rect area) {
        this.area = area;
    }
}
