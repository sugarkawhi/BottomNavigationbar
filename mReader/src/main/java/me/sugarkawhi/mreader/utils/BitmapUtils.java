package me.sugarkawhi.mreader.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Bitmap 二次采样
 * 设置Bitmap
 * Created by ZhaoZongyao on 2018/1/23.
 */

public class BitmapUtils {

    /**
     * 对Bitmap进行采样
     *
     * @param resources  x
     * @param drawableId x
     * @param dstWidth   x
     * @param dstHeight  x
     * @return x采样后的bitmap
     */
    public static Bitmap sampling(Resources resources, int drawableId, int dstWidth, int dstHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, drawableId, options);

        int originWidth = options.outWidth;
        int originHeight = options.outHeight;

        int sampleSize = 1;
        while (originWidth / sampleSize > dstWidth || originHeight / sampleSize > dstHeight) {
            sampleSize *= 2;
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = sampleSize;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeResource(resources, drawableId, options);
    }

    /**
     * 对Bitmap进行采样
     *
     * @param path      x
     * @param dstWidth  x
     * @param dstHeight x
     * @return x采样后的bitmap
     */
    public static Bitmap sampling(String path, int dstWidth, int dstHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        int originWidth = options.outWidth;
        int originHeight = options.outHeight;

        int sampleSize = 1;
        while (originWidth / sampleSize > dstWidth || originHeight / sampleSize > dstHeight) {
            sampleSize *= 2;
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = sampleSize;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeFile(path, options);
    }


    /**
     * 根据给定的宽和高进行拉伸
     *
     * @param origin    原图
     * @param newWidth  新图的宽
     * @param newHeight 新图的高
     * @return new Bitmap
     */
    public static Bitmap scaleBitmap(Bitmap origin, int newWidth, int newHeight) {
        if (origin == null) {
            return null;
        }
        int height = origin.getHeight();
        int width = origin.getWidth();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);// 使用后乘
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (!origin.isRecycled()) {
            origin.recycle();
        }
        return newBM;
    }

    /**
     *
     */
    public static Bitmap getCoverBitmap(View view, Bitmap backgroundBitmap) {
        int width = ScreenUtils.getScreenWidth(view.getContext());
        int height = ScreenUtils.getScreenHeight(view.getContext());
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        if (backgroundBitmap == null) canvas.drawColor(Color.WHITE);
        else canvas.drawBitmap(backgroundBitmap, 0, 0, null);
        view.draw(canvas);
        return bitmap;
    }

}
