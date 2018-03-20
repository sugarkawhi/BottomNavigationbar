package me.sugarkawhi.mreader.utils;

import android.util.Log;

import me.sugarkawhi.mreader.config.IReaderConfig;

/**
 * log print
 * Created by ZhaoZongyao on 2018/1/30.
 */

public class L {


    public static void i(String TAG, String log) {
        if (!IReaderConfig.DEBUG) return;
        Log.i(TAG, log);
    }

    public static void d(String TAG, String log) {
        if (!IReaderConfig.DEBUG) return;
        Log.d(TAG, log);
    }

    public static void w(String TAG, String log) {
        if (!IReaderConfig.DEBUG) return;
        Log.w(TAG, log);
    }

    public static void e(String TAG, String log) {
        if (!IReaderConfig.DEBUG) return;
        Log.e(TAG, log);
    }


}
