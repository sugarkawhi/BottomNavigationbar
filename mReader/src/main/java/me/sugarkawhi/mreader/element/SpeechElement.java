package me.sugarkawhi.mreader.element;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;

import java.util.List;

import me.sugarkawhi.mreader.data.LineData;

/**
 * 语音合成模式
 * Created by ZhaoZongyao on 2018/1/11.
 */

public class SpeechElement extends LineElement {

    private float mHeaderHeight;
    private float mFooterHeight;
    private float mPadding;
    private float mContentWidth;
    private float mContentHeight;
    private Paint mContentPaint;
    private Paint mChapterNamePaint;

    private List<LineData> mLineDataList;

    public SpeechElement(float contentWidth, float contentHeight, float headerHeight, float footerHeight, float padding, Paint contentPaint, Paint chapterNamePaint) {
        super(contentWidth, contentHeight, headerHeight, footerHeight, padding, contentPaint, chapterNamePaint);
    }

    public void setLineData(List<LineData> lineDataList) {
        this.mLineDataList = lineDataList;
    }

    @Override
    public boolean onDraw(Canvas canvas) {
        if (mLineDataList == null) return false;
        for (int i = 0; i < mLineDataList.size(); i++) {
            LineData lineData = mLineDataList.get(i);
            if (lineData == null) continue;
            //画一整行
            if (!TextUtils.isEmpty(lineData.getLine())) {
                canvas.drawText(lineData.getLine(), lineData.getOffsetX() + mPadding, lineData.getOffsetY() + mHeaderHeight, mContentPaint);
                continue;
            }

            //画单个字符
            List<LineData.LetterData> letterDataList = lineData.getLetters();
            if (letterDataList != null) {
                for (LineData.LetterData letter : letterDataList) {
                    String str = String.valueOf(letter.getLetter());
                    float x = letter.getOffsetX() + mPadding;
                    float y = lineData.getOffsetY() + mHeaderHeight;
                    if (lineData.isChapterName()) {
                        canvas.drawText(str, x, y, mChapterNamePaint);
                    } else {
                        canvas.drawText(str, x, y, mContentPaint);
                    }
                }
            }
        }
        return true;
    }
}
