package com.monster.monstersport.persistence;

import android.content.Context;
import android.text.TextUtils;

import com.monster.monstersport.R;
import com.monster.monstersport.base.MonApplication;
import com.monster.monstersport.dao.bean.BookMarkBean;
import com.monster.monstersport.dao.bean.BookMarkBeanDao;
import com.monster.monstersport.dao.bean.BookRecordBean;
import com.monster.monstersport.dao.bean.BookRecordBeanDao;
import com.monster.monstersport.dao.bean.DaoSession;

import java.util.List;

import me.sugarkawhi.mreader.data.PageData;
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
        //夜间模式
        int NIGHT = 5;
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

    //语音合成
    public interface TTS {

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
    public static void saveBookRecord(String bookId, String chapterId, float progress) {
        BookRecordBean record = queryBookRecord(bookId);
        DaoSession session = MonApplication.getInstance().getDaoSession();
        BookRecordBeanDao dao = session.getBookRecordBeanDao();
        if (record == null) {
            //插入
            record = new BookRecordBean(bookId, chapterId, progress);
            long rowID = dao.insert(record);
            L.e(TAG, "insert one book record chapter=");
        } else {
            //更新
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
        if (TextUtils.isEmpty(bookId)) return null;
        DaoSession session = MonApplication.getInstance().getDaoSession();
        BookRecordBeanDao dao = session.getBookRecordBeanDao();
        List<BookRecordBean> list = dao.queryBuilder()
                .where(BookRecordBeanDao.Properties.BookId.eq(bookId))
                .list();
        if (list == null || list.size() == 0)
            return null;
        return list.get(0);
    }

    /**
     * 保存书签
     */
    public static boolean addBookMark(String bookId, PageData pageData) {
        if (pageData == null) return false;
        BookMarkBean bookMark = queryBookMark(bookId, pageData);
        DaoSession session = MonApplication.getInstance().getDaoSession();
        BookMarkBeanDao dao = session.getBookMarkBeanDao();
        if (bookMark == null) {
            //插入
            long time = System.currentTimeMillis();
            String chapterName = pageData.getChapterName();
            String chapterId = pageData.getChapterId();
            int progress = pageData.getProgress();
            String content = pageData.getContent() + "";
            bookMark = new BookMarkBean();
            bookMark.setBookId(bookId);
            bookMark.setTime(time);
            bookMark.setChapterName(chapterName);
            bookMark.setChapterId(chapterId);
            bookMark.setProgress(progress);
            bookMark.setContent(content);
            long rowID = dao.insert(bookMark);
            L.e(TAG, "insert one book mark chapter=" + rowID);
            return true;
        } else {
            L.e(TAG, "addBookMark has one");
            return false;
        }
    }

    /**
     * 删除书签
     */
    public static void deleteBookMark(String bookId, PageData page) {
        BookMarkBean bookMark = queryBookMark(bookId, page);
        if (bookMark != null) {
            DaoSession session = MonApplication.getInstance().getDaoSession();
            BookMarkBeanDao dao = session.getBookMarkBeanDao();
            dao.deleteByKey(bookMark.get_id());
        }
    }

    /**
     * 查询书签
     *
     * @param bookId   书籍id
     * @param pageData 页面信息
     */
    public static BookMarkBean queryBookMark(String bookId, PageData pageData) {
        if (TextUtils.isEmpty(bookId)) return null;
        if (pageData == null) return null;
        DaoSession session = MonApplication.getInstance().getDaoSession();
        BookMarkBeanDao dao = session.getBookMarkBeanDao();
        List<BookMarkBean> list = dao.queryBuilder()
                .where(BookMarkBeanDao.Properties.BookId.eq(bookId),
                        BookMarkBeanDao.Properties.ChapterId.eq(pageData.getChapterId()),
                        BookMarkBeanDao.Properties.Progress.eq(pageData.getProgress()))
                .list();
        if (list == null || list.size() == 0)
            return null;
        return list.get(0);
    }

    /**
     * 根据书籍id查询本书籍的书签列表
     * <p>
     * 新加进来的在最前边-》时间降序排序
     *
     * @param bookId   书籍id
     * @param pageData 页面信息
     */
    public static List<BookMarkBean> queryBookMarkList(String bookId) {
        if (TextUtils.isEmpty(bookId)) return null;
        DaoSession session = MonApplication.getInstance().getDaoSession();
        BookMarkBeanDao dao = session.getBookMarkBeanDao();
        List<BookMarkBean> list = dao.queryBuilder()
                .where(BookMarkBeanDao.Properties.BookId.eq(bookId))
                .orderDesc(BookMarkBeanDao.Properties.Time)
                .list();
        return list;
    }

}
