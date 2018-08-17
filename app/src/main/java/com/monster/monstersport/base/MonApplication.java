package com.monster.monstersport.base;

import android.app.Application;

/**
 * Created by ZhaoZongyao on 2017/9/25.
 */

public class MonApplication extends Application {

    private static MonApplication application;
    /**
     * A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher.
     */

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    public static MonApplication getInstance() {
        return application;
    }

}
