package me.sugarkawhi.mreader.element;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;

import java.util.List;

import me.sugarkawhi.mreader.data.LetterData;
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
    private List<LetterData> mLetterDataList;

    public SpeechElement(float contentWidth, float contentHeight, float headerHeight, float footerHeight, float padding, Paint contentPaint, Paint chapterNamePaint) {
        super(contentWidth, contentHeight, headerHeight, footerHeight, padding, contentPaint, chapterNamePaint);
    }

    public void setLineData(List<LineData> lineDataList) {
        this.mLineDataList = lineDataList;
    }

    public void setLetterData(List<LetterData> letterDataList) {
        this.mLetterDataList = letterDataList;
    }

    @Override
    public boolean onDraw(Canvas canvas) {
//        if (mLineDataList == null) return false;
//        for (int i = 0; i < mLineDataList.size(); i++) {
//            LineData lineData = mLineDataList.get(i);
//            if (lineData == null) continue;
//            //画单个字符
//            List<LetterData> letterDataList = lineData.getLetters();
//            if (letterDataList != null) {
//                for (LetterData letter : letterDataList) {
//                    String str = String.valueOf(letter.getLetter());
//                    float x = letter.getOffsetX() + mPadding;
//                    float y = lineData.getOffsetY() + mHeaderHeight;
//                    if (lineData.isChapterName()) {
//                        canvas.drawText(str, x, y, mChapterNamePaint);
//                    } else {
//                        canvas.drawText(str, x, y, mContentPaint);
//                    }
//                }
//            }
//        }
        drawTts(canvas);
        if (mLetterDataList == null) return false;
        for (LetterData letter : mLetterDataList) {
            String str = String.valueOf(letter.getLetter());
            float x = letter.getOffsetX() + mPadding;
            float y = letter.getOffsetY() + mHeaderHeight;
            if (letter.isChapterName()) {
                canvas.drawText(str, x, y, mChapterNamePaint);
            } else {
                canvas.drawText(str, x, y, mContentPaint);
            }
        }

        return true;
    }
}
