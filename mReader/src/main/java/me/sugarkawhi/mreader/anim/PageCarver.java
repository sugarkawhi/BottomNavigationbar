package me.sugarkawhi.mreader.anim;

import android.graphics.Canvas;

/**
 * Created by ZhaoZongyao on 2018/1/17.
 */

public interface PageCarver {

    void drawPage(Canvas canvas, int index);//绘制页内容

    int requestPrePage();//请求翻到上一页

    int requestNextPage();//请求翻到下一页

    void requestInvalidate();//刷新界面

    int getCurrentPageIndex();//获取当前页

    /**
     * 开始动画的回调
     *
     * @param isCancel 是否是取消动画
     */
    void onStartAnim(boolean isCancel);

    /**
     * 结束动画的回调
     *
     * @param isCancel 是否是取消动画
     */
    void onStopAnim(boolean isCancel);
}
