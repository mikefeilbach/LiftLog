package com.zzheng.helloworld;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
    public final static String EXTRA_MESSAGE = "com.zzheng.helloworld.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {// main function oncreate
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayoutButton button1 = new RelativeLayoutButton(this,R.id.button1);

        button1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "RelativeLayoutButton clicked", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, NewLogActivity.class);

                startActivity(intent);
            }
        });

        button1.setText(R.id.test_button_text2, "New");
        button1.setText(R.id.test_button_text1, "Log");
        int id1 = getResources().getIdentifier("com.zzheng.helloworld:mipmap/button1", null, null);
        button1.setImageResource(R.id.test_button_image, id1);

        RelativeLayoutButton button2 = new RelativeLayoutButton(this,R.id.button2);
        button2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "RelativeLayoutButton clicked", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, ViewHistoryActivity.class);
                startActivity(intent);
            }
        });
        button2.setText(R.id.test_button_text2, "View");
        button2.setText(R.id.test_button_text1, "History");
        int id2 = getResources().getIdentifier("com.zzheng.helloworld:mipmap/button2", null, null);
        button2.setImageResource(R.id.test_button_image, id2);

        RelativeLayoutButton button3 = new RelativeLayoutButton(this,R.id.button3);
        button3.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "RelativeLayoutButton clicked", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, EditBubbleActivity.class);
                startActivity(intent);
            }
        });
        button3.setText(R.id.test_button_text2, "Edit");
        button3.setText(R.id.test_button_text1, "Bubble");
        int id3 = getResources().getIdentifier("com.zzheng.helloworld:mipmap/button3", null, null);
        button3.setImageResource(R.id.test_button_image, id3);

    }

     /*@Override
   public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

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
}
