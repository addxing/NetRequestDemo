package com.example.baselibrary.network.interceptor;


import com.example.baselibrary.utils.log.Loger;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 服务器响应超时重连拦截器
 */
public class RetryIntercepter implements Interceptor {
    public int maxRetry;//最大重试次数
    private int retryNum = 0;//假如设置为3次重试的话，则最大可能请求4次（默认1次+3次重试）

    public RetryIntercepter(int maxRetry) {
        this.maxRetry = maxRetry;
    }

    @Override
    public Response intercept(Chain chain) {
        Request request = chain.request();
        Response response = null;
        try {
            response = chain.proceed(request);
        } catch (IOException e) {
                while (e instanceof SocketTimeoutException&& retryNum < maxRetry) {
                    retryNum++;
                    try {
                        response = chain.proceed(request);
                        Loger.i("RetryIntercepter","重连次数="+retryNum);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
            }
        }

        return response;
    }
}

