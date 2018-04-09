package com.example.daddxing.netrequestdemo.api;

import com.example.daddxing.netrequestdemo.bean.GetTestBean;
import com.example.daddxing.netrequestdemo.bean.PostTestBean;
import com.example.daddxing.netrequestdemo.url.URLConfig;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;


/**
 * @Headers           添加请求头
 *
 * @Header            添加不固定值的Header
 *
 * @Body              用于非表单请求体
 *
 * @Field   @FieldMap 适用于表单字段，传入键值对
 *
 * @Part    @PartMap  适用于表单字段，有文件上传的情况
 *
 * @Query   @QueryMap 适用于表单字段，功能和@Field与@FieldMap一样区别在于@Query与@QueryMap的数据体现在URL上，@Field与@FieldMap的数据体现在请求体上，
 *                    但生成的数据是一样的。推荐用@Field与@FieldMap因为请求体大小没有限制，URL的长度有限制。
 *
 * @path              URL缺省值
 *
 * @URL               URL设置
 */

public interface ApiMethod  {

    @GET()
    Observable<GetTestBean> getTest1(@Url String url);

    @POST()
    @FormUrlEncoded
    Observable<PostTestBean> postTest1(@Url String url,@FieldMap Map<String, String> map);





    //上传单张图片
    @POST("服务器地址")
    Observable<Object> imageUpload(@Part() MultipartBody.Part img);
    //上传多张图片
    @POST("服务器地址")
    Observable<Object> imagesUpload(@Part() List<MultipartBody.Part> imgs);
}
