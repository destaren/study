package ru.isu.drevin.hw5;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created with IntelliJ IDEA.
 * User: Destaren
 * Date: 03.05.13
 * Time: 18:58
 * To change this template use File | Settings | File Templates.
 */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper (Context context) {
        super(context, "RSSDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE rssChannel (" +
                " id INTEGER PRIMARY KEY," +
                " url TEXT," +
                " name TEXT);");
        db.execSQL("CREATE TABLE rssNews (" +
                " channel_id INTEGER," +
                " key TEXT," +
                " title TEXT);");

        ContentValues cv = new ContentValues();
        cv.put("url", "http://www.ntv.ru/exp/newsrss_top.jsp");
        cv.put("name", "НТВ.РУ");
        db.insert("rssChannel", null, cv);

        cv = new ContentValues();
        cv.put("url", "http://www.vesti.ru/vesti.rss");
        cv.put("name", "Вести.Ру");
        db.insert("rssChannel", null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
