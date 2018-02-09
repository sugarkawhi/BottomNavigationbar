package com.monster.monstersport.dao.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 保存书籍浏览记录
 * Created by ZhaoZongyao on 2018/2/6.
 */
@Entity
public class BookRecordBean {

    @Id
    private String bookId;
    //当前章节的id
    private String chapterId;
    //当前章节进度
    private float progress;
    @Generated(hash = 1119842043)
    public BookRecordBean(String bookId, String chapterId, float progress) {
        this.bookId = bookId;
        this.chapterId = chapterId;
        this.progress = progress;
    }
    @Generated(hash = 398068002)
    public BookRecordBean() {
    }
    public String getBookId() {
        return this.bookId;
    }
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
    public String getChapterId() {
        return this.chapterId;
    }
    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }
    public float getProgress() {
        return this.progress;
    }
    public void setProgress(float progress) {
        this.progress = progress;
    }



}
