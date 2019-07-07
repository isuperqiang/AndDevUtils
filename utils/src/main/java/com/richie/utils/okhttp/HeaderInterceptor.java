package com.richie.utils.okhttp;

import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 添加请求的头信息，User Agent
 *
 * @author Richie on 2018.12.31
 */
final class HeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        String userAgent = HttpUtils.getUserAgent();
        if (!TextUtils.isEmpty(userAgent)) {
            builder.addHeader(HttpUtils.HEAD_KEY_USER_AGENT, userAgent);
        }
        builder.tag(request.url());
        return chain.proceed(builder.build());
    }
}
