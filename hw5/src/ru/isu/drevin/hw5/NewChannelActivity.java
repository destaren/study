package ru.isu.drevin.hw5;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created with IntelliJ IDEA.
 * User: Destaren
 * Date: 05.05.13
 * Time: 1:08
 * To change this template use File | Settings | File Templates.
 */
public class NewChannelActivity extends Activity {

    DBHelper dbHelper;
    Button button;
    EditText etName, etUrl;

    final String ATTRIBUTE_NAME_ID = "id";
    final String ATTRIBUTE_NAME_URL = "url";
    final String ATTRIBUTE_NAME_NAME = "name";

    String channelID = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newchannel);
        dbHelper = new DBHelper(this);

        Init();

        if(getIntent().hasExtra("channelID")) {
            channelID = getIntent().getStringExtra("channelID");
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor c = db.query("rssChannel", null, "id = ?", new String[]{channelID}, null, null, null);
            if(c.moveToFirst()) {
                int idColIndex = c.getColumnIndex(ATTRIBUTE_NAME_ID);
                int urlColIndex = c.getColumnIndex(ATTRIBUTE_NAME_URL);
                int nameColIndex = c.getColumnIndex(ATTRIBUTE_NAME_NAME);

                String url = c.getString(urlColIndex);
                String name = c.getString(nameColIndex);

                etName.setText(name);
                etUrl.setText(url);
            }
        }
    }

    public void Init() {
        button = (Button) findViewById(R.id.butAddChannel);

        etName = (EditText) findViewById(R.id.etName);
        etUrl = (EditText) findViewById(R.id.etUrl);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), ""+etName.getText().toString() + "\n" + etUrl.getText().toString(), Toast.LENGTH_SHORT).show();
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                if(channelID == "") {
                    ContentValues cv = new ContentValues();
                    cv.put("url", etUrl.getText().toString());
                    cv.put("name", etName.getText().toString());
                    db.insert("rssChannel", null, cv);
                } else {
                    ContentValues cv = new ContentValues();
                    cv.put("url", etUrl.getText().toString());
                    cv.put("name", etName.getText().toString());
                    db.update("rssChannel", cv, "id = ?", new String[]{channelID});
                }
                finish();
            }
        });
    }

}
