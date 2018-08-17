package com.monster.monstersport.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.monster.monstersport.R;

import java.util.ArrayList;
import java.util.List;

import me.sugarkawhi.bottomnavigationbar.BottomNavigationBar;
import me.sugarkawhi.bottomnavigationbar.BottomNavigationEntity;

/**
 * @author zhzy
 * @Description Created by ZhaoZongyao on 2018/8/17.
 */
public class BnbDemoActivity extends AppCompatActivity {

    public static final int T_IMG_TXT = 1;
    public static final int T_ONLY_IMG = 2;
    public static final int T_ANIM = 3;
    public static final int T_MSG = 4;

    public static void t(Context context, int type) {
        Intent intent = new Intent(context, BnbDemoActivity.class);
        intent.putExtra("t", type);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bnb);
        BottomNavigationBar bottomNavigationBar = findViewById(R.id.bottomNavigationBar);

        Intent intent = getIntent();
        int t = intent.getIntExtra("t", 0);
        List<BottomNavigationEntity> mEntities = new ArrayList<>();
        BottomNavigationEntity entity1 = new BottomNavigationEntity(
                R.drawable.ic_tab_album_default,
                R.drawable.ic_tab_album_selected);
        BottomNavigationEntity entity2 = new BottomNavigationEntity(
                R.drawable.ic_tab_img_default,
                R.drawable.ic_tab_img_selected);
        BottomNavigationEntity entity3 = new BottomNavigationEntity(
                R.drawable.ic_tab_news_default,
                R.drawable.ic_tab_news_selected);
        BottomNavigationEntity entity4 = new BottomNavigationEntity(
                R.drawable.ic_tab_avatar_default,
                R.drawable.ic_tab_avatar_selected);
        mEntities.add(entity1);
        mEntities.add(entity2);
        mEntities.add(entity3);
        mEntities.add(entity4);
        switch (t) {
            case T_IMG_TXT:
                setTitle("图片文字");
                entity1.setText("图片");
                entity2.setText("视频");
                entity3.setText("关注");
                entity4.setText("我的");
                break;
            case T_ONLY_IMG:
                //不设置文字就不会显示
                setTitle("仅图片");
                break;
            case T_ANIM:
                setTitle("支持切换动画");
                //开始切换动画 默认不开启
                bottomNavigationBar.setAnim(true);
                entity1.setText("支支");
                entity2.setText("持持");
                entity3.setText("动动");
                entity4.setText("画画");
                break;
            case T_MSG:
                setTitle("支持消息");
                entity1.setText("支支");
                entity2.setText("持持");
                entity3.setText("消消");
                entity4.setText("息息");
                entity3.setBadgeNum(9);
                entity4.setBadgeNum(100);
                break;
        }


        bottomNavigationBar.setEntities(mEntities);
        bottomNavigationBar.setBnbItemSelectListener(new BottomNavigationBar.IBnbItemSelectListener() {

            @Override
            public void onBnbItemSelect(int position) {
            }
        });


        final RecyclerView recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = getLayoutInflater().inflate(R.layout.list_item_test, parent, false);
                return new RecyclerView.ViewHolder(view) {
                };
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                ((TextView) holder.itemView.findViewById(R.id.tv_content)).setText("position=" + position);
            }

            @Override
            public int getItemCount() {
                return 20;
            }
        });


        bottomNavigationBar.setBnbItemDoubleClickListener(new BottomNavigationBar.IBnbItemDoubleClickListener() {
            @Override
            public void onBnbItemDoubleClick(int position) {
                recyclerView.smoothScrollToPosition(0);
            }
        });

        bottomNavigationBar.setCurrentPosition(0);


        recyclerView.setVisibility(View.GONE);

    }


}
