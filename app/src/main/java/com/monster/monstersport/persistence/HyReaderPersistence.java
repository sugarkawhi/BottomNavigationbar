package com.monster.monstersport.persistence;

import android.content.Context;

import com.monster.monstersport.R;
import com.monster.monstersport.base.MonApplication;
import com.monster.monstersport.dao.bean.BookRecordBean;
import com.monster.monstersport.dao.bean.BookRecordBeanDao;
import com.monster.monstersport.dao.bean.DaoSession;

import java.util.List;

import me.sugarkawhi.mreader.persistence.IReaderPersistence;
import me.sugarkawhi.mreader.utils.L;

/**
 * 本地持久化
 * Created by ZhaoZongyao on 2018/1/30.
 */

public class HyReaderPersistence extends IReaderPersistence {

    //背景
    public interface Background {
        //默认
        int DEFAULT = 1;
        //图片-蓝色
        int IMAGE_BLUE = 2;
        //图片-紫色
        int IMAGE_PURPLE = 3;
        //纯色-抹茶
        int COLOR_MATCHA = 4;
    }

    //字体颜色 对应于背景
    public interface FontColor {
        //默认
        int DEFAULT = R.color.reader_font_default;
        //对应于蓝色背景
        int BLUE = R.color.reader_font_blue;
        //对应于紫色背景
        int PURPLE = R.color.reader_font_purple;
        //对应于抹茶色背景
        int MATCHA = R.color.reader_font_matcha;
    }

    /**
     * 获取背景
     */
    public static int getBackground(Context context) {
        return getSP(context).getInt(READER_BACKGROUND, Background.DEFAULT);
    }

    /**
     * 保存背景
     */
    public static void saveBackground(Context context, int background) {
        getSP(context).edit().putInt(READER_BACKGROUND, background).apply();
    }

    /**
     * 获取字体颜色
     */
    public static int getFontColor(Context context) {
        return getSP(context).getInt(READER_FONT_COLOR, FontColor.DEFAULT);
    }

    /**
     * 保存字体颜色
     */
    public static void saveFontColor(Context context, int fontColor) {
        getSP(context).edit().putInt(READER_FONT_COLOR, fontColor).apply();
    }


    /**
     * 保存书籍浏览记录
     *
     * @param bookId   书籍id
     * @param chapter  章节索引
     * @param position 章节位置
     */
    public static void saveBookRecord(String bookId, String chapterId, int chapterIndex, float progress) {
        BookRecordBean record = queryBookRecord(bookId);
        DaoSession session = MonApplication.getInstance().getDaoSession();
        BookRecordBeanDao dao = session.getBookRecordBeanDao();
        if (record == null) {
            //插入
            record = new BookRecordBean(bookId, chapterId, chapterIndex, progress);
            long rowID = dao.insert(record);
            L.e(TAG, "insert one book record chapter=");
        } else {
            //更新
            record.setChapterIndex(chapterIndex);
            record.setChapterId(chapterId);
            record.setProgress(progress);
            dao.update(record);
            L.e(TAG, "update one book record chapter=");
        }
    }


    /**
     * 获取书籍浏览[章节]及其[位置]
     *
     * @param bookId 书籍id
     */
    public static BookRecordBean queryBookRecord(String bookId) {
        DaoSession session = MonApplication.getInstance().getDaoSession();
        BookRecordBeanDao dao = session.getBookRecordBeanDao();
        List<BookRecordBean> list = dao.queryBuilder()
                .where(BookRecordBeanDao.Properties.BookId.eq(bookId))
                .list();
        if (list == null || list.size() == 0)
            return null;
        return list.get(0);
    }
}
