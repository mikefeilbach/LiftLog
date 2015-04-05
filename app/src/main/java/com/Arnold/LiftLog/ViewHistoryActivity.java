package com.Arnold.LiftLog;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul Theis.
 * This class is the view history screen, where users will be able to scroll
 * through their previous logs (sorted by date), click on any log and it will transfer them to the
 * logs main details.
 */
public class ViewHistoryActivity extends ActionBarActivity {

    //list holding the pre-sorted logs stored in database
     private ArrayList<WorkoutLog> logHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_history);

        logHistory = new ArrayList<WorkoutLog>();

        // Call database to get all logs


        //*********************************************************************
        // START Mike's comments.
        // Paul, here are the lines of code that will give you a sorted
        // ArrayList<WorkoutLog>.
        //*********************************************************************

        // Create database handler. This allows easy interaction with
        // the app's database.
       // DatabaseHandler db = new DatabaseHandler(this);

        // Get sorted (by date) ArrayList of WorkoutLogs.
        //List<WorkoutLog> logs = db.getAllWorkoutLogs();

        // Now, if you want to get the String date & time description, here
        // is how you would do that, for example, on the first WorkoutLog in the
        // List.
       // String dateOfFirstLog = logs.get(0).getLogDateString();

        //*********************************************************************
        // END Mike's comments.
        //*********************************************************************



        // I'm assuming that the log will already be in order in the arraylist, so I will just be
        // printing straight from it.  That can be adjusted later if possible.

        //****************************This is for testing only***********************************
        WorkoutLog log1 = new WorkoutLog("Abs", "Trial1");
        WorkoutLog log2 = new WorkoutLog("Ab", "Trial2");
        WorkoutLog log3 = new WorkoutLog("Abss", "Trial3");
        WorkoutLog log4 = new WorkoutLog("Abbs", "Trial4");
        WorkoutLog log5 = new WorkoutLog("Aabs", "Trial5");
        logHistory.add(log1);
        logHistory.add(log2);
        logHistory.add(log3);
        logHistory.add(log4);
        logHistory.add(log5);
        //***************************************************************************************

        //loop that creates buttons based on the number of logs stored in the database
        //these buttons will be scrollable because of the xml file. Clicking on a button will
        //bring you to another activity in which you can see the contents of the log
        for(int i = 0;i<5;i++) {

            //new button being created for log
            Button myButton = new Button(this);

            //set the text of the log to be the logs title (will add date later)
            myButton.setText(logHistory.get(i).getLogTitle() + "\n\n\n\n\n\n");
            //myButton.setText(log1.getLogTitle() + "\n\n\n\n\n\n");

            //needed because logHistory was complaining below for intent.putExtra(...)
            final int test = i;

            //allows user to click on it
            myButton.setClickable(true);

            //when the button is click on by the user, starts the new activity with the detailed
            //log information.  Right now, when clicked on the log just returns to the main page
            myButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(ViewHistoryActivity.this, NewLogActivity.class);
                    intent.putExtra("logID",String.valueOf(logHistory.get(test).getLogID()));
                    startActivity(intent);
                }
            });

            //gets the layout of this class, which has been nested inside a scrollable interface
            LinearLayout layout = (LinearLayout) findViewById(R.id.View_History);

            //sets button parameters
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            //adds button to the layout
            layout.addView(myButton, lp);
        }




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_history, menu);
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


}
