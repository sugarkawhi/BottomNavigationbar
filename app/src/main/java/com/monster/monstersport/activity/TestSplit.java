package com.monster.monstersport.activity;

import java.util.Arrays;

/**
 * Created by ZhaoZongyao on 2018/3/7.
 */

public class TestSplit {
    static final String STRING = "　　“你们别顾着拍照啊！快救救她，你们有车的快救救她啊！” 　　十字路口边，车子堵成了长龙，在最前方，许多人拿着手机在围观拍照。 　　空气朦胧，天空阴森着脸，给人一种神伤。 　　一个上衣桃领衫，下身紧身牛仔裤的女生在撕心裂肺地冲他们喊着，可似乎没人愿意理她。 　　她的手上抱着一位头破血流的老人，那满头的白发被血染红，脸色苍白，毫无血色。 　　地上到处都是血，从老人身体流出来的血。 　　\n";
    static final String REGEX = "。|。\"|；|？|？\"|！”";

    public static void main(String[] args) {
        String[] arr = STRING.split(REGEX);
        for (String s : arr) {
            System.out.println("----- " + s);
        }
    }
}
