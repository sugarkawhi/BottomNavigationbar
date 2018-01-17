package me.sugarkawhi.mreader.listener;

/**
 * reader touch listener
 * Created by ZhaoZongyao on 2018/1/17.
 */

public interface IReaderTouchListener {

    /**
     * touch center
     */
    void onTouchCenter();

    /**
     * touch right
     */
    boolean onTouchRight();

    /**
     * touch left
     */
    boolean onTouchLeft();
}
