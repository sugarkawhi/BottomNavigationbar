package me.sugarkawhi.mreader.manager;

import android.graphics.Bitmap;

import java.util.List;

import me.sugarkawhi.mreader.bean.BaseChapterBean;
import me.sugarkawhi.mreader.config.IReaderDirection;
import me.sugarkawhi.mreader.data.PageData;
import me.sugarkawhi.mreader.element.PageElement;
import me.sugarkawhi.mreader.listener.IReaderChapterChangeListener;

/**
 * 页数库
 * 维护着3个List 包括了当前章节和前后两个章节的页面列表
 * Created by ZhaoZongyao on 2018/2/1.
 */

public class PageRespository {

    private static final String TAG = "PageRespository";
    private IReaderChapterChangeListener mChapterChangeListener;
    private PageElement mPageElement;
    private BaseChapterBean mCurChapter, mPreChapter, mNextChapter;
    private List<PageData> mCurPageList, mPrePageList, mNextPageList;

    //当前正在显示的page
    private PageData mCurPage;
    //如果取消了 保留一份上一个Page的备份
    private PageData mCancelPage;
    private int mCurIndex;

    //当前章节的浏览位置 为一个百分数
    private float progress;

    public PageRespository(PageElement pageElement) {
        mPageElement = pageElement;
    }

    public void setChapterChangeListener(IReaderChapterChangeListener chapterChangeListener) {
        mChapterChangeListener = chapterChangeListener;
    }

    public void setCurChapter(BaseChapterBean curChapter) {
        mCurChapter = curChapter;
    }

    public void setPreChapter(BaseChapterBean preChapter) {
        mPreChapter = preChapter;
    }

    public void setNextChapter(BaseChapterBean nextChapter) {
        mNextChapter = nextChapter;
    }

    public BaseChapterBean getCurChapter() {
        return mCurChapter;
    }

    public BaseChapterBean getPreChapter() {
        return mPreChapter;
    }

    public BaseChapterBean getNextChapter() {
        return mNextChapter;
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
     * @return hasNext
     */
    public boolean next(Bitmap curBitmap, Bitmap nextBitmap) {
        if (mCurPageList == null) return false;
        int nextIndex = mCurIndex + 1;
        PageData nextPage = null;
        if (nextIndex < mCurPageList.size()) {
            mCurIndex++;
            nextPage = mCurPageList.get(nextIndex);
        }
        //当前章节数到末尾了
        if (nextPage == null) {
            nextPage = getNextPageFromNextChapter();
            if (nextPage == null) return false;
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
     * 到本章的最后一页了
     * 从下一章节取下一页
     */
    private PageData getNextPageFromNextChapter() {
        //当前章节都没有
        if (mCurChapter == null) return null;
        //最后一章
        if (mCurChapter.isLastChapter()) {
            if (mChapterChangeListener != null)
                mChapterChangeListener.onReachLastChapter();
            return null;
        }
        //非最后一章  但是下一章无数据
        if (mNextChapter == null || mNextPageList == null || mNextPageList.isEmpty()) {
            if (mChapterChangeListener != null)
                mChapterChangeListener.onNoNextPage(mCurChapter);
            return null;
        }
        return mNextPageList.get(0);
    }


    /**
     * 动画结束选中了一页
     *
     * @param direction 方向
     * @param isCancel  是否取消了
     */
    public void onSelectPage(int direction, boolean isCancel) {
        if (isCancel) {
            mCurPage = mCancelPage;
        } else {
            int index = mCurPage.getIndexOfChapter();
            switch (direction) {
                case IReaderDirection.NEXT://TODO 处理是否是翻到了下一章的第一页
                    // 翻到下页 还是第一页 毫无疑问切换章节了
                    if (index == 0) {
                        switchNextChapter();
                        if (mChapterChangeListener != null)
                            mChapterChangeListener.onProgressChange(0);
                    } else {
                        if (mChapterChangeListener != null)
                            mChapterChangeListener.onProgressChange((index + 1f) / mCurPageList.size());
                    }
                    break;
                case IReaderDirection.PRE: //处理是否翻到了上一章的最后一页
                    //翻到上页 并且之前一页(当前页的下一页)的索引是0 毫无疑问切换章节了
                    int cancelIndex = mCancelPage.getIndexOfChapter();
                    if (cancelIndex == 0) {
                        switchPreChapter();
                        if (mChapterChangeListener != null)
                            mChapterChangeListener.onProgressChange(1);
                    } else {
                        if (mChapterChangeListener != null)
                            mChapterChangeListener.onProgressChange((index + 1f) / mCurPageList.size());
                    }
                    break;
            }
        }
    }

    /**
     * 切换到下一章节
     */
    private void switchNextChapter() {
        mPreChapter = mCurChapter;
        mCurChapter = mNextChapter;
        mNextChapter = null;

        mPrePageList = mCurPageList;
        mCurPageList = mNextPageList;
        mNextPageList = null;

        mCurIndex = 0;

        if (mChapterChangeListener != null)
            mChapterChangeListener.onChapterChange(mCurChapter, IReaderDirection.NEXT);
    }

    /**
     * 切换到上一章节
     */
    private void switchPreChapter() {
        mNextChapter = mCurChapter;
        mCurChapter = mPreChapter;
        mPreChapter = null;

        mNextPageList = mCurPageList;
        mCurPageList = mPrePageList;
        mPrePageList = null;

        mCurIndex = mCurPageList.size() - 1;

        if (mChapterChangeListener != null)
            mChapterChangeListener.onChapterChange(mCurChapter, IReaderDirection.PRE);
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
            prePage = getPrePageFromLastChapter();
            if (prePage == null) return false;
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
     * 从前一章节取前一页
     */
    private PageData getPrePageFromLastChapter() {
        //当前章节都没有
        if (mCurChapter == null) return null;
        //第一章
        if (mCurChapter.isFirstChapter()) {
            if (mChapterChangeListener != null)
                mChapterChangeListener.onReachFirstChapter();
            return null;
        }
        //非第一章  但是前一章一章无数据
        if (mPreChapter == null || mPrePageList == null || mPrePageList.isEmpty()) {
            if (mChapterChangeListener != null)
                mChapterChangeListener.onNoPrePage(mCurChapter);
            return null;
        }
        return mPrePageList.get(mPrePageList.size() - 1);
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


    public void setCurPageList(List<PageData> curPageList) {
        if (curPageList == null) return;
        mCurPageList = curPageList;
    }

    public void setCurPage(float progress) {
        if (mCurPageList == null || mCurPageList.size() == 0) return;
        mCurIndex = (int) ((mCurPageList.size() - 1) * progress);
        mCurPage = mCurPageList.get(mCurIndex);
    }

    public void setPrePageList(List<PageData> prePageList) {
        if (prePageList == null) return;
        mPrePageList = prePageList;
    }

    public void setNextPageList(List<PageData> nextPageList) {
        if (nextPageList == null) return;
        mNextPageList = nextPageList;
    }

    /**
     * * 设置当前章节的进度
     * 1.首先要知道当前章节的总页数
     * 2.用总页数乘以这百分比 得出当前页数
     *
     * @param progress 进度 >=0 并且<=1
     */
    public void setChapterProgress(float progress) {
        if (mCurPageList == null || mCurPageList.isEmpty()) return;
        mCurIndex = (int) ((getCurrentChapterPageNum() - 1) * progress);
        mCurPage = mCurPageList.get(mCurIndex);
    }

    /**
     * 获取当前章节的总页数
     *
     * @return Num
     */
    private int getCurrentChapterPageNum() {
        return mCurPageList == null ? 0 : mCurPageList.size();
    }

    /**
     * 直接进行下一章
     * TODO 逻辑待处理
     */
    public void directNextChapter() {
        if (mNextPageList == null || mNextPageList.isEmpty()) return;
        mCurPageList = mNextPageList;
        mNextPageList = null;
        mCurIndex = 0;
        mCurPage = mCurPageList.get(mCurIndex);
        //
        //设置章节切换
        mPreChapter = mCurChapter;
        mCurChapter = mNextChapter;
        mNextChapter = null;
        if (mChapterChangeListener != null)
            mChapterChangeListener.onChapterChange(mCurChapter, IReaderDirection.NEXT);
    }

    /**
     * 直接进行上一章
     * TODO 逻辑待处理
     */
    public void directPreChapter() {
        if (mPrePageList == null || mPrePageList.isEmpty()) return;
        mCurPageList = mPrePageList;
        mPrePageList = null;
        mCurIndex = 0;
        mCurPage = mCurPageList.get(mCurIndex);
        //设置章节切换
        mNextChapter = mCurChapter;
        mCurChapter = mPreChapter;
        mPreChapter = null;
        if (mChapterChangeListener != null)
            mChapterChangeListener.onChapterChange(mCurChapter, IReaderDirection.PRE);
    }

    /**
     * 返回阅读进度 当前章节
     *
     * @return 进度
     */
    public float getReadingProgress() {
        if (mCurPageList == null || mCurPageList.isEmpty() || mCurPage == null) {
            progress = 0;
        } else {
            progress = (mCurPage.getIndexOfChapter() + 1) * 1f / mCurPageList.size();
        }
        return progress;
    }

    /**
     * 自动进入下一页
     *
     * @return 返回下一页的数据
     */
    public PageData nextPage() {
        //TODO 进入下一章了
        if (mCurIndex == mCurPageList.size() - 1) {
            return null;
        } else {
            mCurIndex++;
            mCurPage = mCurPageList.get(mCurIndex);
            return mCurPage;
        }
    }
}
