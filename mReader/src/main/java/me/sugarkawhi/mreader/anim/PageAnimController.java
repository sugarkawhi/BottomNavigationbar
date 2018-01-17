package me.sugarkawhi.mreader.anim;

import android.graphics.Canvas;
import android.view.MotionEvent;

import me.sugarkawhi.mreader.view.BaseReaderView;

/**
 * page anim controller
 * Created by ZhaoZongyao on 2018/1/11.
 */

public interface PageAnimController {


    void dispatchDrawPage(Canvas canvas);

    void dispatchTouchEvent(MotionEvent event, BaseReaderView baseReaderView);
}
