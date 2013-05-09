package ru.isu.drevin.hw8;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MyActivity extends Activity {

    String[] chNames = {"Кураж-Бамбей", "MixSibnet Films"};
    String[] chUrls = {"http://xn--80aacbuczbw9a6a.xn--p1ai/news.xml", "http://mix.sibnet.ru/movie/rss/"};

    ListView listView;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        listView = (ListView) findViewById(R.id.chListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, chNames);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent("ru.isu.drevin.hw8.Channel");
                intent.putExtra("url", chUrls[i]);
                intent.putExtra("channelID", ""+i);
                startActivity(intent);
            }
        });
    }
}
