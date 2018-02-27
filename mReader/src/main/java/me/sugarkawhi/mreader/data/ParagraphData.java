package me.sugarkawhi.mreader.data;


import android.graphics.Point;
import android.graphics.Rect;

import java.util.List;

/**
 * 段落信息
 * 1. 包含段落的文字
 * 2. 每个段落的绘制矩形
 * Created by ZhaoZongyao on 2018/1/11.
 */

public class ParagraphData {

    //段落内容
    private String paragraphContent;
    //段落的区域
    private List<Rect> rectList;

    public String getParagraphContent() {
        return paragraphContent;
    }

    public void setParagraphContent(String paragraphContent) {
        this.paragraphContent = paragraphContent;
    }

    public List<Rect> getRectList() {
        return rectList;
    }

    public void setRectList(List<Rect> rectList) {
        this.rectList = rectList;
    }
}

