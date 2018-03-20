package me.sugarkawhi.pulltomark;

/**
 * 接口回调
 * Created by ZhaoZongyao on 2018/3/20.
 */

public interface PtmUIHandler {
    /**
     * UI重置
     */
    void onUIReset();

    /**
     * UI 准备
     *
     * @param hasMark 已经添加了书签
     */
    void onUIPrepare(boolean hasMark);

}
