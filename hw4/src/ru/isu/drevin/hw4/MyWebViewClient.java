package ru.isu.drevin.hw4;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created with IntelliJ IDEA.
 * User: Destaren
 * Date: 03.05.13
 * Time: 14:20
 * To change this template use File | Settings | File Templates.
 */
public class MyWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}
