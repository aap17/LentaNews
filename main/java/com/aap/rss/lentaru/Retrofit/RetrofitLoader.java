package com.aap.rss.lentaru.Retrofit;

import android.util.Log;

import com.aap.rss.lentaru.MVP.Presenter;
import com.aap.rss.lentaru.newsList.LentaXML;

import retrofit.Callback;
import retrofit.RestAdapter;

import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.SimpleXMLConverter;
import retrofit.http.GET;


/**
 * Created by grok on 10/25/16.
 */
public class RetrofitLoader {
    private Presenter presenter;

    public RetrofitLoader(Presenter presenter, String url)
    {
        this.presenter=presenter;
        retrofitLoad(url);
    }


    private interface  AtomInterface {
        @GET("/rss/top7")
            // @GET("/test2.xml")
        void getRecipeDetails(Callback<LentaXML> response);
    }

    void retrofitLoad(String url)
    {
        final String TAG = "Retrofit";
     RestAdapter adapter =  new RestAdapter.Builder()
                .setEndpoint(url)
                .setConverter(new SimpleXMLConverter())
                //.setLogLevel(RestAdapter.LogLevel.FULL) // Log response in full format
                .build();



        AtomInterface RECIPE_APP = adapter.create(AtomInterface.class);
        RECIPE_APP.getRecipeDetails(new Callback<LentaXML>() {
            @Override
            public void success(LentaXML lentaXML, Response response) {
                presenter.onInternetDataLoaded(lentaXML.getMainNewsGson());
                Log.v(TAG, "lentaXML size "+lentaXML.getMainNewsGson().size());
            }

            @Override
            public void failure(RetrofitError error) {
                presenter.onInternetLoadError();
                Log.e(TAG, "error news loading " + error.getMessage());
            }
        });
    }
}
