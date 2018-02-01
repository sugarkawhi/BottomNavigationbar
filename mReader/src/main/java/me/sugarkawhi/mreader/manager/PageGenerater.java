package me.sugarkawhi.mreader.manager;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;

import me.sugarkawhi.mreader.data.PageData;
import me.sugarkawhi.mreader.element.PageElement;

/**
 * 页面生成器
 * Created by ZhaoZongyao on 2018/1/22.
 */

public class PageGenerater {

    public static void generate(PageElement pageElement, PageData pageData, Bitmap bitmap) {
        pageElement.generatePage(pageData, bitmap);
    }

}
