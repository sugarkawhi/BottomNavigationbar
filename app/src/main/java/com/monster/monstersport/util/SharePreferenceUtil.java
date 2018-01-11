package com.monster.monstersport.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ZhaoZongyao on 2017/9/25.
 */

public class SharePreferenceUtil {

    private static final String FILE_NAME = "com.xhhread_sp";

    private SharedPreferences sp;
    private SharePreferenceUtil INSTNACE;

    public SharePreferenceUtil(Context context) {
        sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

}
