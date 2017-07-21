package com.aap.rss.lentaru.MVP;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aap.rss.lentaru.Activities.MainActivity;
import com.aap.rss.lentaru.newsList.MainNewsGson;

import java.util.List;

/**
 * Created by grok on 10/25/16.
 */
public abstract class RssView extends Fragment {

   public String newsUrl="http://lenta.ru";
   private MainActivity activity;


    public RssView() {

    }



   @Nullable
   @Override
   public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    return super.onCreateView(inflater, container, savedInstanceState);
   }

   @Override
   public void onAttach(Context context) {
    super.onAttach(context);
    activity = (MainActivity) context;
   }

   public interface MyInterface {
    public void onItemClicked();
   }

   @Override
   public void onDetach() {
    super.onDetach();
    activity=null;
   }

    public MainActivity getParentActivity()
    {
        return activity;
    }

    public abstract void onNewsLoaded(List<MainNewsGson> items);

    public abstract void onInternetFail();


}
