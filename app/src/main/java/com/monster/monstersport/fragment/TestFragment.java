package com.monster.monstersport.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.monster.monstersport.R;
import com.monster.monstersport.base.BaseLazyFragment;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by ZhaoZongyao on 2017/10/16.
 */

public class TestFragment extends BaseLazyFragment {

    private View mView;

    public static TestFragment newInstance(String title) {
        TestFragment fragment = new TestFragment();
        if (!TextUtils.isEmpty(title)) {
            Bundle bundle = new Bundle();
            bundle.putString("title", title);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    private String TAG = "TestFragment";
    private String mTitle;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test;
    }

    @Override
    protected void init(View view) {
        TextView textView = view.findViewById(R.id.text);
        Button button = view.findViewById(R.id.button);
        mView = view.findViewById(R.id.main_view);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }
        });
    }


    public void show() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                //DB....
                e.onNext("");
            }
        });
    }



    @Override
    protected void loadData() {
        Log.e(TAG, "loadData: title=" + mTitle);
    }


}
