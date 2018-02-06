package com.monster.monstersport.bean;


import java.io.Serializable;
import java.util.List;

/**
 * 章节列表
 * ps: 接口返回.
 *
 * @author zhzy
 * @date 2017/11/6
 */
public class ChapterListBean implements Serializable {
    private List<ChapterBean> datas;

    public List<ChapterBean> getDatas() {
        return datas;
    }

    public void setDatas(List<ChapterBean> datas) {
        this.datas = datas;
    }
}
