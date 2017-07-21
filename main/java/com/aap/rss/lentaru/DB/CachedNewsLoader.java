package com.aap.rss.lentaru.DB;

import android.content.Context;

import com.aap.rss.lentaru.newsList.MainNewsGson;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by grok on 8/25/16.
 */
public class CachedNewsLoader {


    private NewsCacheHelper mNewsCacheHelper;
    private Integer mNumberToLoad=15;

    public CachedNewsLoader(Context context) {
        mNewsCacheHelper = new NewsCacheHelper(context);
    }

    public Observable loadCachedNews() {
        Observable listProducer = Observable.create(new Observable.OnSubscribe<List<MainNewsGson>>() {
            @Override
            public void call(Subscriber<? super List<MainNewsGson>> subscriber) {
                subscriber.onNext(mNewsCacheHelper.selectItemsRange(mNumberToLoad));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
        return listProducer;
    }
}
