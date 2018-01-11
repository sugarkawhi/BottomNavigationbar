package me.sugarkawhi.mreader.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import me.sugarkawhi.mreader.element.PageElement;

/**
 * ReaderView
 * Created by ZhaoZongyao on 2018/1/11.
 */

public class MReaderView extends BaseReaderView {

    public MReaderView(Context context) {
        super(context);
    }

    public MReaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MReaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
