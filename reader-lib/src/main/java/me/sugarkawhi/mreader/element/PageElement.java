package me.sugarkawhi.mreader.element;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;

import java.util.List;

import me.sugarkawhi.mreader.data.ImageData;
import me.sugarkawhi.mreader.data.LetterData;
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

public class PageElement {

    private HeaderElement mHeaderElement;
    private FooterElement mFooterElement;
    private LineElement mLineElement;


    //内容 宽。高

    private int mReaderWidth;
    private int mReaderHeight;

    //背景Bitmap 有时候纯色 有时候纹理图片
    private Bitmap mBackgroundBitmap;
    private Canvas mBackgroundCanvas;
    //扉页 Bitmap
    private Bitmap mCoverBitmap;

    public PageElement(int readerWidth, int readerHeight,
                       float headerHeight, float footerHeight,
                       float padding, Paint headPaint, Paint contentPaint, Paint chapterNamePaint) {
        mReaderWidth = readerWidth;
        mReaderHeight = readerHeight;
        float mContentWidth = readerWidth - padding - padding;
        float mContentHeight = readerHeight - headerHeight - footerHeight;

        mHeaderElement = new HeaderElement(headerHeight, padding, headPaint);
        mFooterElement = new FooterElement(readerWidth, readerHeight, footerHeight, padding, headPaint);
        mLineElement = new LineElement(mContentWidth, mContentHeight,
                headerHeight, footerHeight, padding, contentPaint, chapterNamePaint);


        mBackgroundBitmap = Bitmap.createBitmap(mReaderWidth, mReaderHeight, Bitmap.Config.RGB_565);
        mBackgroundCanvas = new Canvas(mBackgroundBitmap);
    }


    /**
     * 根据 PageData 生成对应的bitmap 对象
     *
     * @param pageData 页面信息
     * @param bitmap   绘制的bitmap对象
     */
    public void generatePage(PageData pageData, Bitmap bitmap) {
        if (pageData == null || bitmap == null) return;
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        //非封面页
        if (!pageData.isCover()) {
            //draw background
            canvas.drawBitmap(mBackgroundBitmap, 0, 0, null);
            //set header
            mHeaderElement.setChapterName(pageData.getChapterName());
            mHeaderElement.onDraw(canvas);
            //set footer
            mFooterElement.setProgress(pageData.getIndexOfChapter() + 1 + "/" + pageData.getTotalPageNum());
            mFooterElement.onDraw(canvas);
            //set line
            mLineElement.setLineData(pageData.getLines());
            mLineElement.setLetterData(pageData.getLetters());
            mLineElement.onDraw(canvas);

        }
        //封面页
        else {
            if (mCoverBitmap == null) return;
            canvas.drawBitmap(mCoverBitmap, 0, 0, null);
        }
    }

    /**
     * 设置背景图片
     * 纯色/图片
     *
     * @param backgroundBitmap 背景
     */
    public void setBackgroundBitmap(Bitmap backgroundBitmap) {
        if (backgroundBitmap == null) return;
        mBackgroundCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        mBackgroundCanvas.drawBitmap(backgroundBitmap, 0, 0, null);
    }

    /**
     * 设置时间
     *
     * @param time 当前时间
     */
    public void setTime(String time) {
        mFooterElement.setTime(time);
    }

    /**
     * 设置电量
     *
     * @param level 电量百分比
     */
    public void setBatteryLevel(float level) {
        mFooterElement.setBatteryLevel(level);
    }

    /**
     * 设置封面
     * Bitmap对象 Canvas 绘制
     *
     * @param bitmap 封面
     */
    public void setCoverBitmap(Bitmap bitmap) {
        this.mCoverBitmap = bitmap;
    }

    /**
     * 百度语音合成
     *
     * @param list 当前页要绘制背景的文字
     */
    public void setTtsLetters(List<LetterData> list) {
        mLineElement.setTtsLetters(list);
    }

    /**
     * 百度语音合成
     *
     * @param list 当前页要绘制背景的文字
     */
    public List<LetterData> getTtsLetters() {
        return mLineElement.getTtsLetters();
    }


    /**
     * 结束语音合成
     */
    public void stopTts() {
        mLineElement.clearTtsLetters();
    }

    /**
     * 清空Tts绘制文字
     */
    public void clearTtsLetters() {
        mLineElement.clearTtsLetters();
    }
}
