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
import me.sugarkawhi.pulltomark.PtmLayout;

/**
 * test
 * Created by ZhaoZongyao on 2017/10/16.
 */

public class TestFragment extends BaseLazyFragment {
    private String TAG = "TestFragment";


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


}
