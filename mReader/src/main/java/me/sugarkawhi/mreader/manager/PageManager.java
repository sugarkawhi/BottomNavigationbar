package me.sugarkawhi.mreader.manager;

import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import org.apache.commons.lang3.ArrayUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.sugarkawhi.mreader.bean.ChapterBean;
import me.sugarkawhi.mreader.data.LineData;
import me.sugarkawhi.mreader.data.PageData;
import me.sugarkawhi.mreader.utils.Utils;

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
                int wordCount = 0;
                String subStr = null;
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
                    if (isChapterName) {
                        wordCount = mChapterNamePaint.breakText(paragraph, true, mContentWidth, null);
                    } else {
                        wordCount = mContentPaint.breakText(paragraph, true, mContentWidth, null);
                    }

                    //最后
//                    if (ArrayUtils.contains(NO_LINE_TAIL, paragraph.charAt(wordCount - 1))) {
//                        wordCount--;
//                    } else {
//                        if (wordCount < paragraph.length()) {
//                            char c = paragraph.charAt(wordCount);
//                            if (ArrayUtils.contains(NO_LINE_HEADER, c)) {
//                                wordCount++;//往前挪一位
//                            }
//                        }
//                    }

//                    subStr = paragraph.substring(0, wordCount);
//                    lineData = new LineData();
//                    lineData.setLine(subStr);
//                    if (isChapterName) {
//                        lineData.setChapterName(true);
//                    }
                    LineData lineData = subLine(isChapterName, paragraph);
                    lineData.setOffsetY(mContentHeight - rHeight);
                    lines.add(lineData);
                    wordCount = lineData.getLetters().size();
                    //测量每个字在屏幕中的位置
//                    List<LineData.LetterData> letters = measureLetters(isChapterName, subStr);
//                    lineData.setLetters(letters);

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
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < paragraph.length(); i++) {
            char letter = paragraph.charAt(i);
            if (isChapterName) {
                letterWidth = mChapterNamePaint.measureText(String.valueOf(letter));
            } else {
                letterWidth = mContentPaint.measureText(String.valueOf(letter));
            }
            //如果还能放得下
            if (mContentWidth - lineWidth > letterWidth) {
                sb.append(letter);
                LineData.LetterData data = new LineData.LetterData();
                data.setLetter(letter);
                data.setOffsetX(lineWidth);
                letterList.add(data);
                lineWidth += (letterWidth + mLetterSpacing);
            }//当前字符放不下了
            else {
                //判断上一个字符能否当末尾 是：处理当前字符、否：删掉
                char lastLetter = paragraph.charAt(i - 1);
                if (ArrayUtils.contains(NO_LINE_TAIL, lastLetter)) {
                    sb.deleteCharAt(i - 1);
                    //之前加上了宽度 要减去
                    if (isChapterName) {
                        letterWidth = mChapterNamePaint.measureText(String.valueOf(lastLetter));
                    } else {
                        letterWidth = mContentPaint.measureText(String.valueOf(lastLetter));
                    }
                    lineWidth -= (letterWidth + mLetterSpacing);
                    letterList.remove(letterList.size() - 1);
                }
                //判断当前字符能否当下一行开头 否：加到本行  是：
                else {
                    if (ArrayUtils.contains(NO_LINE_HEADER, letter)) {
                        sb.append(letter);
                        LineData.LetterData data = new LineData.LetterData();
                        data.setLetter(letter);
                        data.setOffsetX(lineWidth);
                        letterList.add(data);
                        lineWidth += letterWidth;
                    } else {
                        lineWidth -= mLetterSpacing;
                    }
                }
                break;
            }
        }

        String line = sb.toString();
        LineData lineData = new LineData();
        if (isChapterName) lineData.setChapterName(true);
        lineData.setLine(line);
        lineData.setLetters(letterList);
        return lineData;
    }

    /**
     * 测量每个文字在屏幕中的位置
     * 空格符 也占一个位置 但是在breakText中它所占的位置不是均分的 所以如果在最后
     *
     * @param isChapaterName 是否是标题
     * @param line           行数据
     * @return
     */
    private List<LineData.LetterData> measureLetters(boolean isChapaterName, String line) {
        List<LineData.LetterData> letters = new ArrayList<>();
        float textLength;
        if (isChapaterName) {
            textLength = mChapterNamePaint.measureText(line);
        } else {
            textLength = mContentPaint.measureText(line);
        }
        //单个文字平均宽度
        float singleWidth;
        if (textLength > mContentWidth || mContentWidth - textLength < (textLength / line.length())) {
            singleWidth = mContentWidth / line.length();
        } else {
            singleWidth = textLength / line.length();
        }
        float offsetX = 0;
        for (int j = 0; j < line.length(); j++) {
            LineData.LetterData letter = new LineData.LetterData();
            letter.setLetter(line.charAt(j));
            letter.setOffsetX(offsetX);
            offsetX += singleWidth;
            letters.add(letter);
        }
        return letters;
    }

}
