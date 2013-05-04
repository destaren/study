package ru.isu.drevin.hw5;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {

    DBHelper dbHelper;
    final String rssTable = "rssChannel";

    ListView listView;

    final String ATTRIBUTE_NAME_ID = "id";
    final String ATTRIBUTE_NAME_URL = "url";
    final String ATTRIBUTE_NAME_NAME = "name";

    ArrayList<Map<String, String>> items;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        dbHelper = new DBHelper(this);
        Init();
    }

    public void Init() {
        listView = (ListView) findViewById(R.id.listView);

        Cursor c = dbHelper.getWritableDatabase().query(rssTable, null, null, null, null, null, null);
        if(c.moveToFirst()) {
            int idColIndex = c.getColumnIndex(ATTRIBUTE_NAME_ID);
            int urlColIndex = c.getColumnIndex(ATTRIBUTE_NAME_URL);
            int nameColIndex = c.getColumnIndex(ATTRIBUTE_NAME_NAME);
            int rowCount = c.getCount();
            int i = 0;
            items = new ArrayList<Map<String, String>>(rowCount);
            Map<String, String> map;
            do {
                int id = c.getInt(idColIndex);
                String url = c.getString(urlColIndex);
                String name = c.getString(nameColIndex);

                map = new HashMap<String, String>();
                map.put(ATTRIBUTE_NAME_URL, url);
                map.put(ATTRIBUTE_NAME_NAME, name);
                items.add(map);
            } while (c.moveToNext());
        }
        c.close();

        String[] from = {ATTRIBUTE_NAME_NAME};
        int[] to = {android.R.id.text1};

        SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), items, android.R.layout.simple_list_item_1, from, to);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //To change body of implemented methods use File | Settings | File Templates.
                HashMap<String, String> item = (HashMap<String, String>) items.get(i);
                String url = item.get(ATTRIBUTE_NAME_URL);
                Intent intent = new Intent("ru.isu.drevin.hw5.Channel");
                intent.putExtra("url", url);
                startActivity(intent);
                //Toast.makeText(getApplicationContext(), item.get(ATTRIBUTE_NAME_URL), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
