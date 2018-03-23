package com.monster.monstersport.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.monster.monstersport.R;
import com.monster.monstersport.base.BaseLazyFragment;

import java.util.Random;

import butterknife.BindView;

/**
 * ImageFragment
 * Created by ZhaoZongyao on 2017/10/16.
 */

public class ImageFragment extends BaseLazyFragment {
    private String TAG = "ImageFragment";

    private int[] resIds = {R.drawable.icon_cm, R.drawable.icon_pg, R.drawable.icon_xg};

    @BindView(R.id.image)
    ImageView mImageView;

    public static ImageFragment newInstance() {
        return new ImageFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_image;
    }

    @Override
    protected void init(View view) {
        int number = new Random().nextInt(3);
        mImageView.setImageResource(resIds[number]);
    }


    @Override
    protected void loadData() {

    }


}
