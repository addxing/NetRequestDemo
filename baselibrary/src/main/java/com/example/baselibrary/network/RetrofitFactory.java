package com.example.baselibrary.network;

import android.util.Log;

import com.example.baselibrary.network.interceptor.LogInterceptor;
import com.example.baselibrary.network.interceptor.RetryIntercepter;
import com.example.baselibrary.utils.log.Loger;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import io.reactivex.disposables.Disposable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author yyh
 * @date 2017/4/9
 * @description 写自己的代码, 让别人说去吧!
 */

public class RetrofitFactory {
    private static RetrofitFactory retrofitFactory;
    private Retrofit retrofit;
    public OkHttpClient okHttpClient;

    private RetrofitFactory(){
         okHttpClient=new OkHttpClient.Builder()
                .connectTimeout(60000, TimeUnit.SECONDS)//连接超时6秒
                .readTimeout(60000, TimeUnit.SECONDS)//读超时6秒
                .writeTimeout(60000, TimeUnit.SECONDS)//写超时6秒
                .addInterceptor(new LogInterceptor())//添加日志拦截器
                .addInterceptor( new RetryIntercepter(3))//添加服务器响应超时重连拦截器,重连3次
                .retryOnConnectionFailure(true)//是否重连
                .build();
         retrofit =new Retrofit.Builder()
                .baseUrl("https://www.baidu.com/")
                .addConverterFactory(GsonConverterFactory.create())//添加gson转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//添加rxjava转换器
                .client(okHttpClient)
                .build();

    }

    public static RetrofitFactory getInstence(){
        if (retrofitFactory ==null){
            synchronized (RetrofitFactory.class) {
                if (retrofitFactory == null)
                    retrofitFactory = new RetrofitFactory();
            }
        }
        return retrofitFactory;
    }

    public <T> T Api(Class<T> service) {
        return  retrofit.create(service);
    }
    /**
     * 取消订阅
     */
    public static void cancelSubscription(List<Disposable> disposableList) {
        for (int i = 0; i < disposableList.size(); i++) {
            if (disposableList.get(i) != null && !disposableList.get(i).isDisposed()) {
                disposableList.get(i).dispose();
            }
        }

    }
}
