package com.Arnold.LiftLog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends ActionBarActivity {
    public final static String EXTRA_MESSAGE = "com.Arnold.LiftLog.MESSAGE";

    // For tagging messages in Log Cat.
    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {// main function oncreate

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //*********************************************************************
        // BUTTON 1: NEW WORKOUT LOG
        //*********************************************************************
        RelativeLayoutButton button1 = new RelativeLayoutButton(this,R.id.button1);

        button1.setText(R.id.test_button_text2, "New");
        button1.setText(R.id.test_button_text1, "Workout Log");
        button1.setButtonColor(0x88FF9900);     //first 8bits is transparency, second to forth is RGB Respectively
        int id1 = getResources().getIdentifier("com.Arnold.LiftLog:mipmap/button1", null, null);
        button1.setImageResource(R.id.test_button_image, id1);

        button1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewLogActivity.class);
                startActivity(intent);
                //Once pressed on the button1 it triggers NewLogActivity
            }
        });

        //*********************************************************************
        // BUTTON 2: VIEW HISTORY
        //*********************************************************************
        RelativeLayoutButton button2 = new RelativeLayoutButton(this,R.id.button2);

        button2.setText(R.id.test_button_text2, "View");
        button2.setText(R.id.test_button_text1, "History");
        button2.setButtonColor(0xFFFF99FF);     //first 8bits is transparency, second to forth is RGB Respectively
        int id2 = getResources().getIdentifier("com.Arnold.LiftLog:mipmap/button2", null, null);
        button2.setImageResource(R.id.test_button_image, id2);

        button2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewHistoryActivity.class);
                startActivity(intent);
                //Once pressed on the button2 it triggers ViewHistoryActivity
            }
        });

        //*********************************************************************
        // BUTTON 3: EDIT BUBBLES
        //*********************************************************************
        RelativeLayoutButton button3 = new RelativeLayoutButton(this,R.id.button3);

        button3.setText(R.id.test_button_text2, "Edit");
        button3.setText(R.id.test_button_text1, "Bubbles");
        button3.setButtonColor(0xFF00FF99);     //first 8bits is transparency, second to forth is RGB Respectively
        int id3 = getResources().getIdentifier("com.Arnold.LiftLog:mipmap/button3", null, null);
        button3.setImageResource(R.id.test_button_image, id3);

        button3.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditBubbleActivity.class);
                startActivity(intent);
                //Once pressed on the button3 it triggers EditBubbleActivity
            }
        });

        //*********************************************************************
        // START: Exercise database to check functionality.
        //*********************************************************************

        DatabaseTester dbTest = new DatabaseTester(this);
        dbTest.testDatabase();

        //*********************************************************************
        // DONE: Exercise database to check functionality.
        //*********************************************************************
    }

     /*@Override
   public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

    /*@Override
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
    }*/
}
