package com.aap.rss.lentaru.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.aap.rss.lentaru.MVP.RssView;
import com.aap.rss.lentaru.R;

import com.aap.rss.lentaru.newsList.MainNewsGson;

import java.util.List;

/**
 * Created by grok on 7/1/16.
 */
public class ArticleFragment extends RssView {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_articletext, container,false);

        final ProgressBar progressBar= (ProgressBar)getParentActivity().findViewById(R.id.mainProgress);
        progressBar.setVisibility(View.VISIBLE);
        container.setVisibility(View.INVISIBLE);


        Toolbar toolbar = (Toolbar)getParentActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);

        Bundle args = this.getArguments();
        String url = args.getString("url");

        final WebView webView = (WebView) view.findViewById(R.id.webView);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(webView, url);
                progressBar.setVisibility(View.GONE);

                container.setVisibility(View.VISIBLE);
            }
        });

        webView.loadUrl(url);

        return view;
    }

    @Override
    public void onInternetFail() {

    }

    @Override
    public void onNewsLoaded(List<MainNewsGson> items) {

    }



}
