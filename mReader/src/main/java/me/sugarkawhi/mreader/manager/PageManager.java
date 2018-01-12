package me.sugarkawhi.mreader.manager;

import android.graphics.Paint;
import android.util.Log;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.sugarkawhi.mreader.bean.ChapterBean;
import me.sugarkawhi.mreader.config.Config;
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

    /**
     * line max letters
     *
     * @param contentWidth
     * @param fontSize
     * @return
     */
    public static int calcLineLetters(float contentWidth, float fontSize) {
        return (int) (contentWidth / fontSize);
    }

    public static List<String> generateLines(ChapterBean chapter, int lineLetters) {
        List<String> lines = new ArrayList<>();
        if (chapter == null || chapter.getChapterContent() == null)
            return lines;
        String content = chapter.getChapterContent();
        float contentLength = content.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < contentLength; i++) {
            char letter = content.charAt(i);
            sb.append(letter);
            if (letter == PARAGRAPH) {
                lines.add(sb.toString());
                sb.setLength(0);
                continue;
            }
            if (sb.length() == lineLetters) {
                //judge
                if (i + 1 < contentLength) {
                    char nextFirstLetter = content.charAt(i + 1);
                    if (ArrayUtils.contains(NO_LINE_HEADER, nextFirstLetter)) {
                        sb.append(nextFirstLetter);
                        i++;
                    }
                    if (i + 1 < contentLength) {
                        char nextSecondLetter = content.charAt(i + 1);
                        if (nextSecondLetter == PARAGRAPH) {
                            sb.append(nextSecondLetter);
                            i++;
                        }
                    }
                }
                lines.add(sb.toString());
                sb.setLength(0);
            }
        }
        for (String line : lines) {
            System.out.println(line);
        }
        return lines;
    }

    public static List<PageData> generatePages(ChapterBean chapter, float contentWidth, float contentHeight, float fontHeight, float lineSpacing, float paragraphSpacing) {
        List<String> lines = generateLines(chapter, 20);
        List<PageData> pages = new ArrayList<>();
        PageData page = PageData.newInstance();
        page.setChapterName(chapter.getChapterName());
        pages.add(page);
        page.setProgress("0%");
        float curPageHeight = 0;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            //段末尾
            if (line.contains(String.valueOf(PARAGRAPH))) {
                curPageHeight += (fontHeight + paragraphSpacing);
            } else {
                curPageHeight += (fontHeight + lineSpacing);
            }
            //需要重开一页
            if (curPageHeight >= contentHeight) {
                curPageHeight = 0;
                page = PageData.newInstance();
                page.setChapterName(chapter.getChapterName());
                page.setProgress(i * 100f / (lines.size() - 1) + "%");
                pages.add(page);
            }
            page.getLineData().getLines().add(line);
        }
        return pages;
    }

}
