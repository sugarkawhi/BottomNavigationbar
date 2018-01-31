package me.sugarkawhi.mreader.element;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.List;

import me.sugarkawhi.mreader.data.ImageData;

/**
 * 绘制封面
 * Created by ZhaoZongyao on 2018/1/11.
 */

public class CoverElement extends Element {

    private Bitmap mCoverBitmap;


    public Bitmap getCoverBitmap() {
        return mCoverBitmap;
    }

    public void setCoverBitmap(Bitmap coverBitmap) {
        mCoverBitmap = coverBitmap;
    }

    @Override
    public boolean onDraw(Canvas canvas) {
        if (mCoverBitmap != null) canvas.drawBitmap(mCoverBitmap, 0, 0, null);
        return true;
    }
}
