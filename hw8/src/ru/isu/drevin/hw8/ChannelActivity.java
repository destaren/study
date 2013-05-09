package ru.isu.drevin.hw8;

import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentProducer;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Destaren
 * Date: 03.05.13
 * Time: 19:35
 * To change this template use File | Settings | File Templates.
 */
public class ChannelActivity extends Activity {

    static final String NEWS_ID = "_id";
    static final String NEWS_CHANNEL_ID = "channel_id";
    static final String NEWS_LINK = "link";
    static final String NEWS_TITLE = "title";
    static final String NEWS_DATE = "pub_date";
    static final String NEWS_NEW = "new";

    static final Uri NEWS_URI = Uri.parse("content://ru.isu.drevin.hw8.RSSNEWS/m_news");

    ListView listView;
    GetPostsTask postsTask;
    String channelID;
    SimpleCursorAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channel);

        String url = getIntent().getStringExtra("url");
        channelID = getIntent().getStringExtra("channelID");

        listView = (ListView) findViewById(R.id.postList);

        postsTask = (GetPostsTask) getLastNonConfigurationInstance();
        if(postsTask == null) {
            postsTask = new GetPostsTask();
            postsTask.setContentResolver(getContentResolver());
            postsTask.setChannelID(channelID);
            postsTask.execute(url);
        } /*else if (postsTask.getStatus() == AsyncTask.Status.FINISHED) {
            setListViewListener();
        }    */
        postsTask.link(this);

        setListViewListener();
    }

    public Object onRetainNonConfigurationInstance() {
        postsTask.unLink();
        return postsTask;
    }

    public void setListViewListener() {
        Cursor cursor = getContentResolver().query(NEWS_URI, null, "channel_id = ? AND new = 1", new String[]{channelID}, null);
        startManagingCursor(cursor);

        String[] from = {NEWS_TITLE};
        int[] to = {android.R.id.text1};
        adapter = new SimpleCursorAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, cursor, from, to);
        listView = (ListView) findViewById(R.id.postList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor c = (Cursor) adapter.getItem(i);
                int linkColInd = c.getColumnIndex(NEWS_LINK);
                int idColInd = c.getColumnIndex("_id");
                String url = c.getString(linkColInd);
                int newsID = c.getInt(idColInd);

                ContentValues cv = new ContentValues();
                cv.put(NEWS_NEW, 0);
                stopManagingCursor(adapter.getCursor());
                getContentResolver().update(NEWS_URI, cv, "_id = ?", new String[]{""+newsID} );
                //Toast.makeText(getApplicationContext(), c.getString(linkColInd), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent("ru.isu.drevin.hw8.News", Uri.parse(url));
                startActivity(intent);
                //Toast.makeText(getApplicationContext(), "_"+l+"_", Toast.LENGTH_SHORT).show();
            }
        });
    }

    static class GetPostsTask extends AsyncTask<String, Void, Void> {

        ChannelActivity activity = null;
        String[] newsTitles, newsLinks, pubDates;
        boolean error = false;
        int errorCode = 0;

        ContentResolver cr;
        String channelID;

        public void setContentResolver(ContentResolver _cr) {
            cr = _cr;
        }
        public void setChannelID(String id) {
            channelID = id;
        }

        public void link(ChannelActivity act) {
            activity = act;
            if(error) {
                error = false;
                Toast.makeText(activity.getApplicationContext(), "Connection error", Toast.LENGTH_SHORT).show();
            }
        }

        public void unLink() {
            activity = null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... urls) {
            try {
                if(!downloadRSS(urls[0])) {
                    error = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                error = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if(error) {
                if(activity != null) {
                    error = false;
                    Toast.makeText(activity.getApplicationContext(), "Connection error", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            Date lastNewsDate = getLastNewsDate();
            long lNDi = getLastNewsDate_i();
            for(int i = 0; i < newsTitles.length; i++) {
                Date date = new Date(pubDates[i]);
                if(date.after(lastNewsDate) || lNDi < date.getTime()) {
                    ContentValues cv = new ContentValues();
                    cv.put(NEWS_CHANNEL_ID, Integer.parseInt(channelID));
                    cv.put(NEWS_LINK, newsLinks[i]);
                    cv.put(NEWS_TITLE, newsTitles[i]);
                    cv.put(NEWS_DATE, date.getTime() / 1000);
                    cv.put(NEWS_NEW, 1);
                    cr.insert(NEWS_URI, cv);
                    //long inserted = activity.dbHelper.getWritableDatabase().insert("rssNews",null,cv);
                }
            }
        }

        private InputStream OpenHttpGETConnection(String urlString) throws IOException {
            /*
            InputStream in;
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpResponse httpResponse = httpClient.execute(new HttpGet(urlString));
                in = httpResponse.getEntity().getContent();
            } catch (Exception e) {
                throw new IOException("Error connecting");
            }
            return in;
            //*/
            ///*
            InputStream in = null;
            int response = -1;

            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();

            if(!(conn instanceof HttpURLConnection))
                throw new IOException("Not an HTTP connection");

            try {
                HttpURLConnection httpConn = (HttpURLConnection) conn;
                httpConn.setAllowUserInteraction(false);
                httpConn.setInstanceFollowRedirects(true);
                httpConn.setRequestMethod("GET");
                httpConn.connect();

                response = httpConn.getResponseCode();
                if(response == HttpURLConnection.HTTP_OK) {
                    in = httpConn.getInputStream();
                }
            } catch (Exception ex) {
                throw new IOException("Error connecting");
            }

            return  in;
            //*/
        }

        private boolean downloadRSS(String urlString) {
            InputStream in = null;
            try {
                in = OpenHttpGETConnection(urlString);
                if(in == null) {
                    if(activity != null)
                        Toast.makeText(activity.getApplicationContext(), "Wrong connection params", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } catch (Exception e) {
                if(activity != null)
                    Toast.makeText(activity.getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                return false;
            }

            Document document = null;
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder;

            try {
                documentBuilder = documentBuilderFactory.newDocumentBuilder();
                document = documentBuilder.parse(in);
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            document.getDocumentElement().normalize();

            NodeList rssItems = document.getElementsByTagName("item");
            newsLinks = new String[rssItems.getLength()];
            newsTitles = new String[rssItems.getLength()];
            pubDates = new String[rssItems.getLength()];

            for(int i = 0; i < rssItems.getLength(); i++) {
                Node rssItem = rssItems.item(i);
                Element news = (Element) rssItem;
                String title = news.getElementsByTagName("title").item(0).getFirstChild().getNodeValue();
                String link = news.getElementsByTagName("link").item(0).getFirstChild().getNodeValue();
                String pubDate = news.getElementsByTagName("pubDate").item(0).getFirstChild().getNodeValue();

                newsTitles[i] = title;
                newsLinks[i] = link;
                pubDates[i] = pubDate;
                //String pubDate = news.getElementsByTagName("pubDate").item(0).getNodeValue();
            }

            return true;
        }

        public Date getLastNewsDate() {
            Date lastDate = new Date(0);

            try {
                Cursor c = cr.query(NEWS_URI, new String[]{NEWS_DATE}, null, null, NEWS_DATE + " DESC");
                if(c.getCount() > 0 && c.moveToFirst()) {
                    int dateColPos = c.getColumnIndex(NEWS_DATE);
                    long date = c.getInt(dateColPos);
                    lastDate = new Date(date*1000);
                }
            } catch (Exception e) {
                return new Date(0);
            }

            return lastDate;
        }

        public long getLastNewsDate_i() {
            long lastDate = 0;

            try {
                Cursor c = cr.query(NEWS_URI, new String[]{NEWS_DATE}, null, null, NEWS_DATE + " DESC");
                if(c.getCount() > 0 && c.moveToFirst()) {
                    int dateColPos = c.getColumnIndex(NEWS_DATE);
                    long date = c.getInt(dateColPos);
                    lastDate = date*1000L;
                }
            } catch (Exception e) {
                return 0L;
            }

            return lastDate;
        }
    }
}
