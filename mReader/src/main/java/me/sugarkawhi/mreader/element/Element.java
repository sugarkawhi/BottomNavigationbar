package me.sugarkawhi.mreader.element;

import android.graphics.Canvas;

/**
 * 分页模块：功能包括将传入的章节数据分成数个 PageData
 * (生成的 PageData 个数即为该章节页数，PageData 记录了每一页开头文字在章节的位置，
 * 同时包含该页面HeaderData, LineData,ImageData 和 FooterData 数据等。
 * 各个 Data 里面记录了相应的文字信息，可以快速的定位到章节内容中。)；绘制页面；
 * Created by ZhaoZongyao on 2018/1/11.
 */

public abstract class Element {


    public abstract void onDraw(Canvas canvas);


}
