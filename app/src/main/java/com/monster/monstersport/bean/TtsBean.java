package com.monster.monstersport.bean;

import java.util.List;

import me.sugarkawhi.mreader.data.LetterData;

/**
 * 语音合成Bean
 * Created by ZhaoZongyao on 2018/3/7.
 */

public class TtsBean {
    private int utteranceId;
    private List<LetterData> mLetterList;
    private String content;

    public int getUtteranceId() {
        return utteranceId;
    }

    public void setUtteranceId(int utteranceId) {
        this.utteranceId = utteranceId;
    }

    public List<LetterData> getLetterList() {
        return mLetterList;
    }

    public void setLetterList(List<LetterData> letterList) {
        mLetterList = letterList;
        if (letterList == null) return;
        StringBuilder sb = new StringBuilder();
        for (LetterData letter : letterList) {
            char c = letter.getLetter();
            sb.append(c);
        }
        this.content = sb.toString();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
