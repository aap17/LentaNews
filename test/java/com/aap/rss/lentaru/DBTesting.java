package com.aap.rss.lentaru;

import android.util.Log;

import com.aap.rss.lentaru.DB.NewsCacheHelper;
import com.aap.rss.lentaru.newsList.MainNewsGson;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;

import org.junit.After;
import org.junit.Before;
import org.robolectric.RuntimeEnvironment;

import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by grok on 12/28/16.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)


public class DBTesting   {
    private NewsCacheHelper mHelper;
    MainNewsGson mNewsGson;
    Integer mNewsGsonId;

    @Before
    public void setUp() throws Exception {


        ShadowApplication context = Shadows.shadowOf(RuntimeEnvironment.application);

        mHelper = new NewsCacheHelper(context.getApplicationContext());
        mNewsGson = initElement("Thu, 06 Jul 2017 09:28:00 +0300");

        mNewsGsonId = (int) (long) mNewsGson.getId();
    }

    private MainNewsGson initElement(String string){
        MainNewsGson element;
        element = new MainNewsGson();
        element.setTitle("Simple title");
        element.setDescription("Simple text");
        element.setCommentCount(10);
        element.setImageUrl(null);
        element.setPublishedAt(string);
        element.setImageUrl("https://icdn.lenta.ru/images/2017/07/06/11/20170706111238828/pic_d2682ccce4d2ffb0ce34ef1d795c49bc.jpg");
        return element;
    }

    @After
   public void tearDown() throws Exception {
        mHelper.close();
    }




   @Test
    public void testCreateDB()

    {
        assert(mHelper.getReadableDatabase()!=null);
    }



    @Test
    public void getfrom5to3()
    {
       List<MainNewsGson> expectedList = new ArrayList<MainNewsGson>();


        for (int i=0; i<6; i++)
        {

            expectedList.add(mNewsGson);
        }
            mHelper.insertData(expectedList);



        List<MainNewsGson> dBlist = mHelper.selectItemsRange(3);



        for (int i=5; i<2; i--)
        {

            Log.d("Roboelectric", "exp|got: " + expectedList.get(i).getId() + "|" + dBlist.get(i).getId() );
            Assert.assertEquals(new Integer(i), dBlist.get(i).getId());
        }

    }

    @Test
    public void getTheNewst3()
    {
        ShadowLog.stream = System.out;

        List<MainNewsGson> expectedList = new ArrayList<MainNewsGson>();
        Integer rangeSelection = 10;

        for (int i=0; i<rangeSelection; i++)
        {
            MainNewsGson  newEl = initElement("Thu, 06 Jul 2017 09:28:0" +i+" +0300");
            expectedList.add(newEl);
        }
        mHelper.insertData(expectedList);

        List<MainNewsGson> dBlist = mHelper.selectItemsRange(rangeSelection);



        for (int i=0; i < rangeSelection; i++){
            Assert.assertEquals(expectedList.get(i).getPublishedAt(), dBlist.get(rangeSelection-i-1).getPublishedAt());
            Assert.assertEquals(expectedList.get(i).getId(), dBlist.get(rangeSelection-i-1).getId());
            Assert.assertEquals(expectedList.get(i).getTitle(), dBlist.get(rangeSelection-i-1).getTitle());
            Assert.assertTrue(dBlist.get(rangeSelection-i-1).getImage().getByteCount() > 0);
        }
    }



    @Test
    public void loadedRangeNewsWithImage() {

        ShadowLog.stream = System.out;
        mNewsGson.setImageUrl("https://xa-live-static.s3-eu-central-1.amazonaws.com/upload_images/images/000/000/002/small/featured-article-1.png");
        List<MainNewsGson> mList = new ArrayList<>();
        mList.add(mNewsGson);
        mHelper.insertData(mList);
        List<MainNewsGson>element = mHelper.selectItemsRange(1);//mNewsGson.getId()+1);
        Log.e("Roboelectric", "image size " + element.get(0).getImage().getByteCount());
        Assert.assertTrue(element.get(0).getImage().getByteCount() > 0);
    }


}
