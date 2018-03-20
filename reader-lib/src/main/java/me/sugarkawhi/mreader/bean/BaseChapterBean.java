package me.sugarkawhi.mreader.bean;

import java.io.Serializable;

/**
 * 章节
 * Created by ZhaoZongyao on 2018/1/11.
 */

public class BaseChapterBean implements Serializable {

    private int hasNext;
    private int hasPre;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isFirstChapter() {
        return hasPre == 0;
    }

    public boolean isLastChapter() {
        return hasNext == 0;
    }

    private String chapterid;
    private String createtime;
    private String content;
    private int dorder;
    private String edittime;
    private int isedit;
    private int isfree;
    private String name;
    private int price;
    private String storyid;
    private int tab;
    private int words;
    private String authorsay;
    private int isbuysult;

    public String getChapterid() {
        return chapterid;
    }

    public void setChapterid(String chapterid) {
        this.chapterid = chapterid;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public int getDorder() {
        return dorder;
    }

    public void setDorder(int dorder) {
        this.dorder = dorder;
    }

    public String getEdittime() {
        return edittime;
    }

    public void setEdittime(String edittime) {
        this.edittime = edittime;
    }

    public int getIsedit() {
        return isedit;
    }

    public void setIsedit(int isedit) {
        this.isedit = isedit;
    }

    public int getIsfree() {
        return isfree;
    }

    public void setIsfree(int isfree) {
        this.isfree = isfree;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getStoryid() {
        return storyid;
    }

    public void setStoryid(String storyid) {
        this.storyid = storyid;
    }

    public int getTab() {
        return tab;
    }

    public void setTab(int tab) {
        this.tab = tab;
    }

    public int getWords() {
        return words;
    }

    public void setWords(int words) {
        this.words = words;
    }

    @Override
    public int hashCode() {
        return chapterid.hashCode();
    }


    public String getAuthorsay() {
        return authorsay;
    }

    public void setAuthorsay(String authorsay) {
        this.authorsay = authorsay;
    }

    public int getIsbuysult() {
        return isbuysult;
    }


}
