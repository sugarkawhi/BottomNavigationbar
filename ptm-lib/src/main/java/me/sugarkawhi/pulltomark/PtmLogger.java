package me.sugarkawhi.pulltomark;

import android.util.Log;

/**
 * Created by ZhaoZongyao on 2018/3/27.
 */

public class PtmLogger {

    private static boolean debug = true;

    public static void e(String TAG, String messae) {
        if (debug)
            Log.e(TAG, messae);
    }
}
