package me.sugarkawhi.mreader.manager;

import android.graphics.Paint;
import android.util.Log;

import org.apache.commons.lang3.ArrayUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.sugarkawhi.mreader.bean.ChapterBean;
import me.sugarkawhi.mreader.data.PageData;

/**
 * p m
 * Created by ZhaoZongyao on 2018/1/12.
 */

public class PageManager {

    private static final String TAG = "PageManager";
    private static final char PARAGRAPH = '\n';
    private static final char[] NO_LINE_HEADER_FULLCHAR = {'，', '；', '：', '、', '。', '！', '？', '》', ']', '）'};
    public static final char[] NO_LINE_HEADER = {',', '，', ';', '；', ':', '：', '、', '.', '。', '!', '！', '?', '？', '”', '>', '》', ']', ')', '）', '}'};
    public static final char[] NO_LINE_TAIL = {'“', '<', '《', '[', '(', '（', '{'};

    private float mContentHeight;
    private float mContentWidth;
    private float mLineSpacing;
    private float mParagraphSpacing;
    private Paint mTextPaint;

    public PageManager(float contentWidth, float contentHeight, float lineSpacing, float paragraphSpacing, Paint contentPaint) {
        mContentWidth = contentWidth;
        mContentHeight = contentHeight;
        mLineSpacing = lineSpacing;
        mParagraphSpacing = paragraphSpacing;
        mTextPaint = contentPaint;
    }

    /**
     * 分页
     *
     * @param chapter
     * @param br
     * @return
     */
    public List<PageData> generatePages(ChapterBean chapter, BufferedReader br) {
        //生成的页面
        List<PageData> pages = new ArrayList<>();
        //使用流的方式加载
        List<String> lines = new ArrayList<>();
        float rHeight = mContentHeight;
        String paragraph;//默认展示标题
        try {
            while ((paragraph = br.readLine()) != null) {
                //重置段落
                if (paragraph.equals("")) continue;
                //手动添加换行符  BufferedReader据'\n'读取一行
                paragraph = paragraph + "\n";
                int wordCount = 0;
                String subStr = null;
                //段落进行分行
                while (paragraph.length() > 0) {
                    if (paragraph.equals("\n")) {
                        lines.add(paragraph);
                        break;
                    }
                    //当前空间，是否容得下一行文字
                    rHeight -= mTextPaint.getFontSpacing();
                    //一页已经填充满了，创建 TextPage
                    if (rHeight < 0) {
                        //创建Page
                        PageData page = new PageData();
                        page.setIndexOfChapter(pages.size());
                        page.setChapterName(chapter.getChapterName());
                        page.setLines(new ArrayList<>(lines));
                        pages.add(page);
                        //重置Lines
                        lines.clear();
                        rHeight = mContentHeight;
                        continue;
                    }
                    //测量一行占用的字节数
                    wordCount = mTextPaint.breakText(paragraph, true, mContentWidth, null);
                    if (wordCount < paragraph.length()) {
                        char c = paragraph.charAt(wordCount);
                        if (ArrayUtils.contains(NO_LINE_HEADER, c)) {
                            wordCount++;//往前挪一位
                        }
                    }
                    subStr = paragraph.substring(0, wordCount);
                    lines.add(subStr);
                    //设置行间距
                    rHeight -= mLineSpacing;
                    //裁剪
                    paragraph = paragraph.substring(wordCount);
                }
                //段落分行完成
                if (lines.size() != 0) {
                    rHeight = rHeight - mParagraphSpacing;
                }
                Log.e(TAG, "rHeight = " + rHeight);
            }
            if (lines.size() != 0) {
                //创建Page
                PageData page = new PageData();
                page.setIndexOfChapter(pages.size());
                page.setChapterName(chapter.getChapterName());
                page.setLines(new ArrayList<>(lines));
                pages.add(page);
                //重置Lines
                lines.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //可能出现内容为空的情况
        if (pages.size() == 0) {
            PageData page = new PageData();
            page.setLines(new ArrayList<String>(1));
            pages.add(page);
        }
        return pages;
    }

}
