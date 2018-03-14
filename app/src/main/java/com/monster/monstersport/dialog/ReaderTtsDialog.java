package com.monster.monstersport.dialog;

import android.content.Context;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.monster.monstersport.R;
import com.monster.monstersport.persistence.HyReaderPersistence;
import com.monster.monstersport.util.OfflineResource;

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

    @BindView(R.id.reader_seekBar_tts_speed)
    SeekBar mSpeedSeekBar;
    @BindView(R.id.reader_tts_speaker)
    RadioGroup mTtsSpeakerGroup;


    private IReaderTtsChangeListener mListener;

    public ReaderTtsDialog(Context context) {
        super(context);
        ButterKnife.bind(this, getContentView());
        init();
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_reader_tts;
    }

    public ReaderTtsDialog setListener(IReaderTtsChangeListener listener) {
        mListener = listener;
        return this;
    }

    /**
     * 语音合成速度
     *
     * @param speed  // 设置合成的语速，0-9 ，默认 5
     */
    public ReaderTtsDialog setTtsSpeed(int speed) {
        mSpeedSeekBar.setProgress(speed);
        return this;
    }

    /**
     * 设置发音人
     *
     * @param speaker {@link IReaderConfig.Speaker}
     */
    public ReaderTtsDialog setTtsSpeaker(int speaker) {
        switch (speaker) {
            case IReaderConfig.Speaker.FEMALE:
            default:
                mTtsSpeakerGroup.check(R.id.reader_tts_female);
                break;
            case IReaderConfig.Speaker.MALE:
                mTtsSpeakerGroup.check(R.id.reader_tts_male);
                break;
            case IReaderConfig.Speaker.DUXY:
                mTtsSpeakerGroup.check(R.id.reader_tts_duxy);
                break;
            case IReaderConfig.Speaker.DUYY:
                mTtsSpeakerGroup.check(R.id.reader_tts_duyy);
                break;
        }
        return this;
    }

    private void init() {
        mSpeedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mListener != null) mListener.onTtsSpeedChange(seekBar.getProgress());
            }
        });
    }

    @OnCheckedChanged({R.id.reader_tts_male,
            R.id.reader_tts_female,
            R.id.reader_tts_duxy,
            R.id.reader_tts_duyy})
    public void changeCloudVoicer(CompoundButton compoundButton, boolean isChecked) {
        if (!isChecked) return;
        if (mListener == null) return;
        switch (compoundButton.getId()) {
            case R.id.reader_tts_male:
                mListener.onTtsSpeakerChange(IReaderConfig.Speaker.MALE);
                break;
            case R.id.reader_tts_female:
                mListener.onTtsSpeakerChange(IReaderConfig.Speaker.FEMALE);
                break;
            case R.id.reader_tts_duxy:
                mListener.onTtsSpeakerChange(IReaderConfig.Speaker.DUXY);
                break;
            case R.id.reader_tts_duyy:
                mListener.onTtsSpeakerChange(IReaderConfig.Speaker.DUYY);
                break;
        }
    }

    /**
     * 退出语音合成
     */
    @OnClick(R.id.reader_tts_exit)
    public void exit() {
        if (mListener != null) mListener.onTtsExit();
    }


    /**
     * 语音合成设置回调
     */
    public interface IReaderTtsChangeListener {

        void onTtsSpeedChange(int speed);

        void onTtsSpeakerChange(int speaker);//修改发音人

        void onTtsExit(); //退出语音朗读
    }
}
