package me.sugarkawhi.mreader.element;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    private HeaderElement mHeaderElement;
    private FooterElement mFooterElement;
    private LineElement mLineElement;

    private List<PageData> mPageDatas;
    //背景Paint
    private Paint mBgPaint;
    //内容 宽。高
    private float mContentWidth;
    private float mContentHeight;
    //章节名 Paint
    private Paint mChapterNamePaint;
    //内容 Paint
    private Paint mContentPaint;
    //头 底 Paint
    private Paint mHeaderPaint;
    private String mTime;
    private float mElectric;
    private PageManager mPageManager;

    private float mReaderWidth;
    private float mReaderHeight;

    public PageElement(float readerWidth, float readerHeight,
                       float headerHeight, float footerHeight,
                       float padding,
                       float lineSpacing, float paragraphSpacing,
                       Battery battery) {
        mReaderWidth = readerWidth;
        mReaderHeight = readerHeight;
        mContentWidth = readerWidth - padding - padding;
        mContentHeight = readerHeight - headerHeight - footerHeight;
        mPageDatas = new ArrayList<>();
        mContentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mContentPaint.setTextSize(Config.DEFAULT_CONTENT_TEXTSIZE);
        mContentPaint.setColor(Color.parseColor("#404040"));
        mHeaderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHeaderPaint.setTextSize(Config.DEFAULT_HEADER_TEXTSIZE);

        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mChapterNamePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mChapterNamePaint.setTextSize(Config.DEFAULT_CONTENT_TEXTSIZE * 1.3f);
        mChapterNamePaint.setColor(Color.parseColor("#A0522D"));
        mHeaderElement = new HeaderElement(headerHeight, padding, mHeaderPaint);
        mFooterElement = new FooterElement(readerWidth, readerHeight, footerHeight, padding, battery, mHeaderPaint);
        mLineElement = new LineElement(mContentWidth, mContentHeight,
                headerHeight, footerHeight,
                padding, lineSpacing, mContentPaint, mChapterNamePaint);
        mPageManager = new PageManager(mContentWidth, mContentHeight, lineSpacing, paragraphSpacing,
                20, 260, mContentPaint, mChapterNamePaint);
    }


    private void dividerChapter() {
        if (mChapter == null) return;
        // convert String into InputStream
        InputStream is = new ByteArrayInputStream(mChapter.getChapterContent().getBytes());
        // read it with BufferedReader
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        mPageDatas = mPageManager.generatePages(mChapter, br);
    }


    @Override
    public void onDraw(Canvas canvas) {
        mBgPaint.setColor(Color.parseColor("#CEC29C"));
        canvas.drawRect(0, 0, mReaderWidth, mReaderHeight, mBgPaint);
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
        dividerChapter();
    }
}
