package com.example.daddxing.netrequestdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.baselibrary.network.RetrofitFactory;
import com.example.baselibrary.network.cache.manage.Cache;

import com.example.baselibrary.network.observable.SchedulerTransformer;
import com.example.baselibrary.utils.log.Loger;
import com.example.daddxing.netrequestdemo.api.ApiMethod;
import com.example.daddxing.netrequestdemo.api.UploadUtil;
import com.example.daddxing.netrequestdemo.bean.GetTestBean;
import com.example.daddxing.netrequestdemo.bean.PostTestBean;
import com.example.daddxing.netrequestdemo.url.URLConfig;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnGet;
    private Button btnPost;
    private Button btnUpload;
    private TextView tvContent;
    private ImageView ivContent;
    private List<Disposable> disposableList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGet = findViewById(R.id.btn_get);
        btnPost = findViewById(R.id.btn_post);
        ivContent = findViewById(R.id.iv_content);
        btnUpload = findViewById(R.id.btn_upload);
        tvContent = findViewById(R.id.tv_content);
        btnGet.setOnClickListener(this);
        btnPost.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_get:
                getData1();
                break;
            case R.id.btn_post:
                postData1();
                break;
            case R.id.btn_upload:


                break;
        }
    }
    public  void getData1() {
        RetrofitFactory.getInstence().Api(ApiMethod.class)
                .getTest1(URLConfig.get_test)
                .compose(SchedulerTransformer.<GetTestBean>setThread())
                .subscribe(new Observer<GetTestBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableList.add(d);
                        Log.i("Rxjava", "对onSubscribe事件作出响应" );
                    }

                    @Override
                    public void onNext(GetTestBean getTestBean) {
                        Log.i("Rxjava", "对Next事件作出响应" + getTestBean);
                        Cache.getInstance(MainActivity.this).put("getTestBean",getTestBean);
                        tvContent.setText(getTestBean.getData().get(0).getChildren().get(0).getName());
                    }

                    // 当被观察者生产Error事件& 观察者接收到时，会调用该复写方法 进行响应
                    @Override
                    public void onError(Throwable e) {
                        Log.i("Rxjava", "对Error事件作出响应" + e);
                        GetTestBean cacheGetTestBean = Cache.getInstance(MainActivity.this).get("getTestBean");
                        tvContent.setText(cacheGetTestBean.getData().get(0).getChildren().get(0).getName());
                    }

                    @Override
                    public void onComplete() {
                        Log.i("Rxjava", "对onComplete事件作出响应");
                    }

                });

    }

    public  void postData1() {
        Map<String,String> map=new HashMap<>();
        map.put("type","2");
        map.put("provinceId","7");
        map.put("cityId","51");
        RetrofitFactory.getInstence().Api(ApiMethod.class)
                .postTest1(URLConfig.post_test,map)
                .compose(SchedulerTransformer.<PostTestBean>setThread())
                .subscribe(new Observer<PostTestBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableList.add(d);
                        Log.i("Rxjava", "对onSubscribe事件作出响应" );
                    }

                    @Override
                    public void onNext(PostTestBean postTestBean) {
                        Log.i("Rxjava", "对Next事件作出响应" + postTestBean);

                        tvContent.setText(postTestBean.getMsg());
                        Glide.with(MainActivity.this).load("http://ad.yj96179.com/Uploads/Picture/"+postTestBean.getData().get(0).getImgPath()).into(ivContent);
                        Cache.getInstance(MainActivity.this).put("postTestBean",postTestBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("Rxjava", "对Error事件作出响应" + e);

                        PostTestBean cachePostTestBean = Cache.getInstance(MainActivity.this).get("postTestBean");

                        Glide.with(MainActivity.this).load("http://ad.yj96179.com/Uploads/Picture/"+cachePostTestBean.getData().get(0).getImgPath()).into(ivContent);
                        tvContent.setText(cachePostTestBean.getMsg());
                    }

                    @Override
                    public void onComplete() {
                        Log.i("Rxjava", "对onComplete事件作出响应");
                    }
                });

    }
    public void upload(){
        String filepath="图片本地路径";
        UploadUtil.uploadImage(filepath, new Observer() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object o) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void uploads(){
        ArrayList<String> listFilePath=new ArrayList<>();
        listFilePath.add("图片1路径");
        listFilePath.add("图片2路径");
        UploadUtil.uploadImages(listFilePath, new Observer() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object o) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
    @Override
    protected void onDestroy() {
        RetrofitFactory.cancelSubscription(disposableList);
        if( !Cache.getInstance(MainActivity.this).isClosed()){
            Cache.getInstance(MainActivity.this).close();
        }
        super.onDestroy();
    }
}
