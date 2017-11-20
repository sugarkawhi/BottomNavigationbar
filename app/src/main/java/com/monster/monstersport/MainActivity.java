package com.monster.monstersport;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;


import com.monster.monstersport.fragment.JikeViewFragment;
import com.monster.monstersport.fragment.MonsterViewFragment;
import com.monster.monstersport.fragment.TestFragment;

import java.util.ArrayList;
import java.util.List;

import me.sugarkawhi.bottomnavigationbar.BottomNavigationBar;
import me.sugarkawhi.bottomnavigationbar.BottomNavigationEntity;


public class MainActivity extends AppCompatActivity {


    private List<BottomNavigationEntity> entities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final BottomNavigationBar bottomNavigationBar = findViewById(R.id.bottomNavigationBar);
//        entities.add(new BottomNavigationEntity("",
//                R.drawable.hy_mian_icon_sc,
//                R.drawable.hy_mian_icon_sc_down));
//        entities.add(new BottomNavigationEntity("",
//                R.drawable.hy_mian_icon_sj,
//                R.drawable.hy_mian_icon_sj_down));
//        entities.add(new BottomNavigationEntity("",
//                R.drawable.hy_mian_icon_tj,
//                R.drawable.hy_mian_icon_tj_down));
//        entities.add(new BottomNavigationEntity("",
//                R.drawable.hy_mian_icon_wd,
//                R.drawable.hy_mian_icon_wd_down));
        entities.add(new BottomNavigationEntity(
                R.drawable.hy_mian_icon_sc,
                R.drawable.hy_mian_icon_sc_down));
        entities.add(new BottomNavigationEntity(
                R.drawable.hy_mian_icon_sj,
                R.drawable.hy_mian_icon_sj_down));
        entities.add(new BottomNavigationEntity(
                R.drawable.hy_mian_icon_tj,
                R.drawable.hy_mian_icon_tj_down));
        entities.add(new BottomNavigationEntity(
                R.drawable.hy_mian_icon_wd,
                R.drawable.hy_mian_icon_wd_down));
        bottomNavigationBar.setEntities(entities);
        bottomNavigationBar.setBnbItemSelectListener(new BottomNavigationBar.IBnbItemSelectListener() {

            @Override
            public void onBnbItemSelect(int position) {
                switch (position) {
                    case 0:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, new TestFragment())
                                .commit();
                        break;
                    case 1:
//                        getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.container, new JikeViewFragment())
//                                .commit();
                        break;
                }
            }
        });

        bottomNavigationBar.setBnbItemDoubleClickListener(new BottomNavigationBar.IBnbItemDoubleClickListener() {
            @Override
            public void onBnbItemDoubleClick(int position) {
                Toast.makeText(MainActivity.this, "onBnbItemDoubleClick " + position, Toast.LENGTH_SHORT).show();
            }
        });

        bottomNavigationBar.setCurrentPosition(1);
    }

}
