package com.monster.monstersport.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.monster.monstersport.R;
import com.monster.monstersport.view.JiKeView;

/**
 * Created by ZhaoZongyao on 2017/10/16.
 */

public class JikeViewFragment extends Fragment {


    public static JikeViewFragment newInstance() {
        return new JikeViewFragment();
    }

    SeekBar seekBar;
    JiKeView jiKeView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_jike, container, false);
        seekBar = v.findViewById(R.id.seekBar);
        jiKeView = v.findViewById(R.id.jikeView);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float progress = seekBar.getProgress() / 100f;
                Log.e("JikeViewFragment", "onProgressChanged: " + progress);
                jiKeView.setPercent(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        v.findViewById(R.id.RESET).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jiKeView.reset();
                seekBar.setProgress(0);
            }
        });
        v.findViewById(R.id.COMPLETE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jiKeView.complete();
            }
        });
        return v;
    }


}
