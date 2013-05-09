package ru.isu.drevin.hw8;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created with IntelliJ IDEA.
 * User: Destaren
 * Date: 10.05.13
 * Time: 1:28
 * To change this template use File | Settings | File Templates.
 */
public class MyRSSNewsProvider extends ContentProvider {

    // DB
    static final String DB_NAME = "MyRSSDB";
    static final int DB_VERSION = 1;

    // Table
    static final String NEWS_TABLE = "m_news";

    // Field
    static final String NEWS_ID = "_id";
    static final String NEWS_CHANNEL_ID = "channel_id";
    static final String NEWS_LINK = "link";
    static final String NEWS_TITLE = "title";
    static final String NEWS_DATE = "pub_date";
    static final String NEWS_NEW = "new";

    // Create table script
    static final String NEWS_TABLE_CREATE = "CREATE TABLE " + NEWS_TABLE + "(" +
            NEWS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NEWS_CHANNEL_ID + " INTEGER, " +
            NEWS_LINK + " TEXT, " +
            NEWS_TITLE + " TEXT, " +
            NEWS_DATE + " INTEGER," +
            NEWS_NEW + " BOOLEAN" +
            ");";

    // AUTHORITY
    static final String AUTHORITY = "ru.isu.drevin.hw8.RSSNEWS";

    // PATH
    static final String NEWS_PATH = "m_news";

    // public URI
    public static final Uri NEWS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + NEWS_PATH);

    // datatypes
    static  final String NEWS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + NEWS_PATH;

    // single row
    static final String NEWS_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + NEWS_PATH;

    //// UriMatcher
    // common uri
    static final int URI_NEWS = 1;

    // uri with id
    static final int URI_NEWS_ID = 2;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, NEWS_PATH, URI_NEWS);
        uriMatcher.addURI(AUTHORITY, NEWS_PATH + "/#", URI_NEWS_ID);
    }

    DBHelper dbHelper;
    SQLiteDatabase db;

    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (uriMatcher.match(uri)) {
            case URI_NEWS:
                if(TextUtils.isEmpty(sortOrder)) {
                    sortOrder = NEWS_DATE + " DESC";
                }
                break;
            case URI_NEWS_ID:
                String id = uri.getLastPathSegment();
                if(TextUtils.isEmpty(selection)) {
                    selection = NEWS_ID + " = " + id;
                } else  {
                    selection = selection + " AND " + NEWS_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(NEWS_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), NEWS_CONTENT_URI);
        return cursor;
    }

    public Uri insert(Uri uri, ContentValues values) {
        if(uriMatcher.match(uri) != URI_NEWS)
            throw new IllegalArgumentException("Wrong URI: " + uri);

        db = dbHelper.getWritableDatabase();
        long rowID = db.insert(NEWS_TABLE, null, values);
        Uri resultUri = ContentUris.withAppendedId(NEWS_CONTENT_URI, rowID);
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case URI_NEWS:
                break;
            case URI_NEWS_ID:
                String id = uri.getLastPathSegment();
                if(TextUtils.isEmpty(selection)) {
                    selection = NEWS_ID + " = " +id;
                } else {
                    selection = selectionArgs + " AND " + NEWS_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.delete(NEWS_TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case URI_NEWS:
                break;
            case URI_NEWS_ID:
                String id = uri.getLastPathSegment();
                if(TextUtils.isEmpty(selection)) {
                    selection = NEWS_ID + " = " +id;
                } else {
                    selection = selectionArgs + " AND " + NEWS_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.update(NEWS_TABLE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case URI_NEWS:
                return NEWS_CONTENT_TYPE;
            case URI_NEWS_ID:
                return NEWS_CONTENT_ITEM_TYPE;
        }
        return null;
    }

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(NEWS_TABLE_CREATE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){}
    }
}