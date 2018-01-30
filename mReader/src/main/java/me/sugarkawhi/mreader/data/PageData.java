package me.sugarkawhi.mreader.data;


import java.util.List;

/**
 * PageData 记录了每一页开头文字在章节的位置，
 * 同时包含该页面HeaderData, LineData,ImageData 和 FooterData 数据等。
 * Created by ZhaoZongyao on 2018/1/11.
 */

public class PageData {
    //是否是封面页
    private boolean isCover;
    //在章节中的索引
    private float indexOfChapter;
    //同时包含该页面HeaderData
    private String chapterName;
    //进度
    private String progress;
    //
    private List<LineData> lines;
    private List<ImageData> images;


    public boolean isCover() {
        return isCover;
    }

    public void setCover(boolean cover) {
        isCover = cover;
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

    public List<LineData> getLines() {
        return lines;
    }

    public void setLines(List<LineData> lines) {
        this.lines = lines;
    }

    public List<ImageData> getImages() {
        return images;
    }

    public void setImages(List<ImageData> images) {
        this.images = images;
    }
}
