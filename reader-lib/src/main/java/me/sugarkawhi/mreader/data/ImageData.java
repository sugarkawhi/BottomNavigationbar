package me.sugarkawhi.mreader.data;


import android.graphics.Bitmap;

/**
 * 图片信息 包含图片大小和位置
 * <p>
 * Created by ZhaoZongyao on 2018/1/11.
 */

public class ImageData {

    private Bitmap bitmap;

    private float x;
    private float y;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
