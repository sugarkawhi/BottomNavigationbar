package com.monster.monstersport.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by ZhaoZongyao on 2018/1/9.
 */

@SuppressLint("AppCompatCustomView")
public class FilterImageView extends ImageView {
    public FilterImageView(Context context) {
        super(context);
    }

    public FilterImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FilterImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
