package me.sugarkawhi.mreader.manager;

import java.util.ArrayList;
import java.util.List;

import me.sugarkawhi.mreader.data.PageData;

/**
 * 页数库
 * 维护着3个List 包括了当前章节和前后两个章节的页面列表
 * Created by ZhaoZongyao on 2018/2/1.
 */

public class PageRespository {
    private List<PageData> mPrePageList;
    private List<PageData> mCurPageList;
    private List<PageData> mNextPageList;

    private PageData mCurPage;
    private int mCurIndex;


    public PageRespository() {
        mPrePageList = new ArrayList<>();
        mCurPageList = new ArrayList<>();
        mNextPageList = new ArrayList<>();
    }


    /**
     * 获取当前显示页
     *
     * @return cur
     */
    public PageData getCurPage() {
        return mCurPage;
    }


    /**
     * 从[当前章节]中查找[当前页]的下一个页面
     * i：如果当前章节结束了  取下一章节的第一个页面
     * ii： 如果没有下一章节 回调.
     *
     * @return next page
     */
    public PageData getNextPage() {
        int nextIndex = mCurIndex + 1;
        if (nextIndex < mCurPageList.size())
            return mCurPageList.get(nextIndex);
        //当前章节数到末尾了
        if (mNextPageList == null || mNextPageList.size() == 0) {
            return null;
        }
        mCurPageList = mNextPageList;
        mNextPageList.clear();
        mCurIndex = 0;
        return mCurPageList.get(0);
    }

    /**
     * 取消翻页了 - 要回退到上一个索引
     * 涉及到章节间的撤销
     */
    public void cancel() {

    }

    /**
     * 切换章节
     */
    private void switchChapter() {


    }


    public void setCurPageList(List<PageData> curPageList) {
        mCurPageList = curPageList;
        if (curPageList == null || mCurPageList.size() == 0) return;
        mCurPage = mCurPageList.get(0);
    }

    public void setPrePageList(List<PageData> prePageList) {
        mPrePageList = prePageList;
    }

    public void setNextPageList(List<PageData> nextPageList) {
        mNextPageList = nextPageList;
    }
}
