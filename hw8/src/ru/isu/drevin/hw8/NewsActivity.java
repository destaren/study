package ru.isu.drevin.hw8;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * Created with IntelliJ IDEA.
 * User: Destaren
 * Date: 07.05.13
 * Time: 17:18
 * To change this template use File | Settings | File Templates.
 */
public class NewsActivity extends Activity {
    WebView webView;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news);
        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new MyWebViewClient());
        Uri data = getIntent().getData();
        webView.loadUrl(data.toString());
    }
}
