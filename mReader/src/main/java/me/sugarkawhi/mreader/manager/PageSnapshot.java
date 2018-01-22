package me.sugarkawhi.mreader.manager;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;

/**
 * 页面快照 保存
 * Created by ZhaoZongyao on 2018/1/22.
 */

public class PageSnapshot {

    private int pageIndex;
    private Bitmap mBitmap;
    private Canvas mCanvas;


    public void beginRecording(int width, int height) {
        if (mBitmap == null) {
            mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            mCanvas = new Canvas(mBitmap);
        } else {
            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        }
    }

    public void draw() {
        if (mBitmap != null && mCanvas != null) {
            mCanvas.drawBitmap(mBitmap, 0, 0, null);
        }
    }

    public void destory() {
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }
}
