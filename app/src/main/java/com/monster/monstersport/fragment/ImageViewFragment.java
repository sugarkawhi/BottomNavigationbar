package com.monster.monstersport.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Outline;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.monster.monstersport.R;
import com.monster.monstersport.helper.ShadowProperty;
import com.monster.monstersport.helper.ShadowViewDrawable;
import com.monster.monstersport.view.JiKeView;
import com.monster.monstersport.view.LoadingTextView;

/**
 * Created by ZhaoZongyao on 2017/10/16.
 */

public class ImageViewFragment extends Fragment {


    SeekBar seekBar;
    JiKeView jiKeView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image, container, false);
        seekBar = v.findViewById(R.id.seekBar);
        jiKeView = v.findViewById(R.id.jikeView);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float progress = seekBar.getProgress() / 100f;
                Log.e("ImageViewFragment", "onProgressChanged: " + progress);
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
