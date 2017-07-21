package com.aap.rss.lentaru.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aap.rss.lentaru.Listeners.RecyclerItemClickListener;
import com.aap.rss.lentaru.MVP.Presenter;
import com.aap.rss.lentaru.MVP.RssView;
import com.aap.rss.lentaru.R;
import com.aap.rss.lentaru.newsList.MainNewsGson;
import com.aap.rss.lentaru.newsList.NewsAdapter;

import java.util.List;

/**
 * Created by admin on 21.06.16.
 */

public class MainNewsFragment extends RssView {


    private List<MainNewsGson> mNewsItem;

    private RecyclerView mRecyclerView;

    private Presenter mPresenter;

    private ProgressBar mProgressBar;

    private View mNoInternetView;

    private boolean isTablet=false;

    private SwipeRefreshLayout swipeRefreshLayout;

    public MainNewsFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.mainnews_fragment, container,false);

        mProgressBar = (ProgressBar)getParentActivity().findViewById(R.id.mainProgress);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.newsList);

        mNoInternetView = view.findViewById(R.id.nointernetbanner);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);

        showProgress(true);
        if (view.findViewById(R.id.isTablet) != null){
            isTablet=true;
        }

        Bundle args = this.getArguments();
        if (args!=null) {
            newsUrl = args.getString("url");
        }
        //MVP
        if (mNewsItem ==null) {
            Log.d("MainNewsFragment", "mNewsItem null");
            mPresenter = new Presenter(this);
            mPresenter.getNews(newsUrl);
        } else
        {
            initilizeRecyclerView(mNewsItem);
        }

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(view.getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (mNewsItem.get(position).getLink()!=null)
                        {
                          getParentActivity().loadNews(mNewsItem.get(position).getLink());
                        }
                    }
                }));

        mNoInternetView.setVisibility(View.GONE);
        mNoInternetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress(true);
                mPresenter.getNews(newsUrl);
                view.setVisibility(View.GONE);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("MainNewsFragment", "refresh!");
                mPresenter.getNews(newsUrl);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;

    }

    @Override
    public void onNewsLoaded(List<MainNewsGson> items) {
    //    if (mNewsItem != null) {
     //       mRecyclerView.getAdapter().notifyItemMoved(0, mNewsItem.size());
     //   }
        initilizeRecyclerView(items);
        showProgress(false);
    }


    private void showProgress(Boolean isProgress) {
        if (isProgress) {
            getParentActivity().findViewById(R.id.frame_container).setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            getParentActivity().findViewById(R.id.frame_container).setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public  void onInternetFail() {
        mNoInternetView.setVisibility(View.VISIBLE);

    }



    private void initilizeRecyclerView(List<MainNewsGson> news) {
        if (news!=null) {
            mNewsItem = news;
            mRecyclerView.setAdapter(new NewsAdapter(null));
            mRecyclerView.setAdapter(new NewsAdapter(mNewsItem));
            if (isTablet){
                mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
                Log.v("MainNewsFragment", " tablet");
            } else {
                mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, 1));
                Log.v("MainNewsFragment", " mobile");

            }
            mRecyclerView.getAdapter().notifyDataSetChanged();
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        else {
          Toast.makeText(mRecyclerView.getContext(), "Error loading page",Toast.LENGTH_LONG).show();
        }
        showProgress(false);

    }
}
