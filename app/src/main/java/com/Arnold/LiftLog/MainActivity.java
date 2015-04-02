package com.Arnold.LiftLog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import java.util.List;

public class MainActivity extends ActionBarActivity {
    public final static String EXTRA_MESSAGE = "com.Arnold.LiftLog.MESSAGE";

    // For tagging messages in Log Cat.
    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {// main function oncreate

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayoutButton button1 = new RelativeLayoutButton(this,R.id.button1);

        button1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewLogActivity.class);
                startActivity(intent);
                //Once pressed on the button1 it triggers NewLogActivity
            }
        });

        button1.setText(R.id.test_button_text2, "New");
        button1.setText(R.id.test_button_text1, "Log");
        int id1 = getResources().getIdentifier("com.Arnold.LiftLog:mipmap/button1", null, null);
        button1.setImageResource(R.id.test_button_image, id1);

        RelativeLayoutButton button2 = new RelativeLayoutButton(this,R.id.button2);
        button2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewHistoryActivity.class);
                startActivity(intent);
                //Once pressed on the button2 it triggers ViewHistoryActivity
            }
        });
        button2.setText(R.id.test_button_text2, "View");
        button2.setText(R.id.test_button_text1, "History");
        int id2 = getResources().getIdentifier("com.Arnold.LiftLog:mipmap/button2", null, null);
        button2.setImageResource(R.id.test_button_image, id2);

        RelativeLayoutButton button3 = new RelativeLayoutButton(this,R.id.button3);
        button3.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditBubbleActivity.class);
                startActivity(intent);
                //Once pressed on the button3 it triggers EditBubbleActivity
            }
        });
        button3.setText(R.id.test_button_text2, "Edit");
        button3.setText(R.id.test_button_text1, "Bubble");
        int id3 = getResources().getIdentifier("com.Arnold.LiftLog:mipmap/button3", null, null);
        button3.setImageResource(R.id.test_button_image, id3);

        //*********************************************************************
        // START: Exercise database to check functionality.
        //*********************************************************************

        //testDatabase(this);

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

    /**
     * Used to test the database functionality. Will return early if one
     * of the tests does not pass. Tests are self-commenting via Log print
     * statements.
     *
     * @param context the context within the onCreate() function in this class.
     */
    private void testDatabase(Context context)
    {
        // Create database handler. This is done whenever we want to interact
        // with the database. It gives access to the database functions in
        // my database API. It can add Bubbles, search for Bubbles, delete
        // Bubbles, etc. It also does all Log interaction, with similar
        // functions.
        DatabaseHandler db = new DatabaseHandler(context);

        // Delete & recreate the database.
        //db.recreateDatabase(context);

        Log.v(TAG, "Starting tests.");
        db.addLog(new WorkoutLog("Abs", "10 reps"));
        db.addLog(new WorkoutLog("Chest", "50 reps"));
        db.addLog(new WorkoutLog("Chest Again", "70 reps"));

        List<WorkoutLog> logs = db.getAllWorkoutLogs();

        for (int i = 0; i < logs.size(); i++)
        {
            Log.v(TAG, logs.get(i).toString());
        }

        // Testing.
        if (true)
            return;

        // Start test.
        Log.v(TAG, "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
                + "@@@@@@@@@@");
        Log.v(TAG, "@@ START: Database Test.");
        Log.v(TAG, "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
                + "@@@@@@@@@@");

        Log.v(TAG, "Deleting all Bubbles in the Bubble table.");
        db.deleteAllBubbles();

        Log.v(TAG, "Inserting: Mike, Paul, Zheng, Lauro.");
        db.addBubble(new Bubble("Mike"));
        db.addBubble(new Bubble("Paul"));
        db.addBubble(new Bubble("Zheng"));
        db.addBubble(new Bubble("Lauro"));

        Log.v(TAG, "Checking there are four records in Bubble table.");
        if (db.getAllBubbles().size() == 4)
        {
            Log.v(TAG, "Passed");
        }
        else
        {
            Log.v(TAG, "**************** FAILED.");
            return;
        }

        // Print out all Bubbles in Bubble table.
        List<Bubble> bubbles = db.getAllBubbles();

        Log.v(TAG, "Bubbles in table printed below. Should be Mike, Paul, Zheng, Lauro.");
        for (Bubble bubble : bubbles)
        {
            String log = "ID: " + bubble.getBubbleID()
                    + ", Content: " + bubble.getBubbleContent();

            // Write this Bubble to the Log.
            Log.v(TAG, log);
        }

        Log.v(TAG, "Deleting: Paul, Lauro. Should be able to delete both.");
        boolean del1 = db.deleteBubble("Paul");
        boolean del2 = db.deleteBubble("Lauro");

        if (del1 & del2)
        {
            Log.v(TAG, "Passed");
        }
        else
        {
            Log.v(TAG, "**************** FAILED");
            return;
        }

        Log.v(TAG, "Trying to delete Paul again. Should not be able to " +
                "delete--this record no longer exists.");
        if (!db.deleteBubble("Paul"))
        {
            Log.v(TAG, "Passed");
        }
        else
        {
            Log.v(TAG, "**************** FAILED");
            return;
        }

        // Print out all Bubbles in Bubble table.
        bubbles = db.getAllBubbles();
        Log.v(TAG, "Bubbles in table printed below. Should be Mike, Zheng.");
        for (Bubble bubble : bubbles)
        {
            String log = "ID: " + bubble.getBubbleID()
                    + ", Content: " + bubble.getBubbleContent();

            // Write this Bubble to the Log.
            Log.v(TAG, log);
        }

        Log.v(TAG, "Making sure there are only two records in Bubble table.");
        if (db.getAllBubbles().size() == 2)
        {
            Log.v(TAG, "Passed");
        }
        else
        {
            Log.v(TAG, "**************** FAILED");
            return;
        }

        Log.v(TAG, "Try to lookup Mike. Should work.");
        if (db.getBubble("Mike").getBubbleContent().equals("Mike"))
        {
            Log.v(TAG, "Passed");
        }
        else
        {
            Log.v(TAG, "**************** FAILED");
            return;
        }

        Log.v(TAG, "Try to lookup Zheng. Should work.");
        if (db.getBubble("Zheng").getBubbleContent().equals("Zheng"))
        {
            Log.v(TAG, "Passed");
        }
        else
        {
            Log.v(TAG, "**************** FAILED");
            return;
        }

        Log.v(TAG, "Try to lookup Paul. Shouldn't work.");
        if (db.getBubble("Paul") ==  null)
        {
            Log.v(TAG, "Passed");
        }
        else
        {
            Log.v(TAG, "**************** FAILED");
            return;
        }

        Log.v(TAG, "Try to lookup Lauro. Shouldn't work.");
        if (db.getBubble("Lauro") == null)
        {
            Log.v(TAG, "Passed");
        }
        else
        {
            Log.v(TAG, "**************** FAILED");
            return;
        }

        Log.v(TAG, "Trying to insert Mike. Shouldn't work, no duplicates allowed.");
        if (!db.addBubble(new Bubble("Mike")))
        {
            Log.v(TAG, "Passed");
        }
        else
        {
            Log.v(TAG, "**************** FAILED");
            return;
        }

        Log.v(TAG, "Trying to insert Zheng. Shouldn't work, no duplicates allowed.");
        if (!db.addBubble(new Bubble("Zheng")))
        {
            Log.v(TAG, "Passed");
        }
        else
        {
            Log.v(TAG, "**************** FAILED");
            return;
        }

        Log.v(TAG, "Add three new records: Ke$ha, Wiz Khalifa, Demi Lovato.");
        db.addBubble(new Bubble("Ke$ha"));
        db.addBubble(new Bubble("Wiz Khalifa"));
        db.addBubble(new Bubble("Demi Lovato"));

        // Print out all Bubbles in Bubble table.
        bubbles = db.getAllBubbles();
        Log.v(TAG, "Bubbles in table printed below. Should be Mike, Zheng, Ke$ha, " +
                "Wiz Khalifa, Demi Lovato.");
        for (Bubble bubble : bubbles)
        {
            String log = "ID: " + bubble.getBubbleID()
                    + ", Content: " + bubble.getBubbleContent();

            // Write this Bubble to the Log.
            Log.v(TAG, log);
        }

        Log.v(TAG, "Making sure there are five records in Bubble table.");
        if (db.getAllBubbles().size() == 5)
        {
            Log.v(TAG, "Passed");
        }
        else
        {
            Log.v(TAG, "**************** FAILED");
            return;
        }


        // End test! If we get here, all tests have passed. If any tests
        // fail, they return right away.
        Log.v(TAG, "********************************************************"
                + "**********");
        Log.v(TAG, "** END: Database Test. All tests passed!");
        Log.v(TAG, "********************************************************"
                + "**********");
    }
}
