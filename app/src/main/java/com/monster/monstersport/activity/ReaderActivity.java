package com.monster.monstersport.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.monster.monstersport.R;

import me.sugarkawhi.mreader.bean.ChapterBean;
import me.sugarkawhi.mreader.config.Config;
import me.sugarkawhi.mreader.listener.IReaderTouchListener;
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
        chapter.setBookName("《三国演义》");
        chapter.setChapterName("第一章 刘关张桃园三结义 鲁智深拳打镇关西");
        chapter.setChapterContent(TEST_CONTENT);
        readView.setChapter(chapter);
        readView.setElectric(0.6f);
        readView.setTime("18:00");
        readView.setReaderTouchListener(new IReaderTouchListener() {
            @Override
            public void onTouchCenter() {
                Toast.makeText(ReaderActivity.this, "onTouchCenter", Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onTouchRight() {
                return false;
            }

            @Override
            public boolean onTouchLeft() {
                return false;
            }
        });
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

    String TEST_CONTENT =
            "　　赵 家屯，位于凤 凰山下， 依  山傍水，景色宜人。但因为交通不便，这个屯子很穷。穷到连 吃肉都成为了很奢侈的事情，穷 到连 电灯都 不舍得用。\n" +
                    "　　当然，赵家屯这个名字已 经成为过去式了，十里八乡赫赫有名的女人村。\n" +
                    "　　当然，赵家屯 这 个名字已经成为过去式了，如今成为了十里八乡赫赫有名的女人村。\n" +
                    "　　这件事说起来还要追溯到一年以前，村里首富赵大山在城里工地上接了一个安装水电暖的工程。这个工程很大，如果按时完工能赚个几十万。\n" +
                    "　　本打算让全村老爷们跟着多赚几个，谁知道那个小区是豆腐渣工程，就在全村老爷们如火如荼的安装着水电暖的时候，那座小区直接坍塌了。\n" +
                    "　　而赵家屯所有人都被掩埋到了废墟下，当被人们拔出来后，二百多人都已经不幸遇难了。\n" +
                    "　　虽然和赵大山没有直接关系，但这件事却是因他而起。而他瞬间成为赵家屯中万夫所指的罪人。\n" +
                    "　　因为良心有愧，赵大山服下敌敌畏自尽了。\n" +
                    "　　赵大山虽然解脱了，但是却留下年仅十六岁的赵小宁独自一人生活在赵家屯，生活在人们的指指点点中。\n" +
                    "　　赵大山虽然解脱了，但是却留下年仅十六岁的赵小宁独自一人生活在赵家屯，生活在人们的指指点点中。\n" +
                    "　　赵大山虽然解脱了，但是却留下年仅十六岁的赵小宁独自一人生活在赵家屯，生活在人们的指指点点中。\n" +
                    "　　一座长满杂草的坟圈子前，赵小宁眼睛痛苦的坐在那里，虽然才十六岁，但五官清秀。尤其是那满是沧桑的双眸更是同龄人所无法拥有的。洗的发黄的白色体恤，搭配一条大裤衩，千层底，地地道道的农家小子的装扮。\n" +
                    "　　当然，赵家屯这个名字已经成为过去式了，如今成为了十里八乡赫赫有名的女人村。\n" +
                    "　　一座长满杂草的坟圈子前，赵小宁眼睛痛苦的坐在那里，虽然才十六岁，但五官清秀。尤其是那满是沧桑的双眸更是同龄人所无法拥有的。洗的发黄的白色体恤，搭配一条大裤衩，千层底，地地道道的农家小子的装扮。\n" +
                    "　　当然，赵家屯这个名字已经成为过去式了，如今成为了十里八乡赫赫有名的女人村。\n" +
                    "　　一座长满杂草的坟圈子前，赵小宁眼睛痛苦的坐在那里，虽然才十六岁，但五官清秀。尤其是那满是沧桑的双眸更是同龄人所无法拥有的。洗的发黄的白色体恤，搭配一条大裤衩，千层底，地地道道的农家小子的装扮。\n" +
                    "　　当然，赵家屯这个名字已经成为过去式了，如今成为了十里八乡赫赫有名的女人村。\n" +
                    "　　一座长满杂草的坟圈子前，赵小宁眼睛痛苦的坐在那里，虽然才十六岁，但五官清秀。尤其是那满是沧桑的双眸更是同龄人所无法拥有的。洗的发黄的白色体恤，搭配一条大裤衩，千层底，地地道道的农家小子的装扮。\n" +
                    "　　一座长满杂草的坟圈子前，赵小宁眼睛痛苦的坐在那里，虽然才十六岁，但五官清秀。尤其是那满是沧桑的双眸更是同龄人所无法拥有的。洗的发黄的白色体恤，搭配一条大裤衩，千层底，地地道道的农家小子的装扮。\n" +
                    "　　一座长满杂草的坟圈子前，赵小宁眼睛痛苦的坐在那里，虽然才十六岁，但五官清秀。尤其是那满是沧桑的双眸更是同龄人所无法拥有的。洗的发黄的白色体恤，搭配一条大裤衩，千层底，地地道道的农家小子的装扮。\n";

}
