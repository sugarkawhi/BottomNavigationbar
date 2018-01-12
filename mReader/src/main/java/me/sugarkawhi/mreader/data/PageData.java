package me.sugarkawhi.mreader.data;


import java.util.ArrayList;

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
    private LineData lineData;
    private ImageData imageData;

    public static PageData newInstance() {
        PageData pageData = new PageData();
        LineData lineData = new LineData();
        lineData.setLines(new ArrayList<String>());
        pageData.setLineData(lineData);
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

    public LineData getLineData() {
        return lineData;
    }

    public void setLineData(LineData lineData) {
        this.lineData = lineData;
    }

    public ImageData getImageData() {
        return imageData;
    }

    public void setImageData(ImageData imageData) {
        this.imageData = imageData;
    }

}
