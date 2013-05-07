package ru.isu.drevin.hw5;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {

    final int DB_VERSION = 2;
    DBHelper dbHelper;
    final String rssTable = "rssChannel";

    ListView listView;

    final String ATTRIBUTE_NAME_ID = "id";
    final String ATTRIBUTE_NAME_URL = "url";
    final String ATTRIBUTE_NAME_NAME = "name";

    final int CONTEXT_MENU_EDIT = 1;
    final int CONTEXT_MENU_DELETE = 2;

    final int OPTIONS_MENU_ADD = 1;

    int currentItemID = -1;

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

    @Override
    public void onResume() {
        InitListView();
        super.onResume();
    }

    public void Init() {
        listView = (ListView) findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //To change body of implemented methods use File | Settings | File Templates.
                HashMap<String, String> item = (HashMap<String, String>) items.get(i);
                String url = item.get(ATTRIBUTE_NAME_URL);
                String channelID = item.get(ATTRIBUTE_NAME_ID);
                Intent intent = new Intent("ru.isu.drevin.hw5.Channel");
                intent.putExtra("url", url);
                intent.putExtra("channelID", channelID);
                startActivity(intent);
                //Toast.makeText(getApplicationContext(), item.get(ATTRIBUTE_NAME_URL), Toast.LENGTH_SHORT).show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentItemID = i;
                //Toast.makeText(getApplicationContext(), ""+view.getId()+"\n"+android.R.id.text1, Toast.LENGTH_SHORT).show();
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }
        });

        registerForContextMenu(listView);
    }

    public void InitListView() {
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
                map.put(ATTRIBUTE_NAME_ID, ""+id);
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
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        switch (view.getId()) {
            case R.id.listView:
                menu.add(0, CONTEXT_MENU_EDIT, 0, "Изменить");
                menu.add(0, CONTEXT_MENU_DELETE, 0, "Удалить");
                break;
        }
        super.onCreateContextMenu(menu, view, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        HashMap<String, String> channel = (HashMap<String, String>) items.get(currentItemID);
        String channelId = channel.get(ATTRIBUTE_NAME_ID);

        switch (item.getItemId()) {
            case CONTEXT_MENU_EDIT:
                //Toast.makeText(getApplicationContext(), "Editing "+currentItemID, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent("ru.isu.drevin.hw5.NewChannel");
                intent.putExtra("channelID", channelId);
                startActivity(intent);
                break;
            case CONTEXT_MENU_DELETE:
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                int countDeleted = db.delete("rssChannel", "id = ?", new String[]{channelId});
                //Toast.makeText(getApplicationContext(), ""+countDeleted, Toast.LENGTH_SHORT).show();
                InitListView();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, OPTIONS_MENU_ADD, 0, "Добавить канал");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case OPTIONS_MENU_ADD:
                //Toast.makeText(getApplicationContext(), "Adding", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent("ru.isu.drevin.hw5.NewChannel");
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
