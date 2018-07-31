package com.monster.monstersport.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;

import static android.content.ContentValues.TAG;


/**
 * Created by fujiayi on 2017/5/19.
 */

public class OfflineResource {


    private static final String SAMPLE_DIR = "baiduTTS";

    private AssetManager assets;
    private String destPath;

    private String textFilename;
    private String modelFilename;

    public OfflineResource(Context context, int speaker) throws IOException {
        context = context.getApplicationContext();
        this.assets = context.getApplicationContext().getAssets();
        this.destPath = FileUtil.createTmpDir(context);
        setOfflineVoiceType(speaker);
    }

    public String getModelFilename() {
        return modelFilename;
    }

    public String getTextFilename() {
        return textFilename;
    }

    /**
     * m15 离线男声
     * f7 离线女声
     * yyjw 度逍遥
     * as 度丫丫
     */

    public void setOfflineVoiceType(int speaker) throws IOException {
        String text = "bd_etts_text.dat";
        String model;

    }


    private String copyAssetsFile(String sourceFilename) throws IOException {
        String destFilename = destPath + "/" + sourceFilename;
        FileUtil.copyFromAssets(assets, sourceFilename, destFilename, false);
        Log.i(TAG, "文件复制成功：" + destFilename);
        return destFilename;
    }


}
