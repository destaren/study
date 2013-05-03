package ru.isu.drevin.hw4;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity
{
    private Button webButton, imgButton, textButton;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Init();
    }

    private void Init() {
        webButton = (Button) findViewById(R.id.web);
        imgButton = (Button) findViewById(R.id.img);
        textButton = (Button) findViewById(R.id.text);

        webButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i = new Intent("ru.isu.drevin.Web");
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.isu.ru"));
                startActivity(i);
            }
        });

        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent("ru.isu.drevin.Img");
                startActivity(i);
            }
        });

        textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent("ru.isu.drevin.Text");
                startActivity(i);
            }
        });
    }
}
