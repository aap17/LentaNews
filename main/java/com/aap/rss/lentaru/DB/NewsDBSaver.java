package com.aap.rss.lentaru.DB;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.aap.rss.lentaru.newsList.MainNewsGson;

import java.util.List;

/**
 * Created by grok on 8/25/16.
 */
public class NewsDBSaver {

    private Context mContext;

    private List<MainNewsGson> mainNewsList;

    private static NewsCacheHelper newsCacheHelper;

    public NewsDBSaver(Context context, List<MainNewsGson> list)
    {
        this.mContext =context;
        this.mainNewsList =list;
        newsCacheHelper = new NewsCacheHelper(mContext);
        Log.e("DB", "saver run, total item " +list.size());
        new saveNewsRangeAsync().execute(list);

    }

    private static void saveNewsRange(List<MainNewsGson> list) {
        newsCacheHelper.insertData(list);
    }

    private static class saveNewsRangeAsync extends AsyncTask<List<MainNewsGson>, Void, Void>
    {

        @Override
        protected Void doInBackground(List<MainNewsGson>... lists) {
            saveNewsRange(lists[0]);
            return null;
        }
    }
}
