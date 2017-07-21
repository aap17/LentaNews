package com.aap.rss.lentaru.MVP;

import android.util.Log;

import com.aap.rss.lentaru.DB.CachedNewsLoader;
import com.aap.rss.lentaru.DB.NewsDBSaver;
import com.aap.rss.lentaru.Retrofit.RetrofitLoader;
import com.aap.rss.lentaru.newsList.MainNewsGson;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by grok on 10/25/16.
 */
public class Presenter {
    private RssView view;

    private CachedNewsLoader cachedNewsLoader;

    public Presenter(RssView view) {
        this.view=view;
    }

    public void getNews(String url) {
            Log.d("Presenter", "get news");
            new RetrofitLoader(this, url);
    }

    public void onInternetDataLoaded(List<MainNewsGson> items) {
        if (items!=null) {
            view.onNewsLoaded(items);
            saveNewData(items);
        }
    }


    private void saveNewData(List<MainNewsGson> items) {
        new NewsDBSaver(view.getActivity().getApplicationContext(), items);
    }

    public void onInternetLoadError() {
        loadRxData();
    }

    public void onDBDataLoaded(List<MainNewsGson> items) {
        Log.d("Presenter", "load by rx items total "+ items.size());
        view.onNewsLoaded(items);

    }

    private void loadRxData() {
        Log.d("Presenter", "rx loading");
        cachedNewsLoader=new CachedNewsLoader(view.getActivity().getApplicationContext());
        Observable<List<MainNewsGson>> observable = cachedNewsLoader.loadCachedNews();
        observable.subscribe(new Subscriber<List<MainNewsGson>>() {
            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onNext(List<MainNewsGson> mainNewsGsons) {
                onDBDataLoaded(mainNewsGsons);
            }
        });
    }
}
