package com.monster.monstersport.http;


import com.monster.monstersport.http.api.IHyangApi;
import com.monster.monstersport.http.okhttp.OkHttpClientProvider;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求
 *
 * @author zhzy
 * @date 2017/11/1
 */
public class HttpUtils {

    private IHyangApi mApiInstance;

    private HttpUtils() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(OkHttpClientProvider.getDefaultOkHttpClient())
                .baseUrl(IHyangApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        mApiInstance = retrofit.create(IHyangApi.class);
    }


    private static class SingletonHolder {
        private static final HttpUtils INSTANCE = new HttpUtils();
    }

    /**
     * 获取单例
     */
    private static HttpUtils getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 获取API单例
     */
    public static IHyangApi getApiInstance() {
        return getInstance().mApiInstance;
    }

}
