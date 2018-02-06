package com.monster.monstersport.http.okhttp;

import com.monster.monstersport.http.interceptor.BasicParamsInterceptor;
import com.trello.rxlifecycle2.internal.Preconditions;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * OkHttpClient 生成类
 *
 * @author zhzy
 * @date 2017/11/1
 */
public class OkHttpClientProvider {

    private final static long DEFAULT_CONNECT_TIMEOUT = 5;
    private final static long DEFAULT_WRITE_TIMEOUT = 20;
    private final static long DEFAULT_READ_TIMEOUT = 5;

    public static OkHttpClient getDefaultOkHttpClient() {
        return getOkHttpClient(null);
    }

    private static OkHttpClient getOkHttpClient(Interceptor cacheControl) {
        //定制OkHttp
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        //设置超时时间
        httpClientBuilder.connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS);
        //设置缓存
//        File cacheFile = new File(HyApplication.getInstance().getCacheDir(), "OkHttpCache");
//        Cache cache = new Cache(cacheFile, 100 * 1024 * 1024);
//        httpClientBuilder.cache(cache);
        //设置拦截器
        httpClientBuilder.addInterceptor(new UserAgentInterceptor("Android Device"));

        httpClientBuilder.addInterceptor(new BasicParamsInterceptor.Builder()
                .addQueryParam("api_version", "2")
                .build());
        return httpClientBuilder.build();
    }


    private static class UserAgentInterceptor implements Interceptor {
        private static final String USER_AGENT_HEADER_NAME = "User-Agent";
        private final String userAgentHeaderValue;

        UserAgentInterceptor(String userAgentHeaderValue) {
            this.userAgentHeaderValue = Preconditions.checkNotNull(userAgentHeaderValue, "userAgentHeaderValue = null");
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            final Request originalRequest = chain.request();
            final Request requestWithUserAgent = originalRequest.newBuilder()
                    .removeHeader(USER_AGENT_HEADER_NAME)
                    .addHeader(USER_AGENT_HEADER_NAME, userAgentHeaderValue)
                    .addHeader("App", "1")
                    .addHeader("Accept", "application/json")
                    .build();
            return chain.proceed(requestWithUserAgent);
        }
    }


}
