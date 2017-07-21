package com.aap.rss.lentaru.newsList;

import android.graphics.Bitmap;
import android.util.Log;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by grok on 5/27/16.
 */
@Root(name = "item")
public class MainNewsGson {

    private static String TAG = "MainNewsGson";

    Long id;

    Bitmap image;

    Integer commentCount=0;

    @Element(name = "guid")
    String guid="";

    @Element(name = "title")
    String title;

    @Element(name = "link")
    String link;

    @Element(name = "description", data=true)
    String description;

    @Element(name = "pubDate")
    String publishedAt="";

    @Element(name = "enclosure", required = false)
    EnclosureXML enclosureXML;

    @Element(name = "category")
    String category;


    public Long getId() {

       if (id == null) {
            String lentaTime = getPublishedAt();
            long epoch = 0;
            long publishedUnix = 0;
            long newsId = 0;
            try {
                String publishedAt = lentaTime.replaceAll(".+,\\s", "");
                DateFormat df = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
                epoch = df.parse("4 Jul 2017").getTime() / 1000;
                df = new SimpleDateFormat("dd MMM yyyy H:m:ss Z", Locale.ENGLISH);
                publishedUnix = df.parse(publishedAt).getTime()/1000;
                newsId = publishedUnix - epoch;
            } catch (ParseException e) {
                e.printStackTrace();
                Log.v("MainNewsGson", "error id "+e.getMessage());
            }
            id = newsId;
        }
      return id;
    }

    public MainNewsGson()
    {

    }



    public void setDescription(String text) {
        this.description=text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }


    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getImageUrl() { return  (enclosureXML != null) ? enclosureXML.getUrl() : null;}

    public void setImageUrl(String url){
       if (enclosureXML == null) { enclosureXML= new EnclosureXML();}
        enclosureXML.setUrl(url);
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getDescription() {
        return description;
    }


}


class EnclosureXML {
    @Attribute(name = "length")
    private String length="";

    @Attribute(name = "type")
    private String type="";
    @Attribute(name = "url")
    private String url="";

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}