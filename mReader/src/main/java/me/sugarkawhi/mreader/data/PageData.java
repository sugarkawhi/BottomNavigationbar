package me.sugarkawhi.mreader.data;


/**
 * PageData 记录了每一页开头文字在章节的位置，
 * 同时包含该页面HeaderData, LineData,ImageData 和 FooterData 数据等。
 * Created by ZhaoZongyao on 2018/1/11.
 */

public class PageData {
    private float indexOfChapter;
    private HeaderData headerData;
    private LineData lineData;
    private ImageData imageData;
    private FooterData footerData;

    public float getIndexOfChapter() {
        return indexOfChapter;
    }

    public void setIndexOfChapter(float indexOfChapter) {
        this.indexOfChapter = indexOfChapter;
    }

    public HeaderData getHeaderData() {
        return headerData;
    }

    public void setHeaderData(HeaderData headerData) {
        this.headerData = headerData;
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

    public FooterData getFooterData() {
        return footerData;
    }

    public void setFooterData(FooterData footerData) {
        this.footerData = footerData;
    }
}
