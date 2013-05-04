package ru.isu.drevin.hw6;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends Activity
{
    final int MENU_ALPHA_ID = 1;
    final int MENU_SCALE_ID = 2;
    final int MENU_TRANSLATE_ID = 3;
    final int MENU_ROTATE_ID = 4;
    final int MENU_COMBO_ID = 5;

    ImageView iv;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        InitComponents();
    }

    private void InitComponents() {
        iv = (ImageView) findViewById(R.id.imageView);
        registerForContextMenu(iv);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
        switch (view.getId()) {
            case R.id.imageView:
                menu.add(0, MENU_ALPHA_ID, 0, "alpha");
                menu.add(0, MENU_SCALE_ID, 0, "scale");
                menu.add(0, MENU_TRANSLATE_ID, 0, "translate");
                menu.add(0, MENU_ROTATE_ID, 0, "rotate");
                menu.add(0, MENU_COMBO_ID, 0, "combo");
                break;
        }
        super.onCreateContextMenu(menu, view, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Animation animation = null;

        switch (item.getItemId()) {
            case MENU_ALPHA_ID:
                animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.myalpha);
                break;
            case MENU_SCALE_ID:
                animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.myscale);
                break;
            case MENU_TRANSLATE_ID:
                animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.mytrans);
                break;
            case MENU_ROTATE_ID:
                animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.myrotate);
                break;
            case MENU_COMBO_ID:
                animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.mycombo);
                break;
        }

        iv.startAnimation(animation);

        return super.onContextItemSelected(item);
    }
}
