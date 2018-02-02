package me.sugarkawhi.mreader.manager;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

import me.sugarkawhi.mreader.config.IReaderDirection;
import me.sugarkawhi.mreader.data.PageData;
import me.sugarkawhi.mreader.element.PageElement;

/**
 * 页数库
 * 维护着3个List 包括了当前章节和前后两个章节的页面列表
 * Created by ZhaoZongyao on 2018/2/1.
 */

public class PageRespository {

    private PageElement mPageElement;
    private List<PageData> mPrePageList;
    private List<PageData> mCurPageList;
    private List<PageData> mNextPageList;

    //当前正在显示的page
    private PageData mCurPage;
    //如果取消了 保留一份上一个Page的备份
    private PageData mCancelPage;
    private int mCurIndex;


    public PageRespository(PageElement pageElement) {
        mPageElement = pageElement;
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

    public PageData getCancelPage() {
        return mCancelPage;
    }

    /**
     * 从[当前章节]中查找[当前页]的下一个页面
     * i：如果当前章节结束了  取下一章节的第一个页面
     * ii： 如果没有下一章节 回调.
     *
     * @return hasNext
     */
    public boolean next(Bitmap curBitmap, Bitmap nextBitmap) {
        int nextIndex = mCurIndex + 1;
        PageData nextPage = null;
        if (nextIndex < mCurPageList.size()) {
            mCurIndex++;
            nextPage = mCurPageList.get(nextIndex);
        }
        if (nextPage == null) {
            //当前章节数到末尾了
            if (mNextPageList == null || mNextPageList.size() == 0) {
                return false;
            } else {
                mCurPageList = mNextPageList;
                mCurIndex = 0;
                nextPage = mCurPageList.get(0);
            }
        }
        mCancelPage = mCurPage;
        mCurPage = nextPage;
        //绘制左边的图
        PageGenerater.generate(mPageElement, mCancelPage, curBitmap);
        //绘制右边的bitmap
        PageGenerater.generate(mPageElement, mCurPage, nextBitmap);
        return true;
    }

    /**
     * 从[当前章节]中查找[当前页]的上一个页面
     * i：如果当前章节结束了  取下一章节的第一个页面
     * ii： 如果没有下一章节 回调.
     *
     * @return hasPre
     */
    public boolean pre(Bitmap curBitmap, Bitmap nextBitmap) {
        int preIndex = mCurIndex - 1;
        PageData prePage = null;
        if (preIndex >= 0) {
            mCurIndex--;
            prePage = mCurPageList.get(preIndex);
        }
        if (prePage == null) {
            //当前章节数到第一页了
            if (mPrePageList == null || mPrePageList.size() == 0) {
                return false;
            } else {
                //TODO 这里逻辑待处理
                mCurPageList = mPrePageList;
                mCurIndex = mCurPageList.size() - 1;
                prePage = mCurPageList.get(mCurIndex);
            }
        }
        mCancelPage = mCurPage;
        mCurPage = prePage;
        //绘制左边的图
        PageGenerater.generate(mPageElement, mCurPage, curBitmap);
        //绘制右边的bitmap
        PageGenerater.generate(mPageElement, mCancelPage, nextBitmap);
        return true;
    }


    /**
     * 取消翻页了 - 要回退到上一个索引
     * 涉及到章节间的撤销
     */
    public void cancel(int direction) {
        mCurPage = mCancelPage;
        switch (direction) {
            case IReaderDirection.NEXT://向下翻页取消了 TODO 章节间的翻页 翻到下一章节的第一页
                mCurIndex--;
                break;
            case IReaderDirection.PRE://向下翻页
                mCurIndex++;
                break;
        }
    }

    /**
     * 切换章节
     */
    private void switchChapter() {


    }


    public void setCurPageList(List<PageData> curPageList) {
        if (curPageList == null) return;
        mCurPageList = curPageList;
        if (mCurPageList.size() == 0) return;
        mCurPage = mCurPageList.get(0);
    }

    public void setPrePageList(List<PageData> prePageList) {
        if (prePageList == null) return;
        mPrePageList = prePageList;
    }

    public void setNextPageList(List<PageData> nextPageList) {
        if (nextPageList == null) return;
        mNextPageList = nextPageList;
    }
}
