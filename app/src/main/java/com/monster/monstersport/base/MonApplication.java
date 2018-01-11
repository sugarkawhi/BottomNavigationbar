package com.monster.monstersport.base;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.iflytek.cloud.SpeechUtility;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by ZhaoZongyao on 2017/9/25.
 */

public class MonApplication extends Application {

    public static MonApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        SpeechUtility.createUtility(this, "appid=5a532148");
        setupLeakCanary();
    }

    public static Application getInstance() {
        return application;
    }

    protected void setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        enabledStrictMode();
        refWatcher = LeakCanary.install(this);
    }

    private static void enabledStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder() //
                .detectAll() //
                .penaltyLog() //
                .penaltyDeath() //
                .build());
    }

    private RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        MonApplication leakApplication = (MonApplication) context.getApplicationContext();
        return leakApplication.refWatcher;
    }
}
