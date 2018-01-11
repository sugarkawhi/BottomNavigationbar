package me.sugarkawhi.mreader.element;

import android.graphics.Canvas;

import java.util.List;

import me.sugarkawhi.mreader.data.PageData;

/**
 * 分页模块：功能包括将传入的章节数据分成数个 PageData
 * (生成的 PageData 个数即为该章节页数，PageData 记录了每一页开头文字在章节的位置，
 * 同时包含该页面HeaderData, LineData,ImageData 和 FooterData 数据等。
 * 各个 Data 里面记录了相应的文字信息，可以快速的定位到章节内容中。)；绘制页面；
 * Created by ZhaoZongyao on 2018/1/11.
 */

public class PageElement extends Element {

    private float mReaderWidth;
    private float mReaderHeight;
    private float mReaderPadding;
    private HeaderElement mHeaderElement;
    private FooterElement mFooterElement;
    private ImageElement mImageElement;

    private List<PageData> mPageDatas;
    private PageData mPageData;
    private String mProgress;
    private String mTime;
    private float mElectric;

    public PageElement(float headerHeight, float footerHeight, float padding, float chapterNameSize,
                       int batteryWidth, int batteryHeight, int batteryHead, int batteryGap) {
        mHeaderElement = new HeaderElement(headerHeight, padding, chapterNameSize);
        mFooterElement = new FooterElement(footerHeight, padding, chapterNameSize, batteryWidth, batteryHeight, batteryHead, batteryGap);
        mReaderPadding = padding;
    }

    public void setReaderSize(float readerWidth, float readerHeight) {
        mReaderWidth = readerWidth;
        mReaderHeight = readerHeight;
        mFooterElement.setReaderSize(readerWidth, readerHeight);
    }


    @Override
    public void onDraw(Canvas canvas) {
        mHeaderElement.setChapterTitle("第一章 夜的第七章");
        mHeaderElement.onDraw(canvas);
        mFooterElement.setTime(mTime);
        mFooterElement.setProgress(mProgress);
        mFooterElement.setElectric(mElectric);
        mFooterElement.onDraw(canvas);
    }

    public void setTime(String time) {
        this.mTime = time;
    }

    public void setElectric(float electric) {
        mElectric = electric;
    }

}
