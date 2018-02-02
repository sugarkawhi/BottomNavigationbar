package com.monster.monstersport.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;

import com.monster.monstersport.R;

/**
 * 设置字体
 * Created by ZhaoZongyao on 2018/1/30.
 */

public class SpacingSettingDialog extends BottomSheetDialog {
    public SpacingSettingDialog(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_reader_spacing;
    }
}
