package me.sugarkawhi.mreader.listener;

/**
 * reader touch listener
 * Created by ZhaoZongyao on 2018/1/17.
 */

public interface IReaderTouchListener {

    /**
     * is can touch
     */
    boolean canTouch();

    /**
     * touch center
     */
    void onTouchCenter();


    /**
     * 正在播放语音时候 触摸屏幕回调
     */
    void onTouchSpeaking();

}
