package me.sugarkawhi.mreader.data;


import java.util.ArrayList;
import java.util.List;

/**
 * PageData 记录了每一页开头文字在章节的位置，
 * 同时包含该页面HeaderData, LineData,ImageData 和 FooterData 数据等。
 * Created by ZhaoZongyao on 2018/1/11.
 */

public class PageData {
    private float indexOfChapter;
    //同时包含该页面HeaderData
    private String chapterName;
    //进度
    private String progress;
    //
    private List<String> lines;
    private ImageData imageData;

    public static PageData newInstance() {
        PageData pageData = new PageData();
        return pageData;
    }

    public float getIndexOfChapter() {
        return indexOfChapter;
    }

    public void setIndexOfChapter(float indexOfChapter) {
        this.indexOfChapter = indexOfChapter;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public List<String> getLines() {
        return lines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }

    public ImageData getImageData() {
        return imageData;
    }

    public void setImageData(ImageData imageData) {
        this.imageData = imageData;
    }

}
