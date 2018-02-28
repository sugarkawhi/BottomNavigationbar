package com.monster.monstersport.dialog;

import android.content.Context;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.monster.monstersport.R;
import com.monster.monstersport.persistence.HyReaderPersistence;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import me.sugarkawhi.mreader.config.IReaderConfig;
import me.sugarkawhi.mreader.persistence.IReaderPersistence;

import static com.monster.monstersport.persistence.HyReaderPersistence.Background.COLOR_MATCHA;
import static com.monster.monstersport.persistence.HyReaderPersistence.Background.DEFAULT;
import static com.monster.monstersport.persistence.HyReaderPersistence.Background.IMAGE_BLUE;
import static com.monster.monstersport.persistence.HyReaderPersistence.Background.IMAGE_PURPLE;
import static com.monster.monstersport.persistence.HyReaderPersistence.Background.NIGHT;
import static me.sugarkawhi.mreader.config.IReaderConfig.PageMode.COVER;
import static me.sugarkawhi.mreader.config.IReaderConfig.PageMode.NONE;
import static me.sugarkawhi.mreader.config.IReaderConfig.PageMode.SIMULATION;
import static me.sugarkawhi.mreader.config.IReaderConfig.PageMode.SLIDE;

/**
 * 语音合成
 * Created by ZhaoZongyao on 2018/2/28.
 */

public class ReaderTtsDialog extends BottomPopDialog {

    public static final String TYPE_LOCAL = "local";
    public static final String TYPE_CLOUD = "cloud";

    private IReaderTtsChangeListener mListener;

    public ReaderTtsDialog(Context context) {
        super(context);
        ButterKnife.bind(this, getContentView());
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_reader_tts;
    }

    public ReaderTtsDialog setListener(IReaderTtsChangeListener listener) {
        mListener = listener;
        return this;
    }

    @OnCheckedChanged({R.id.reader_tts_voicer_cloud_xiaoyan,
            R.id.reader_tts_voicer_cloud_xiaofeng,
            R.id.reader_tts_voicer_cloud_xiaokun,
            R.id.reader_tts_voicer_cloud_xiaoqian})
    public void changeCloudVoicer(CompoundButton compoundButton, boolean isChecked) {
        if (!isChecked) return;
        if (mListener == null) return;
        switch (compoundButton.getId()) {
            case R.id.reader_tts_voicer_cloud_xiaoyan:
                mListener.onTtsVoicerChange(TYPE_CLOUD, "xiaoyan");
                break;
            case R.id.reader_tts_voicer_cloud_xiaofeng:
                mListener.onTtsVoicerChange(TYPE_CLOUD, "vixf");
                break;
            case R.id.reader_tts_voicer_cloud_xiaokun:
                mListener.onTtsVoicerChange(TYPE_CLOUD, "xiaokun");
                break;
            case R.id.reader_tts_voicer_cloud_xiaoqian:
                mListener.onTtsVoicerChange(TYPE_CLOUD, "xiaoqian");
                break;
        }
    }


    public interface IReaderTtsChangeListener {
        void onTtsTypeChange(String ttsType);//Local Cloud

        void onTtsVoicerChange(String ttsType, String voicer);//人物
    }
}
