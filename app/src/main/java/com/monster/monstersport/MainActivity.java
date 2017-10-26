package com.monster.monstersport;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;


import com.monster.monstersport.fragment.JikeViewFragment;
import com.monster.monstersport.fragment.MonsterViewFragment;

import java.util.ArrayList;
import java.util.List;

import me.sugarkawhi.bottomnavigationbar.BottomNavigationBar;


public class MainActivity extends AppCompatActivity {


    private List<BottomNavigationBar.BottomNavigationEntity> entities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottomNavigationBar);
        entities.add(new BottomNavigationBar.BottomNavigationEntity("新闻",
                R.drawable.ic_tab_news_default,
                R.drawable.ic_tab_news_selected));
        entities.add(new BottomNavigationBar.BottomNavigationEntity("发现",
                R.drawable.ic_tab_img_default,
                R.drawable.ic_tab_img_selected));
        entities.add(new BottomNavigationBar.BottomNavigationEntity("关注",
                R.drawable.ic_tab_album_default,
                R.drawable.ic_tab_album_selected));
        entities.add(new BottomNavigationBar.BottomNavigationEntity("我的",
                R.drawable.ic_tab_avatar_default,
                R.drawable.ic_tab_avatar_selected));
        bottomNavigationBar.setEntities(entities);
        bottomNavigationBar.setOnBottomNavigationBarItemClickListener(new BottomNavigationBar.OnBottomNavigationBarItemClickListener() {
            @Override
            public void onBottomNavigationBarItemClick(int position) {
                bottomNavigationBar.setCurrentPosition(position);
                switch (position) {
                    case 0:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, new MonsterViewFragment())
                                .commit();
                        break;
                    case 1:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, new JikeViewFragment())
                                .commit();
                        break;
                }

            }
        });

        bottomNavigationBar.setOnBottomNavigationBarItemDoubleClickListener(new BottomNavigationBar.OnBottomNavigationBarItemDoubleClickListener() {
            @Override
            public void onBottomNavigationBarItemDoubleClick(int position) {
            }
        });
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new MonsterViewFragment())
                .commit();
    }

}
