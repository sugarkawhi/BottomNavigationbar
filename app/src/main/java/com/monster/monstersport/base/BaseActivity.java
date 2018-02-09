package com.monster.monstersport.base;

import android.widget.Toast;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * Created by ZhaoZongyao on 2018/1/30.
 */

public class BaseActivity extends RxAppCompatActivity {

    Toast mToast;

    public void showToast(String toast) {
        if (mToast == null)
            mToast =  Toast.makeText(this, "", Toast.LENGTH_SHORT);
        mToast.setText(toast);
        mToast.show();
    }

}
