package com.aap.rss.lentaru.newsList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aap.rss.lentaru.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by grok on 5/27/16.
 */
  public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsElements>   {
        private static String TAG="NewsAdapter";
        private List<MainNewsGson> newsDataList = new ArrayList<MainNewsGson>();
        private Date rightNowTime;

        public class NewsElements extends RecyclerView.ViewHolder {

            TextView newsTitle;
            TextView date;

            ImageView backImage;
            Typeface roboto;

            ImageView commentBubble;

            TextView articleText;

            TextView isTablet;

            NewsElements(View element) {
                super(element);
                newsTitle = (TextView)element.findViewById(R.id.newsTitle);
                date=(TextView)element.findViewById(R.id.date);


                backImage= (ImageView)element.findViewById(R.id.backImage);
                roboto = Typeface.createFromAsset(element.getContext().getAssets(), "roboto/Roboto-Thin.ttf");

                commentBubble = (ImageView)element.findViewById(R.id.commentBubble);
                articleText = (TextView)element.findViewById(R.id.artText);

                isTablet =(TextView)element.findViewById(R.id.isTablet);
            }



        }


        public NewsAdapter(List<MainNewsGson> newsDataList)
        {
            if (newsDataList!=null) {
                this.newsDataList = newsDataList;

            }
            rightNowTime = new Date();
        }



        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public NewsElements onCreateViewHolder(ViewGroup parent, int viewType) {
            View v;
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mainnews, parent, false);
            NewsElements newsAdapter = new NewsElements(v);
            return newsAdapter;
        }



        @Override
        public void onBindViewHolder(NewsElements holder, int position) {
            Log.v("NewsAdapter", "isTablet is" + holder.isTablet );
            if (holder.isTablet == null) {
                StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
                layoutParams.setFullSpan(true);
            }
            MainNewsGson item = newsDataList.get(position);

            tuneTextView(holder.newsTitle, holder.roboto, item.getTitle());

            if (item.getImage() != null){
                holder.backImage.setImageBitmap(item.getImage());
            }else if (item.getImageUrl() != null) {
                //good answer about loading proper image http://stackoverflow.com/questions/9279111/determine-if-the-device-is-a-smartphone-or-tablet
                Picasso.with(holder.backImage.getContext()).load(item.getImageUrl()).into(holder.backImage);
            }

         //   tuneTextView(holder.comments, holder.roboto,item.getCommentCount().toString());
            if (holder.date!=null) {
                tuneTextView(holder.date, holder.roboto, getWhenPublished(holder.date.getContext(), item.getPublishedAt()));
            }
            if (holder.commentBubble != null){
                holder.commentBubble.setColorFilter(Color.WHITE);
            }

            if (holder.articleText != null){
                tuneTextView( holder.articleText, holder.roboto, item.getDescription());
                Log.d(TAG, "descr: "+item.getDescription());
            } else {
                Log.d(TAG, "descr null");
            }
            //    holder.subOnlyText.setTypeface(holder.roboto,Typeface.BOLD);
           //holder.subOnlyText.setTextSize(holder.subOnlyText.getContext().getResources().);
        }



    @Override
    public int getItemCount() {
            return newsDataList.size();
        }

    @Override
    public int getItemViewType(int position) {
        return 0;

    }


    private void tuneTextView(TextView view, Typeface font, String text)
    {
        view.setAlpha(1);
        view.setText(text);
    }

    @Override
    public boolean onFailedToRecycleView(NewsElements holder) {
        return true;
    }

    private String getWhenPublished(Context ctx, String lentaTime) {
            String publishedAt = lentaTime.replaceAll(".+,\\s","");
        try {
            DateFormat df = new SimpleDateFormat("dd MMM yyyy H:m:ss Z", Locale.ENGLISH);
            Log.e(TAG, "now: "+df.format(rightNowTime));
            Date publishingDate = df.parse(publishedAt);

            Long timeDiff = Math.abs(publishingDate.getTime() - rightNowTime.getTime());


            df = new SimpleDateFormat("H");
            df.setTimeZone(TimeZone.getTimeZone("GMT"));
            String timePassed= df.format(new Date(timeDiff));


            if (Integer.parseInt(timePassed)>0) {
                if (Integer.parseInt(timePassed)>1){
                    return timePassed + " "+ ctx.getResources().getString(R.string.hoursAgo);


                } else {
                    return timePassed + " "+  ctx.getResources().getString(R.string.hourAgo);
                }
            } else {
                df = new SimpleDateFormat("m");
                timePassed = df.format(new Date(timeDiff));
                if (Integer.parseInt(timePassed)>1){
                    return timePassed + " "+  ctx.getResources().getString(R.string.minutesAgo);
                } else {
                    return ctx.getResources().getString(R.string.rightNow);
                }
            }
        } catch (ParseException e) {
            Log.e("NewsAdapter", " "+e.getMessage().toString());

            return "";
        }
    }

}
