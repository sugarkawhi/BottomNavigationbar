package com.monster.monstersport.base;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.iflytek.cloud.SpeechUtility;
import com.monster.monstersport.dao.bean.DaoMaster;
import com.monster.monstersport.dao.bean.DaoSession;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import org.greenrobot.greendao.database.Database;

/**
 *
 * Created by ZhaoZongyao on 2017/9/25.
 */

public class MonApplication extends Application {

    private static MonApplication application;
    /**
     * A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher.
     */
    public static final boolean ENCRYPTED = true;
    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        SpeechUtility.createUtility(this, "appid=5a936bf5");
        setupLeakCanary();
        initGreenDao();
    }

    public static MonApplication getInstance() {
        return application;
    }

    protected void setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        refWatcher = LeakCanary.install(this);
    }


    private RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        MonApplication leakApplication = (MonApplication) context.getApplicationContext();
        return leakApplication.refWatcher;
    }

    private void initGreenDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "notes-db-encrypted" : "notes-db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
