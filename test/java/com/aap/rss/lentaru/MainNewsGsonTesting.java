package com.aap.rss.lentaru;

import android.util.Log;

import com.aap.rss.lentaru.newsList.MainNewsGson;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowLog;

/**
 * Created by grok on 7/6/17.
 */


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)

public class MainNewsGsonTesting {

    MainNewsGson newsGson;

    @Before
    public void setUp() throws Exception {
        ShadowApplication context = Shadows.shadowOf(RuntimeEnvironment.application);
        newsGson= new MainNewsGson();
        newsGson.setPublishedAt("Thu, 06 Jul 2017 09:27:00 +0300");
    }

    @Test
    public void idGenerator() {
        ShadowLog.stream = System.out;

        Long id = newsGson.getId();
        newsGson= new MainNewsGson();
        newsGson.setPublishedAt("Thu, 06 Jul 2017 09:27:01 +0300");
        Long id2 = newsGson.getId();
        newsGson= new MainNewsGson();
        newsGson.setPublishedAt("Thu, 06 Jul 2017 09:28:00 +0300");
        Long id3 = newsGson.getId();
        Log.v("MainNewsGsonTesting", id + " " + id2 + " " + id3 + " " + newsGson.getPublishedAt());
        Assert.assertTrue((id > 0) && (id2 > 0) && (id3 > 0));
        Assert.assertTrue((id2 > id) && (id3 > id2));
    }
}

