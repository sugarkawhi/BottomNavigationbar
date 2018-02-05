package com.monster.monstersport.dialog;

import android.content.Context;
import android.widget.SeekBar;

import com.monster.monstersport.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 设置字体
 * Created by ZhaoZongyao on 2018/1/30.
 */

public class SpacingSettingDialog extends BottomPopDialog {
    @BindView(R.id.reader_seekBar_letterSpacing)
    SeekBar mLetterSeekBar;
    @BindView(R.id.reader_seekBar_linerSpacing)
    SeekBar mLineSeekBar;
    @BindView(R.id.reader_seekBar_paragraphSpacing)
    SeekBar mParagraphSeekBar;

    private IReaderSpacingChangeListener mSpacingChangeListener;

    public SpacingSettingDialog(Context context) {
        super(context);
        ButterKnife.bind(this, getContentView());
        setListener();
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_reader_spacing;
    }

    private void setListener() {
        mLetterSeekBar.setOnSeekBarChangeListener(new MySeekBarChangeListener(mLetterSeekBar));
        mLineSeekBar.setOnSeekBarChangeListener(new MySeekBarChangeListener(mLineSeekBar));
        mParagraphSeekBar.setOnSeekBarChangeListener(new MySeekBarChangeListener(mParagraphSeekBar));
    }

    public SpacingSettingDialog setSpacingChangeListener(IReaderSpacingChangeListener spacingChangeListener) {
        mSpacingChangeListener = spacingChangeListener;
        return this;
    }

    public SpacingSettingDialog setLetterSpacing(int progress) {
        mLetterSeekBar.setProgress(progress);
        return this;
    }

    public SpacingSettingDialog setLineSpacing(int progress) {
        mLineSeekBar.setProgress(progress);
        return this;
    }

    public SpacingSettingDialog setParagraphSpacing(int progress) {
        mParagraphSeekBar.setProgress(progress);
        return this;
    }

    private class MySeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        private SeekBar mSeekBar;

        MySeekBarChangeListener(SeekBar seekBar) {
            mSeekBar = seekBar;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (mSpacingChangeListener == null) return;
            switch (mSeekBar.getId()) {
                case R.id.reader_seekBar_letterSpacing:
                    mSpacingChangeListener.onLetterSpacingChange(progress);
                    break;
                case R.id.reader_seekBar_linerSpacing:
                    mSpacingChangeListener.onLineSpacingChange(progress);
                    break;
                case R.id.reader_seekBar_paragraphSpacing:
                    mSpacingChangeListener.onParagraphSpacingChange(progress);
                    break;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    public interface IReaderSpacingChangeListener {
        void onLetterSpacingChange(int progress);

        void onLineSpacingChange(int progress);

        void onParagraphSpacingChange(int progress);
    }

}
