package com.aap.rss.lentaru.newsList;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by grok on 6/27/17.
 */

@Root(name = "rss", strict = false)
public class LentaXML
{


    @Path(value = "channel")
    @ElementList(inline = true, name = "item")
    List<MainNewsGson> item;

    public List<MainNewsGson> getMainNewsGson() {
        return item;
    }
}