package me.sugarkawhi.mreader.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
}
