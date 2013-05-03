package ru.isu.drevin.hw4;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ImgActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.img);

        Bitmap bitmap = downloadImage("http://1megamir.ru/pictures/companies/avatarnone.png");
        ImageView img = (ImageView) findViewById(R.id.imgView);
        if(bitmap != null && img != null)
            img.setImageBitmap(bitmap);
        else
            Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_SHORT).show();
    }

    private InputStream OpenHttpConnection(String urlString) throws IOException {
        InputStream in = null;
        int response = -1;

        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        if(!(conn instanceof  HttpURLConnection))
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
    }

    private Bitmap downloadImage(String urlString) {
        Bitmap bitmap = null;
        InputStream in = null;
        try {
            in = OpenHttpConnection(urlString);
            if(in != null) {
                bitmap = BitmapFactory.decodeStream(in);
                in.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bitmap;
    }
}
