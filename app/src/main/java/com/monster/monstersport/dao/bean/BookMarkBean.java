package com.monster.monstersport.dao.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 书签
 * Created by ZhaoZongyao on 2018/3/15.
 */
@Entity
public class BookMarkBean {
    private String bookId;
    private long time;
    private String chapterName;
    private String chapterId;
    private int progress;
    private String content;
    @Generated(hash = 2056223125)
    public BookMarkBean(String bookId, long time, String chapterName,
            String chapterId, int progress, String content) {
        this.bookId = bookId;
        this.time = time;
        this.chapterName = chapterName;
        this.chapterId = chapterId;
        this.progress = progress;
        this.content = content;
    }
    @Generated(hash = 237936453)
    public BookMarkBean() {
    }
    public String getBookId() {
        return this.bookId;
    }
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
    public long getTime() {
        return this.time;
    }
    public void setTime(long time) {
        this.time = time;
    }
    public String getChapterName() {
        return this.chapterName;
    }
    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }
    public String getChapterId() {
        return this.chapterId;
    }
    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }
    public int getProgress() {
        return this.progress;
    }
    public void setProgress(int progress) {
        this.progress = progress;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }

}
