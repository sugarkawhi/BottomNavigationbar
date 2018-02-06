package com.monster.monstersport.http.api;


/**
 * 网络请求 配置
 *
 * @author zhzy
 * @date 2017/11/2
 */
public interface ApiConfig {

    /**
     * 状态码 0--失败
     */
    int CODE_FAILURE = 0;
    /**
     * 状态码 1--成功
     */
    int CODE_SUCCESS = 1;
    /**
     * 状态码 2--重复提交
     */
    int CODE_REPET = 2;
    /**
     * 状态码 70--账户余额不足
     */
    int PAY_ACCOUNT_LACK = 70;
    /**
     * 状态码 500--服务器异常
     */
    int SERVER_ERROR = 500;

    /*定义是/否的常量*/
    public static class YesOrNo {
        public static final Integer YES = 1;
        public static final Integer NO = 0;
        public static final String YES_S = "1";
        public static final String NO_S = "0";

        public static Integer intValue(Boolean bool) {
            return bool ? YES : NO;
        }

        public static String strValue(Boolean bool) {
            return bool ? YES_S : NO_S;
        }

        public static boolean bool(Integer arg) {
            return YES.equals(arg);
        }

        public static boolean bool(String arg) {
            return YES_S.equals(arg) || "true".equals(arg);
        }
    }


    interface AD_TYPE {
        int BANNER = 2;//书库BANNER
        int SPLASH = 6;//闪屏页广告
        int SPECIAL = 7;// 书库页专题
        int RECOMMEND = 8;// 推荐页
    }

    /*长短篇标识Storytype 1:短篇  2:长篇*/
    interface StoryType {
        int LONG_STORY = 2;
        int SHORT_STORY = 1;
    }

    /**
     * 评论和回复类型
     * type 评论类型（1短篇2长篇3回复）
     */
    interface COMMENT_TYPE {
        int STORY = 2;// 长篇
        int REPLY = 3;// 回复
    }

    /**
     * 评论渠道（1书籍详情页，2评论列表页，3评论详情页）
     */
    interface COMMENT_CHANNEL {
        int BOOK_DETAIL = 1;// 1书籍详情页
        int COMMENT_LIST = 2;// 2评论列表页
        int COMMENT_DETAIL = 3;// 3评论详情页
    }


    /**
     * 发送验证码场景
     */
    interface SendCode_Scene {
        int REGISTER = 1;// 注册验证码
        int RETRIEVE_PWD = 2;// 找回密码验证码
        int MODIFY_PWD = 3;// 修改密码验证码
    }

    /**
     * 注册渠道
     */
    interface Register_Channel {
        /*3-Android*/
        Integer ANDROID = 3;
        /*4-iOS*/
        Integer IOS = 4;
    }

    /**
     * 书籍章节是否免费
     */
    interface YES_NO {
        int Y = 1;
        int N = 0;
    }


    /**
     * 性别
     */
    interface GENDER {
        int MAN = 1; //男
        int WOMAN = 0;// 女
    }

    /**
     * 用户类型
     */
    interface UserType {
        /*1-普通用户需要密码的*/
        int NORMAL = 1;
        /*2-微信用户*/
        int WEICHAT = 2;
        /*3-qq用户*/
        int QQ = 3;
    }

    /**
     * 账户记录
     */
    interface ACCOUNT_LOG {
        /* 账务类型1--支出消费 */
        int OUT = 1;
        /*账务类型2--充值入账 */
        int RECHARGE = 2;
        /*账务类型3--获赠入账 */
        int GAIN = 3;
    }

    /**
     * 消息类型
     */
    interface MESSAGE_INFO {
        //1系统消息，
        int SYSTEM = 1;
        //4书更通知
        int BOOK_UPDATE = 4;
    }

    /**
     * 评论列表排序
     * 排序类型（1最热，2最新，默认最新）
     */
    interface COMMENT_ORDER_TYPE {
        int POPULAR = 1;
        int NEW = 2;
    }

    /**
     * 版本平台类型
     */
    interface Platform {
        int Android = 1;
        int iOS = 2;
    }

    /**
     * 支付方式
     */
    interface PayWay {
         /*3-支付宝客户端 */
        int ALIPAY = 3;
        /*4-微信 */
        int WECHAT = 4;
    }
}
