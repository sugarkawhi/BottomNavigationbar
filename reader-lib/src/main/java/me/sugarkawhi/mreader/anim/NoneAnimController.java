package me.sugarkawhi.mreader.anim;


import android.graphics.Canvas;
import android.graphics.Rect;

import me.sugarkawhi.mreader.config.IReaderDirection;
import me.sugarkawhi.mreader.element.PageElement;
import me.sugarkawhi.mreader.utils.L;
import me.sugarkawhi.mreader.view.ReaderView;

/**
 * None page anim controller
 * Created by ZhaoZongyao on 2018/1/11.
 */

public class NoneAnimController extends PageAnimController {


    public NoneAnimController(ReaderView readerView, int readerWidth, int readerHeight, PageElement pageElement, IPageChangeListener pageChangeListener) {
        super(readerView, readerWidth, readerHeight, pageElement, pageChangeListener);
    }


    @Override
    void drawStatic(Canvas canvas) {
        canvas.drawBitmap(mCurrentBitmap, 0, 0, null);
    }

    @Override
    void drawMove(Canvas canvas) {
        canvas.drawBitmap(mCurrentBitmap, 0, 0, null);
    }

}
