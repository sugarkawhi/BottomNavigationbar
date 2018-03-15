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
    private int indexOfChapter;
    //本章节总页数
    private int totalPageNum;
    //章节id
    private String chapterId;
    //同时包含该页面HeaderData
    private String chapterName;
    //进度 整数 0-100  最大100
    private int progress;
    //行数据
    private List<LineData> lines;
    //图片数据
    private List<ImageData> images;
    //段落数据
    private String content;
    //字符数据
    private List<LetterData> letters;


    public PageData() {
    }

    public boolean isCover() {
        return isCover;
    }

    public void setCover(boolean cover) {
        isCover = cover;
    }

    public int getIndexOfChapter() {
        return indexOfChapter;
    }

    public void setIndexOfChapter(int indexOfChapter) {
        this.indexOfChapter = indexOfChapter;
    }

    public int getTotalPageNum() {
        return totalPageNum;
    }

    public void setTotalPageNum(int totalPageNum) {
        this.totalPageNum = totalPageNum;
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<LetterData> getLetters() {
        return letters;
    }

    public void setLetters(List<LetterData> letters) {
        this.letters = letters;
    }
}
