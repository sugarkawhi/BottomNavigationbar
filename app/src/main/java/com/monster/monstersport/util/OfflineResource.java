package com.monster.monstersport.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;

import me.sugarkawhi.mreader.config.IReaderConfig;

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

    public void setOfflineVoiceType(int speaker) throws IOException {
        String text = "bd_etts_text.dat";
        String model;
        if (speaker == IReaderConfig.Speaker.MALE) {
            model = "bd_etts_common_speech_m15_mand_eng_high_am-mix_v3.0.0_20170505.dat";
        } else if (speaker == IReaderConfig.Speaker.FEMALE) {
            model = "bd_etts_common_speech_f7_mand_eng_high_am-mix_v3.0.0_20170512.dat";
        } else if (speaker == IReaderConfig.Speaker.DUXY) {
            model = "bd_etts_common_speech_yyjw_mand_eng_high_am-mix_v3.0.0_20170512.dat";
        } else if (speaker == IReaderConfig.Speaker.DUYY) {
            model = "bd_etts_common_speech_as_mand_eng_high_am_v3.0.0_20170516.dat";
        } else {
            throw new RuntimeException("voice type is not in list");
        }
        textFilename = copyAssetsFile(text);
        modelFilename = copyAssetsFile(model);

    }


    private String copyAssetsFile(String sourceFilename) throws IOException {
        String destFilename = destPath + "/" + sourceFilename;
        FileUtil.copyFromAssets(assets, sourceFilename, destFilename, false);
        Log.i(TAG, "文件复制成功：" + destFilename);
        return destFilename;
    }


}
