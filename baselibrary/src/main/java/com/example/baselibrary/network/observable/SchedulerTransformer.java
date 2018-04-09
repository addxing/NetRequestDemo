package com.example.baselibrary.network.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/3/26.
 */

public class SchedulerTransformer {
    public static  <T> ObservableTransformer<T,T> setThread(){
        return new ObservableTransformer<T,T>() {
            @Override
            public ObservableSource<T>  apply(Observable<T>  upstream) {
                return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}


