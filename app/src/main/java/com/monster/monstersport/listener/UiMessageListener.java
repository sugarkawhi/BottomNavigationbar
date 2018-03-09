package com.monster.monstersport.listener;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import static com.monster.monstersport.util.MainHandlerConstant.UI_CHANGE_SYNTHES_TEXT_SELECTION;

/**
 * 在 MessageListener的基础上，和UI配合。
 * Created by fujiayi on 2017/9/14.
 */

public class UiMessageListener extends MessageListener {


    private static final String TAG = "UiMessageListener";

    public UiMessageListener() {
        super();
    }

    /**
     * 合成数据和进度的回调接口，分多次回调。
     * 注意：progress表示进度，与播放到哪个字无关
     * @param utteranceId
     * @param data 合成的音频数据。该音频数据是采样率为16K，2字节精度，单声道的pcm数据。
     * @param progress 文本按字符划分的进度，比如:你好啊 进度是0-3
     */
    @Override
    public void onSynthesizeDataArrived(String utteranceId, byte[] data, int progress) {
        // sendMessage("onSynthesizeDataArrived");
    }

    /**
     * 播放进度回调接口，分多次回调
     * 注意：progress表示进度，与播放到哪个字无关
     *
     * @param utteranceId
     * @param progress 文本按字符划分的进度，比如:你好啊 进度是0-3
     */
    @Override
    public void onSpeechProgressChanged(String utteranceId, int progress) {
        // sendMessage("onSpeechProgressChanged");
        sendMessage("播放进度回调, progress：" + progress + ";序列号:" + utteranceId);
    }

    protected void sendMessage(String message) {
        sendMessage(message, false);
    }

    @Override
    protected void sendMessage(String message, boolean isError) {
        sendMessage(message, isError, PRINT);
    }


    protected void sendMessage(String message, boolean isError, int action) {
        super.sendMessage(message, isError);

    }
}
