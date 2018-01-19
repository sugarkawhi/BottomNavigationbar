package me.sugarkawhi.mreader.config;


/**
 * 配置类
 * Created by ZhaoZongyao on 2018/1/11.
 */

public interface IReaderConfig {

    boolean DEBUG = true;
    //电池宽
    int DEFAULT_BATTERY_WIDTH = 60;
    //电池高
    int DEFAULT_BATTERY_HEIGHT = 20;
    //电池头
    int DEFAULT_BATTERY_HEAD = 10;
    //电池内边距
    int DEFAULT_BATTERY_GAP = 3;
    //内容文字大小
    int DEFAULT_CONTENT_TEXTSIZE = 50;
    //头部(包括底部 i.时间 ii.进度 )文字大小
    int DEFAULT_HEADER_TEXTSIZE = 30;
    //字间距
    int DEFAULT_CONTENT_LETTER_SPACING = 3;
    //行间距
    int DEFAULT_CONTENT_LINE_SPACING = 20;
    //段间距
    int DEFAULT_CONTENT_PARAGRAPH_SPACING = 40;

}
