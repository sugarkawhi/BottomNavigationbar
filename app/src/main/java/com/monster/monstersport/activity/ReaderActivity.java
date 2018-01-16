package com.monster.monstersport.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.monster.monstersport.R;

import me.sugarkawhi.mreader.bean.ChapterBean;
import me.sugarkawhi.mreader.config.Config;
import me.sugarkawhi.mreader.view.BaseReaderView;

/**
 * Created by ZhaoZongyao on 2018/1/12.
 */

public class ReaderActivity extends AppCompatActivity {

    BaseReaderView readView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_reader);
        readView = findViewById(R.id.readerView);
        ChapterBean chapter = new ChapterBean();
        chapter.setChapterName("第一章 刘关张桃园三结义");
        chapter.setChapterContent(Config.TEST_CONTENT);
        readView.setChapter(chapter);
        readView.setElectric(0.6f);
        readView.setTime("18:00");

        hideSystemUI();
    }


    /**
     * 隐藏菜单。沉浸式阅读
     */
    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    private void showSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }
}
