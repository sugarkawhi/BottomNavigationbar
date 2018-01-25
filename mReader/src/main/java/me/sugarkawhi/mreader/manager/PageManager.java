package me.sugarkawhi.mreader.manager;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import org.apache.commons.lang3.ArrayUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.sugarkawhi.mreader.bean.ChapterBean;
import me.sugarkawhi.mreader.data.LineData;
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

    //内容宽高
    private float mContentHeight;
    private float mContentWidth;
    //字间距
    private float mLetterSpacing;
    //行间距
    private float mLineSpacing;
    //段间距
    private float mParagraphSpacing;
    //章节名多行间距
    private float mChapterNameSpacing;
    //章节名 顶部、底部 间距
    private float mChapterNameMargin;


    private Paint mContentPaint;
    private Paint mChapterNamePaint;

    public PageManager(float contentWidth, float contentHeight,
                       float letterSpacing, float lineSpacing, float paragraphSpacing,
                       float chapterNameSpacing, float chapterNameMargin,
                       Paint contentPaint, Paint chapterNamePaint) {
        mContentWidth = contentWidth;
        mContentHeight = contentHeight;
        mLetterSpacing = letterSpacing;
        mLineSpacing = lineSpacing;
        mParagraphSpacing = paragraphSpacing;
        mChapterNameSpacing = chapterNameSpacing;
        mChapterNameMargin = chapterNameMargin;
        mContentPaint = contentPaint;
        mChapterNamePaint = chapterNamePaint;
    }

    /**
     * 分页 比较耗时
     *
     * @param chapter
     * @param br
     * @return
     */
    @SuppressLint("DefaultLocale")
    public List<PageData> generatePages(ChapterBean chapter, BufferedReader br) {
        //生成的页面
        List<PageData> pages = new ArrayList<>();
        //使用流的方式加载
        List<LineData> lines = new ArrayList<>();
//        LineData lineData;
        float rHeight = mContentHeight;
        boolean isChapterName = true;
        String paragraph = chapter.getChapterName();//默认展示标题
        try {
            while (isChapterName || (paragraph = br.readLine()) != null) {
                //重置段落
                if (paragraph.equals("")) continue;
                if (isChapterName) {
                    //设置Title的顶部间距
                    rHeight -= mChapterNameMargin;
                } else {
                    //手动添加换行符  BufferedReader据'\n'读取一行
                    paragraph = paragraph + "\n";
                }

                //段落进行分行
                while (paragraph.length() > 0) {
                    if (paragraph.equals("\n")) {
                        break;
                    }
                    //当前空间，是否容得下一行文字
                    if (isChapterName) {
                        rHeight -= mChapterNamePaint.getFontSpacing();
                    } else {
                        rHeight -= mContentPaint.getFontSpacing();
                    }
                    //一页已经填充满了，创建 TextPage
                    if (rHeight < 0) {
                        //创建Page
                        PageData page = new PageData();
                        page.setIndexOfChapter(pages.size());
                        page.setLines(new ArrayList<>(lines));
                        pages.add(page);
                        if (pages.size() == 1) {
                            page.setChapterName(chapter.getBookName());
                        } else {
                            page.setChapterName(chapter.getChapterName());
                        }
                        //重置Lines
                        lines.clear();
                        rHeight = mContentHeight;
                        continue;
                    }
                    //测量一行占用的字节数
                    LineData lineData = subLine(isChapterName, paragraph);
                    lineData.setOffsetY(mContentHeight - rHeight);
                    lines.add(lineData);

                    int wordCount = lineData.getLetters().size();
                    //设置行间距
                    if (isChapterName) {
                        rHeight -= mChapterNameSpacing;
                    } else {
                        rHeight -= mLineSpacing;
                    }
                    //裁剪
                    paragraph = paragraph.substring(wordCount);
                }
                //段落分行完成
                if (!isChapterName && lines.size() != 0) {
                    rHeight = rHeight - mParagraphSpacing;
                }
                if (isChapterName) {
                    isChapterName = false;
                    rHeight = rHeight - mChapterNameMargin;
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
            page.setLines(new ArrayList<LineData>(1));
            pages.add(page);
        }
        for (int i = 0; i < pages.size(); i++) {
//            String progress = "本章进度" + (i + 1) * 100 / pages.size() + " %";
            String progress = (i + 1) + "/" + pages.size();
            pages.get(i).setProgress(progress);
        }
        return pages;
    }

    /**
     * 按段落截取行
     *
     * @param isChapterName
     * @param paragraph
     * @return
     */
    private LineData subLine(boolean isChapterName, String paragraph) {
        List<LineData.LetterData> letterList = new ArrayList<>();
        float lineWidth = 0;
        //字体宽度
        float letterWidth;
        for (int i = 0; i < paragraph.length(); i++) {
            char letter = paragraph.charAt(i);
            if (isChapterName) {
                letterWidth = mChapterNamePaint.measureText(String.valueOf(letter));
            } else {
                letterWidth = mContentPaint.measureText(String.valueOf(letter));
            }
            //如果还能放得下
            if (mContentWidth - lineWidth > letterWidth) {
                LineData.LetterData data = new LineData.LetterData();
                data.setLetter(letter);
                data.setOffsetX(lineWidth);
                letterList.add(data);
                lineWidth += (letterWidth + mLetterSpacing);
            }
            //当前字符放不下了
            else {
                //判断上一个字符能否当末尾 是：处理当前字符、否：删掉
                char lastLetter = paragraph.charAt(i - 1);
                float lastLetterWidth;
                if (ArrayUtils.contains(NO_LINE_TAIL, lastLetter)) {
                    //之前加上了宽度 要减去
                    if (isChapterName) {
                        lastLetterWidth = mChapterNamePaint.measureText(String.valueOf(lastLetter));
                    } else {
                        lastLetterWidth = mContentPaint.measureText(String.valueOf(lastLetter));
                    }
                    lineWidth -= (lastLetterWidth + mLetterSpacing);
                    letterList.remove(letterList.size() - 1);
                }
                //判断当前字符能否当下一行开头 否：  是：
                //TODO  getTextBounds 测量宽度比meassureText略微小点。取舍问题？这里选择的是meassureText
                else if (ArrayUtils.contains(NO_LINE_HEADER, letter)) {
                    LineData.LetterData data = new LineData.LetterData();
                    data.setLetter(letter);
                    data.setOffsetX(lineWidth);
                    letterList.add(data);
                    lineWidth += letterWidth;
                }
                //正常情况 ：减去最后一个字符多余的字间距
                else {
                    lineWidth -= mLetterSpacing;
                }
                //重新对行内文字布局
                layoutLetters(isChapterName, letterList, lineWidth);
                break;
            }
        }

        LineData lineData = new LineData();
        if (isChapterName) lineData.setChapterName(true);
        lineData.setLetters(letterList);
        return lineData;
    }

    /**
     * 重新测量每个字在屏幕中的位置
     * Notice:不处理标题中出现的这种情况
     * <p>
     * 针对两种情况: i： 末尾新增标点 标点要占据位置 让前边的字符间距适度变小
     * ii：末尾有多余空间
     */
    private void layoutLetters(boolean isChapterName, List<LineData.LetterData> letters, float lineWidth) {
        if (isChapterName) return;
        if (lineWidth == mContentWidth) return;
        float offset = mContentWidth - lineWidth;
        int letterSize = letters.size();
        //每个字间距左移或右移
        float averageSpacing = offset / (letterSize - 1);
        //从第二个开始
        for (int i = 1; i < letterSize; i++) {
            LineData.LetterData letter = letters.get(i);
            float newOffsetX = letter.getOffsetX() + averageSpacing * i;
            letter.setOffsetX(newOffsetX);
        }
    }
}
