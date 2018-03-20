package me.sugarkawhi.mreader.config;


/**
 * 配置类
 * Created by ZhaoZongyao on 2018/1/11.
 */

public interface IReaderConfig {

    boolean DEBUG = true;

    //翻页动画时长
    int DURATION_PAGE_SWITCH = 300;
    //标题和内容文字大小比例
    float RATIO_CHAPTER_CONTENT = 1.2f;
    //头部(包括底部 i.时间 ii.进度 )文字大小
    int DEFAULT_HEADER_TEXTSIZE = 35;
    //章节名
    int CHAPTER_NAME_MARGIN = 150;

    //关于电池的配置
    interface Battery {
        int WIDTH = 59;
        int HEIGHT = 29;
        float HEAD = 5.5f;
        float GAP = 4f;
        float RADIUS = 3.5f;
    }

    //字间距
    interface LetterSpacing {
        //最大
        int MAX = 15;
        //默认
        int DEFAULT = 5;
        //最小
        int MIN = 0;
    }

    //行间距
    interface LineSpacing {
        //最大
        int MAX = 50;
        //默认
        int DEFAULT = 20;
        //最小
        int MIN = 0;
    }

    //段间距
    interface ParagraphSpacing {
        //最大
        int MAX = 100;
        //默认
        int DEFAULT = 60;
        //最小
        int MIN = 0;
    }


    //字体大小
    interface FontSize {
        //最大
        int MAX = 120;
        //默认
        int DEFAULT = 60;
        //最小
        int MIN = 50;
    }

    //翻页模式
    interface PageMode {
        //仿真
        int SIMULATION = 1;
        //覆盖
        int COVER = 2;
        //滑动
        int SLIDE = 3;
        //无
        int NONE = 4;
    }

    //发音人
    //百度文档： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
    interface Speaker {
        // 普通女声
        int FEMALE = 0;
        // 普通男声
        int MALE = 1;
        // 度丫丫
        int DUXY = 3;
        // 度逍遥
        int DUYY = 4;
    }


}
