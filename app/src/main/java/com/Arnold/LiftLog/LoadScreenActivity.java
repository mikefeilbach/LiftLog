package com.Arnold.LiftLog;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;


public class LoadScreenActivity extends Activity {

    private final int LOAD_SCREEN_DISPLAY_TIME = 2500; //display load screen for 2.5 seconds.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_screen);

        /* New Handler to start the Main Menu Activity after some seconds. */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(LoadScreenActivity.this, MainActivity.class);
                LoadScreenActivity.this.startActivity(mainIntent);
                LoadScreenActivity.this.finish();
            }
        }, LOAD_SCREEN_DISPLAY_TIME);
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_load_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */
}
