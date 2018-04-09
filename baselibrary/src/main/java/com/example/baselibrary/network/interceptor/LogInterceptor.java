package com.example.baselibrary.network.interceptor;

import com.example.baselibrary.utils.log.Loger;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


/**
 * Created by Administrator on 2017/9/19 0019.
 */

public class LogInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {



        //这个chain里面包含了request和response，所以你要什么都可以从这里拿
        Request request = chain.request();
        long t1 = System.nanoTime();//请求发起的时间
        String method = request.method();
        if ("POST".equals(method)) {
            StringBuilder sb = new StringBuilder();
            if (request.body() instanceof FormBody) {
                FormBody body = (FormBody) request.body();
                for (int i = 0; i < body.size(); i++) {
                    sb.append(body.encodedName(i) + "=" + body.encodedValue(i) + ",");
                }
                sb.delete(sb.length() - 1, sb.length());
                /**
                 * sb.toString()格式是key1=value1,key2=value2,key3=value3,为了打印url美观，把“，”替换成了“&”
                 */
                Loger.i("LJQ", String.format("发送POST请求 %s ", request.url() + sb.toString().replaceAll(",", "&")));//只打印URL
//                Loger.i("LJQ", String.format("发送请求 %s on %s %n%s %nRequestParams:{%s}",request.url(), chain.connection(), request.headers(), sb.toString()));
            }
        } else if("GET".equals(method)) {
            Loger.json("LJQ", String.format("发送GET请求 %s", request.url()));//只打印URL
//            Loger.json("LJQ", String.format("发送请求 %s on %s%n%s", request.url(), chain.connection(), request.headers()));
        }





        Response response = chain.proceed(request);
        long t2 = System.nanoTime();//收到响应的时间
        /**
         * 这里不能直接使用response.body().string()的方式输出日志
         * 因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一
         * 个新的response给应用层处理
         */
        ResponseBody responseBody = response.peekBody(1024 * 1024);
        if(response.request().method().equals("POST")){
            StringBuilder sb1 = new StringBuilder();
            if (request.body() instanceof FormBody) {
                FormBody body = (FormBody) request.body();
                for (int i = 0; i < body.size(); i++) {
                    sb1.append(body.encodedName(i) + "=" + body.encodedValue(i) + ",");
                }
                sb1.delete(sb1.length() - 1, sb1.length());
//                        Loger.i("LJQ",String.format("接收POST响应: [%s] %n返回json:【%s】 %.1fms %n%s", response.request().url().toString()+sb1.toString().replaceAll(",", "&"),responseBody.string(),(t2 - t1) / 1e6d,response.headers()));
                Loger.json("LJQ", String.format("接收POST响应: %s  %n返回json数据:%n%s",  response.request().url().toString()+sb1.toString().replaceAll(",", "&"), responseBody.string()));//只返回json
            }
        }else if(response.request().method().equals("GET")){
//            Loger.i("LJQ",String.format("接收GET响应: [%s] %n返回json数据:【%s】 %.1fms %n%s",response.request().url(),responseBody.string(),(t2 - t1) / 1e6d,response.headers()));
            Loger.json("LJQ", String.format("接收GET响应: %s  %n返回json数据:%n%s",  response.request().url().toString(), responseBody.string()));//只返回json
        }


        return response;
    }
}