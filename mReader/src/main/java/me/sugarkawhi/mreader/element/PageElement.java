package me.sugarkawhi.mreader.element;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import me.sugarkawhi.mreader.bean.Battery;
import me.sugarkawhi.mreader.data.PageData;

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


    //内容 宽。高
    private float mContentWidth;
    private float mContentHeight;

    private String mTime;
    private float mElectric;

    private float mReaderWidth;
    private float mReaderHeight;

    private PageData mPageData;
    //背景Bitmap 有时候纯色 有时候纹理图片
    private Bitmap mBackgroundBitmap;
    private Canvas mBackgroundCanvas;

    public PageElement(float readerWidth, float readerHeight,
                       float headerHeight, float footerHeight,
                       float padding, float letterSpacing,
                       float lineSpacing, float paragraphSpacing,
                       Battery battery,
                       Paint headPaint, Paint contentPaint, Paint chapterNamePaint) {
        mReaderWidth = readerWidth;
        mReaderHeight = readerHeight;
        mContentWidth = readerWidth - padding - padding;
        mContentHeight = readerHeight - headerHeight - footerHeight;

        mHeaderElement = new HeaderElement(headerHeight, padding, headPaint);
        mFooterElement = new FooterElement(readerWidth, readerHeight, footerHeight, padding, battery, headPaint);
        mLineElement = new LineElement(mContentWidth, mContentHeight,
                headerHeight, footerHeight,
                padding, lineSpacing, contentPaint, chapterNamePaint);
        init();
    }

    private void init() {
        mBackgroundBitmap = Bitmap.createBitmap((int) mReaderWidth, (int) mReaderHeight, Bitmap.Config.RGB_565);
        mBackgroundCanvas = new Canvas(mBackgroundBitmap);
        mBackgroundCanvas.drawColor(Color.parseColor("#CEC29C"));
    }

    @Override
    public boolean onDraw(Canvas canvas) {
        if (mPageData == null) return false;
        //draw background
        canvas.drawBitmap(mBackgroundBitmap, 0, 0, null);
        //set header
        mHeaderElement.setChapterName(mPageData.getChapterName());
        mHeaderElement.onDraw(canvas);
        //set footer
        mFooterElement.setProgress(mPageData.getProgress());
        mFooterElement.setElectric(mElectric);
        mFooterElement.setTime(mTime);
        mFooterElement.onDraw(canvas);
        //set line
        mLineElement.setLineData(mPageData.getLines());
        mLineElement.onDraw(canvas);
        return true;
    }

    public void setTime(String time) {
        this.mTime = time;
    }

    public void setElectric(float electric) {
        mElectric = electric;
    }

    public void setPageData(PageData pageData) {
        mPageData = pageData;
    }


}
