package com.monster.monstersport.base;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * Created by ZhaoZongyao on 2018/1/30.
 */

public class BaseActivity extends RxAppCompatActivity {

    public void showToast(String toast) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }

}
