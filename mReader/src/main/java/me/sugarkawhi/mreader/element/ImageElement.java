package me.sugarkawhi.mreader.element;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.List;

import me.sugarkawhi.mreader.data.ImageData;
import me.sugarkawhi.mreader.data.LineData;

/**
 * 图片部分：
 * Created by ZhaoZongyao on 2018/1/11.
 */

public class ImageElement extends Element {

    private float mHeaderHeight;
    private float mFooterHeight;
    private float mPadding;
    private float mContentWidth;
    private float mContentHeight;
    private Paint mContentPaint;
    private Paint mChapterNamePaint;

    private List<ImageData> mImageDataList;

    public ImageElement(float headerHeight, float footerHeight, float padding) {
        this.mHeaderHeight = headerHeight;
        this.mFooterHeight = footerHeight;
        this.mPadding = padding;
    }

    public void setImageDataList(List<ImageData> imageDataList) {
        this.mImageDataList = imageDataList;
    }

    @Override
    public boolean onDraw(Canvas canvas) {
        if (mImageDataList == null) return true;
        for (ImageData image : mImageDataList) {
            canvas.drawBitmap(image.getBitmap(), mPadding + image.getX(), mHeaderHeight + image.getY(), null);
        }
        return true;
    }
}
