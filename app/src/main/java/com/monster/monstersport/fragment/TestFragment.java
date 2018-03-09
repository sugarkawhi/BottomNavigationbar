package com.monster.monstersport.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.monster.monstersport.R;
import com.monster.monstersport.base.BaseLazyFragment;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * test
 * Created by ZhaoZongyao on 2017/10/16.
 */

public class TestFragment extends BaseLazyFragment {
    private String TAG = "TestFragment";

    private static final String CONTENT = "　　“你们别顾着拍照啊！快救救她，你们有车的快救救她啊！” 　　十字路口边，车子堵成了长龙，在最前方，许多人拿着手机在围观拍照。 　　空气朦胧，天空阴森着脸，给人一种神伤。 　　一个上衣桃领衫，下身紧身牛仔裤的女生在撕心裂肺地冲他们喊着，可似乎没人愿意理她。 　　她的手上抱着一位头破血流的老人，那满头的白发被血染红，脸色苍白，毫无血色。 　　地上到处都是血，从老人身体流出来的血。";

    private static final char[] TAIL_CHAR = {'，', '。', ';', '”', '！', '？'};


    public static TestFragment newInstance() {
        return new TestFragment();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test;
    }

    @Override
    protected void init(View view) {

    }


    @Override
    protected void loadData() {
    }

    @OnClick(R.id.text)
    public void calculate() {
        List<String> list = new ArrayList<>();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < CONTENT.length(); i++) {
            char c = CONTENT.charAt(i);
            stringBuffer.append(c);
            if (ArrayUtils.contains(TAIL_CHAR, c)) {
                list.add(stringBuffer.toString());
                stringBuffer.setLength(0);
                continue;
            }
        }
        for (String ss : list) {
            Log.e(TAG, "ss > " + ss);
        }

    }

}
