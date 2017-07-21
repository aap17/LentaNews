package com.aap.rss.lentaru.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


import com.aap.rss.lentaru.newsList.MainNewsGson;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by grok on 8/23/16.
 */
public class NewsCacheHelper extends SQLiteOpenHelper {

    private static String TAG = "NewsCacheHelper";

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "newsDB";

    private static final String TABLE_NEWS = "mewsTable";

    private static final String NEWS_ID = "idnews";

    private static final String TITLE = "title";

    private static final String IMAGE = "image";

    private static final String TEXT = "text";

    private static final String DATE = "pubDate";

    private Context context;

    private String[] allColumns = { NEWS_ID,
           TITLE,IMAGE };

    public NewsCacheHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_NEWS_TABLE = "CREATE TABLE " + TABLE_NEWS + "(" + NEWS_ID + " INTEGER PRIMARY KEY," + TITLE + " TEXT," + IMAGE
                + " BLOB, " + TEXT + " TEXT, " + DATE + " TEXT)";
        sqLiteDatabase.execSQL(CREATE_NEWS_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS);
    }




    private boolean isNewsExists(SQLiteDatabase db, Long newsId) {
        Cursor cursor = db.rawQuery("SELECT 1 FROM " + TABLE_NEWS
                + " WHERE " + NEWS_ID + " = '" + newsId +"'" , new String[] {});
        boolean exists = (cursor.getCount() > 0);
        return exists;
    }


    public void insertData(List<MainNewsGson> newsElement) {
        final SQLiteDatabase db = this.getWritableDatabase();
        for (MainNewsGson element: newsElement) {

            if (!isNewsExists(db, element.getId())) {
                ContentValues values = getValues(element);
                db.insert(TABLE_NEWS, null, values);
                insertImage(element);
            }
        }
    }

    private void insertImage(final MainNewsGson newsElement){
        try {
            if (newsElement.getImageUrl()!=null) {
                final SQLiteDatabase db = this.getWritableDatabase();

                ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
                loadBitmap(newsElement.getImageUrl()).compress(Bitmap.CompressFormat.JPEG, 100, bmpStream);

                ContentValues values = getValues(newsElement);
                values.put(IMAGE, bmpStream.toByteArray());

                //just update existing row. becouse image may be not loaded
                String selection = NEWS_ID + "= ?";
                String[] selectionArgs = {String.valueOf(newsElement.getId())};
                db.update(TABLE_NEWS, values, selection, selectionArgs);
            }
        } catch (Exception e) {
        Log.e("NewsCacheHelper", "insertImage: "+e.getLocalizedMessage());
        }
    }

    private ContentValues getValues(MainNewsGson newsElement) {
        ContentValues values = new ContentValues();
        values.put(NEWS_ID, newsElement.getId());
        values.put(TITLE, newsElement.getTitle());
        values.put(TEXT, newsElement.getDescription());
        values.put(DATE, newsElement.getPublishedAt());
        return values;
    }


    //WTF ????
    public List<MainNewsGson> selectItemsRange(int numbersToGet) {
        Cursor cursor;
        Log.d("DB", "select items");
        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.rawQuery("SELECT *" +" FROM "+ TABLE_NEWS + " order by " + NEWS_ID+" desc LIMIT " + numbersToGet,null );

        List<MainNewsGson> list = new ArrayList<MainNewsGson>();

        while (cursor.moveToNext()){
            MainNewsGson item = new MainNewsGson();
            item.setTitle(cursor.getString(1));
            byte[] blob= cursor.getBlob(2);
            if (blob!=null){
                item.setImage(BitmapFactory.decodeByteArray(blob, 0, blob.length));

            }
            else {
                Log.e(TAG, "image null");
            }
            item.setDescription(cursor.getString(3));
            item.setPublishedAt(cursor.getString(4));
            list.add(item);
        }
        return list;
    }



    private static Bitmap loadBitmap(String picUrl) {
        Bitmap bmp = null;
        try {
            URL url = new URL(picUrl);
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (Exception ex) {

            Log.e("NewsCacheHelper", "image error: " + ex.getMessage());
        }
        return bmp;
    }

}
