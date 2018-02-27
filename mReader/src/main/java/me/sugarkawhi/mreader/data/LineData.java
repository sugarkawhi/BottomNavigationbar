package me.sugarkawhi.mreader.data;

import android.graphics.Rect;

import java.util.List;

/**
 * 行信息
 * 1.内容 line
 * 2.起始 x y 坐标 相对于绘制内容的y
 * 3.每个字的坐标
 * <p>
 * Created by ZhaoZongyao on 2018/1/11.
 */

public class LineData {
    //本行的字符-单个抽取出来
    private List<LetterData> letters;
    //相对于 内容绘制区域来讲
    private float offsetX;
    //相对于 内容绘制区域来讲
    private float offsetY;
    //是否是章节名  章节第一页
    private boolean isChapterName;
    //本行的字符串 区别于letters
    private String line;
    //本行区域
    private Rect backgroundRect;

    public LineData() {
    }

    public List<LetterData> getLetters() {
        return letters;
    }

    public void setLetters(List<LetterData> letters) {
        this.letters = letters;
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

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public Rect getBackgroundRect() {
        return backgroundRect;
    }

    public void setBackgroundRect(Rect backgroundRect) {
        this.backgroundRect = backgroundRect;
    }


}
