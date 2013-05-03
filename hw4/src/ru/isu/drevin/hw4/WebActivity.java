package ru.isu.drevin.hw4;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

public class WebActivity extends Activity
{
    WebView webView;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web);
        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new MyWebViewClient());
        Uri data = getIntent().getData();
        webView.loadUrl(data.toString());
    }
}
