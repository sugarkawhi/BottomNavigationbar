package me.sugarkawhi.hyloadingview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 加载动画
 *
 * @author zhzy
 * @date 2018/1/8
 */
public abstract class HyLoadingView extends View {
    public static final int STATE_LOADING = 1;
    public static final int STATE_ERROR = 2;
    public static final int STATE_SUCCESS = 3;
    //当前状态
    protected int mCurrentState;
    protected int mLoadingColor;
    protected int mErrorColor;
    protected int mSuccessColor;
    protected int mLoadingDuration;
    protected int mStokeWidth;
    protected RectF mRectF;
    protected Paint mPaint;
    protected Path mPath;
    protected int mWidth;

    public HyLoadingView(Context context) {
        this(context, null);
    }

    public HyLoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HyLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.HyLoadingView);
        mLoadingColor = array.getColor(R.styleable.HyLoadingView_hy_loading_color, Color.BLUE);
        mErrorColor = array.getColor(R.styleable.HyLoadingView_hy_loading_color, Color.RED);
        mSuccessColor = array.getColor(R.styleable.HyLoadingView_hy_loading_color, Color.GREEN);
        mLoadingDuration = array.getInt(R.styleable.HyLoadingView_hy_loading_duration, 200);
        mStokeWidth = array.getDimensionPixelSize(R.styleable.HyLoadingView_hy_loading_stoke_width, 10);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
        mPath = new Path();
        mRectF = new RectF();
        mCurrentState = STATE_LOADING;
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                mWidth = MeasureSpec.getSize(widthMeasureSpec);
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
            default:
                mWidth = 0;
        }
        mRectF.set(mStokeWidth / 2, mStokeWidth / 2 + mWidth / 2,
                mWidth - mStokeWidth / 2, mWidth - mStokeWidth / 2 + mWidth / 2);
        setMeasuredDimension(mWidth, (int) (mWidth * 1.5f));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (mCurrentState) {
            case STATE_LOADING:
                drawLoading(canvas);
                break;
            case STATE_ERROR:
                drawError(canvas);
                break;
            case STATE_SUCCESS:
                drawSuccess(canvas);
                break;
        }
    }


    public void setCurrentState(int currentState) {
        mCurrentState = currentState;
    }

    public int getCurrentState() {
        return mCurrentState;
    }

    public void loading() {
        this.mCurrentState = STATE_LOADING;
        invalidate();
    }

    public void success() {
        this.mCurrentState = STATE_SUCCESS;
        invalidate();
    }

    public void error() {
        this.mCurrentState = STATE_ERROR;
        invalidate();
    }


    abstract void drawLoading(Canvas canvas);

    abstract void drawSuccess(Canvas canvas);

    abstract void drawError(Canvas canvas);


}
