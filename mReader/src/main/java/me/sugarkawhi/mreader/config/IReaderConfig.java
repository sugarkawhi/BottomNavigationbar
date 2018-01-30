package me.sugarkawhi.mreader.config;


/**
 * 配置类
 * Created by ZhaoZongyao on 2018/1/11.
 */

public interface IReaderConfig {

    boolean DEBUG = true;

    //标题和内容文字大小比例
    float RATIO_CHAPTER_CONTENT = 1.3f;
    //头部(包括底部 i.时间 ii.进度 )文字大小
    int DEFAULT_HEADER_TEXTSIZE = 35;

    interface Cover {
        //封面图片 宽高
        int IMG_WIDTH = 400;
        int IMG_HEIGHT = (int) (IMG_WIDTH * 1.43f);
        //书名 大小
        int FONT_SIZE_BOOKNAME = 60;
        //作者名大小
        int FONT_SIZE_AUTHOR = 60;


    }

    //关于电池的配置
    interface Battery {
        int WIDTH = 60;
        int HEIGHT = 30;
        int HEAD = 6;
        int GAP = 3;
    }

    //字间距
    interface LetterSpacing {
        //最大
        int MAX = 10;
        //默认
        int DEFAULT = 5;
        //最小
        int MIN = 0;
    }

    //行间距
    interface LineSpacing {
        //最大
        int MAX = 40;
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
        int MIN = 20;
    }


    //字体大小
    interface FontSize {
        //最大
        int MAX = 90;
        //默认
        int DEFAULT = 60;
        //最小
        int MIN = 40;
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
        int NODE = 4;
    }


}
