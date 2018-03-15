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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof LetterData)) return false;
        LetterData letter = (LetterData) obj;
        return letter.getLetter() == this.getLetter()
                && letter.getOffsetX() == this.getOffsetX()
                && letter.getOffsetY() == this.getOffsetY();
    }


    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + letter;
        result = (int) (31 * result + offsetY);
        result = (int) (31 * result + offsetY);
        return result;
    }
}
