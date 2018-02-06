package com.monster.monstersport.http.observer;

import android.net.ParseException;
import android.text.TextUtils;

import com.google.gson.JsonParseException;
import com.monster.monstersport.http.BaseHttpResult;
import com.monster.monstersport.http.api.ApiConfig;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import me.sugarkawhi.mreader.utils.L;
import retrofit2.HttpException;

import static com.monster.monstersport.http.observer.DefaultObserver.ExceptionReason.BAD_NETWORK;
import static com.monster.monstersport.http.observer.DefaultObserver.ExceptionReason.CONNECT_ERROR;
import static com.monster.monstersport.http.observer.DefaultObserver.ExceptionReason.CONNECT_TIMEOUT;
import static com.monster.monstersport.http.observer.DefaultObserver.ExceptionReason.PARSE_ERROR;
import static com.monster.monstersport.http.observer.DefaultObserver.ExceptionReason.UNKNOWN_ERROR;

/**
 * DefaultObserver
 *
 * @author zhzy
 * @date 2017/11/2
 */

public abstract class DefaultObserver<T> implements Observer<BaseHttpResult<T>> {

    private static final String TAG = "DefaultObserver";

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(@NonNull BaseHttpResult<T> result) {
        if (result.getCode() == ApiConfig.CODE_SUCCESS)
            onSuccess(result.getData());
        else
            onFail(result);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        e.printStackTrace();
        if (e instanceof HttpException) {//HTTP错误
            onException(BAD_NETWORK);
        } else if (e instanceof ConnectException
                || e instanceof UnknownHostException) {   //   连接错误
            onException(CONNECT_ERROR);
        } else if (e instanceof InterruptedIOException) {   //  连接超时
            onException(CONNECT_TIMEOUT);
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {   //  解析错误
            onException(PARSE_ERROR);
        } else {
            onException(UNKNOWN_ERROR);
        }
    }

    @Override
    public void onComplete() {

    }

    /**
     * 数据请求成功 code==ApiConfig.CODE_SUCCESS
     */
    protected abstract void onSuccess(T t);

    /**
     * 服务器返回数据 code！=ApiConfig.CODE_SUCCESS
     */
    protected void onFail(BaseHttpResult<T> result) {
        String message = result.getMessage();
        if (TextUtils.isEmpty(message)) {
            L.e(TAG, "onFail -> message is empty");
        } else {
            L.e(TAG, "onFail -> " + message);
        }
    }

    /**
     * 请求网络出现异常状况
     */
    protected void onException(ExceptionReason reason) {
        switch (reason) {
            case CONNECT_ERROR:
//                ToastUtils.show(R.string.connect_error);
                break;
            case CONNECT_TIMEOUT:
//                ToastUtils.show(R.string.connect_timeout);
                break;
            case BAD_NETWORK:
//                ToastUtils.show(R.string.bad_network);
                break;
            case PARSE_ERROR:
//                ToastUtils.show(R.string.parse_error);
                break;
            case UNKNOWN_ERROR:
            default:
//                ToastUtils.show(R.string.unknown_error);
                break;
        }
    }

    /**
     * 请求网络失败原因
     */
    public enum ExceptionReason {
        /**
         * 解析数据失败
         */
        PARSE_ERROR,
        /**
         * 网络问题
         */
        BAD_NETWORK,
        /**
         * 连接错误
         */
        CONNECT_ERROR,
        /**
         * 连接超时
         */
        CONNECT_TIMEOUT,
        /**
         * 未知错误
         */
        UNKNOWN_ERROR,
    }
}
