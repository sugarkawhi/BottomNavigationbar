package com.monster.monstersport.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;


import com.monster.monstersport.R;

import java.util.ArrayList;
import java.util.List;

import me.sugarkawhi.bottomnavigationbar.BottomNavigationBar;
import me.sugarkawhi.bottomnavigationbar.BottomNavigationEntity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void imageAndText(View view) {
        BnbDemoActivity.t(this, BnbDemoActivity.T_IMG_TXT);
    }


    public void onlyImage(View view) {
        BnbDemoActivity.t(this, BnbDemoActivity.T_ONLY_IMG);
    }

    public void supportAnim(View view) {
        BnbDemoActivity.t(this, BnbDemoActivity.T_ANIM);
    }



    public void supportMsg(View view) {
        BnbDemoActivity.t(this, BnbDemoActivity.T_MSG);
    }
}
