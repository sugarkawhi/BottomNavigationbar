package me.sugarkawhi.mreader.element;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

import me.sugarkawhi.mreader.bean.Battery;
import me.sugarkawhi.mreader.bean.ChapterBean;
import me.sugarkawhi.mreader.config.Config;
import me.sugarkawhi.mreader.data.PageData;
import me.sugarkawhi.mreader.manager.PageManager;

/**
 * 分页模块：功能包括将传入的章节数据分成数个 PageData
 * (生成的 PageData 个数即为该章节页数，PageData 记录了每一页开头文字在章节的位置，
 * 同时包含该页面HeaderData, LineData,ImageData 和 FooterData 数据等。
 * 各个 Data 里面记录了相应的文字信息，可以快速的定位到章节内容中。)；绘制页面；
 * <p>
 * PageElement 利用各个 Element 模块将章节数据进行测量分页，
 * 每一页 PageData 记录着 LineData,ImageData,HeaderData和FooterData信息。
 * 绘图时需要将各个信息填入 Element 中
 * <p>
 * Created by ZhaoZongyao on 2018/1/11.
 */

public class PageElement extends Element {

    private float mReaderWidth;
    private float mReaderHeight;
    private float mReaderPadding;
    private float mHeaderHeight;
    private float mFooterHeight;

    private HeaderElement mHeaderElement;
    private FooterElement mFooterElement;
    private LineElement mLineElement;
    private ImageElement mImageElement;

    private List<PageData> mPageDatas;
    private String mTime;
    private float mElectric;

    private float mLineSpacing = Config.DEFAULT_CONTENT_LINE_SPACING;
    private float mParagraphSpacing = Config.DEFAULT_CONTENT_PARAGRAPH_SPACING;

    public PageElement(float headerHeight, float footerHeight, float padding, float chapterNameSize, Battery battery) {
        mHeaderElement = new HeaderElement(headerHeight, padding, chapterNameSize);
        mFooterElement = new FooterElement(footerHeight, padding, chapterNameSize, battery);
        mLineElement = new LineElement(headerHeight, footerHeight, padding);
        mPageDatas = new ArrayList<>();
    }


    public void setReaderSize(float readerWidth, float readerHeight) {
        mReaderWidth = readerWidth;
        mReaderHeight = readerHeight;
        mFooterElement.setReaderSize(readerWidth, readerHeight);
        mLineElement.setReaderSize(readerWidth, readerHeight);
        dividerChapter();
    }


    private void dividerChapter() {
        if (mChapter == null) return;
        mPageDatas = PageManager.generatePages(mChapter,
                mReaderWidth - 2 * mReaderPadding,
                mReaderHeight - mFooterHeight - mHeaderHeight,
                40, mLineSpacing, mParagraphSpacing);
    }


    @Override
    public void onDraw(Canvas canvas) {
        PageData page = null;
        if (mPageDatas.size() > 0) {
            page = mPageDatas.get(0);
        }
        if (page != null) {
            //set header
            mHeaderElement.setChapterName(page.getChapterName());
            mHeaderElement.onDraw(canvas);
            //set footer
            mFooterElement.setProgress(page.getProgress());
        }
        if (Config.DEBUG) {
            mElectric = 0.6f;
            mTime = "19:28";
            mFooterElement.setProgress("78%");
        }
        mFooterElement.setElectric(mElectric);
        mFooterElement.setTime(mTime);
        mFooterElement.onDraw(canvas);
        //set line
        mLineElement.setPageData(page);
        mLineElement.onDraw(canvas);
    }

    public void setTime(String time) {
        this.mTime = time;
    }

    public void setElectric(float electric) {
        mElectric = electric;
    }


    private ChapterBean mChapter;

    public void setChapter(ChapterBean chapter) {
        this.mChapter = chapter;
    }

    public void setContentTextSize(float textSize) {
        mLineElement.setTextSize(textSize);
    }

    public void setContentTextColor(int color) {
        mLineElement.setContentTextColor(color);
    }
}
