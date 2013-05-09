package ru.isu.drevin.hw7_108;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

public class MyActivity extends Activity implements ActionBar.TabListener {

    final String LOG_TAG = "myLogs";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ActionBar bar = getActionBar();

        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        Tab tab = bar.newTab();
        tab.setText("tab1");
        tab.setTabListener(this);
        bar.addTab(tab);

        tab = bar.newTab();
        tab.setText("tab2");
        tab.setTabListener(this);
        bar.addTab(tab);

    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
        Log.d(LOG_TAG, "reselected tab: " + tab.getText());
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        Log.d(LOG_TAG, "selected tab: " + tab.getText());
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        Log.d(LOG_TAG, "unselected tab: " + tab.getText());
    }
}