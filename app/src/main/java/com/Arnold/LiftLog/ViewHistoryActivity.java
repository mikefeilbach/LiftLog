package com.Arnold.LiftLog;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

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
    //private GestureDetectorCompat gd;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_history);


        logHistory = new ArrayList<WorkoutLog>();

        // Create database handler. This allows easy interaction with
        // the app's database.
        db = new DatabaseHandler(this);

        // Get sorted (by date) ArrayList of WorkoutLogs.
        logHistory = (ArrayList<WorkoutLog>) db.getAllWorkoutLogs();

        //loop that creates buttons based on the number of logs stored in the database
        //these buttons will be scrollable because of the xml file. Clicking on a button will
        //bring you to another activity in which you can see the contents of the log
        for (int i = 0; i < logHistory.size(); i++) {

            //new button being created for log
            final Button myButton = new Button(this);

            //set the text of the log to be the logs title (will add date later)
            //myButton.setText(logHistory.get(i).getLogTitle() + "\n\n\n\n\n\n");
            //myButton.setText(log1.getLogTitle() + "\n\n\n\n\n\n");
            myButton.setText(logHistory.get(i).getLogTitle() + "\n" + logHistory.get(i).getLogDateString() + "\n\n\n");


            //needed because logHistory was complaining below for intent.putExtra(...)
            final int test = i;

            //allows user to click on it
            myButton.setClickable(true);

            //when the button is click on by the user, starts the new activity with the detailed
            //log information.  Right now, when clicked on the log just returns to the main page
            myButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(ViewHistoryActivity.this, ViewHistoryDetailed.class);
                    intent.putExtra("logID", String.valueOf(logHistory.get(test).getLogID()));
                    startActivity(intent);
                }
            });

            //when the button is held by the user, asks them if they wish to delete the log, if no,
            //just returns them to the list, if yes, then deletes the log from the list.
            myButton.setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View v) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ViewHistoryActivity.this);
                    alertDialogBuilder.setTitle("Delete Log");
                    alertDialogBuilder.setMessage("Do you wish to delete this log?");

                    //don't delete button
                    alertDialogBuilder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do nothing
                        }
                    });

                    //delete button
                    alertDialogBuilder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //delete log from database
                            db.deleteWorkoutLog(logHistory.get(test).getLogTitle(), logHistory.get(test).getLogBody());

                            myButton.setVisibility(View.GONE);

                            Toast.makeText(ViewHistoryActivity.this, "Deleted log", Toast.LENGTH_SHORT).show();
                        }
                    });
                    alertDialogBuilder.create().show();

                    return true;
                }
            });
/*
            myButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    gd = new GestureDetectorCompat(ViewHistoryActivity.this, new MyGestureListener());
                    gd.onTouchEvent(event);
                    return true;
                }

                class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
                    //private static final String DEBUG_TAG = "Gestures";
                    private static final int SWIPE_THRESHOLD = 100;
                    private static final int SWIPE_VELOCITY_THRESHOLD = 100;

                    public boolean onDown(MotionEvent event) {

                        Toast.makeText(ViewHistoryActivity.this, "got to onDown", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    public boolean onFling(MotionEvent e1, MotionEvent e2,
                                           float velocityX, float velocityY) {
                        Toast.makeText(ViewHistoryActivity.this, "Got to here", Toast.LENGTH_SHORT).show();
                        boolean result = false;
                        try {
                            float diffY = e2.getY() - e1.getY();
                            float diffX = e2.getX() - e1.getX();
                            if (Math.abs(diffX) > Math.abs(diffY)) {
                                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                                    if (diffX > 0) {
                                        onSwipeRight();
                                    } else {
                                       // onSwipeLeft();
                                    }
                                }
                                result = true;
                            } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                                if (diffY > 0) {
                                   // onSwipeBottom();
                                } else {
                                   // onSwipeTop();
                                }
                            }
                            result = true;

                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                        return result;
                    }
                    public void onSwipeRight() {
                        Toast.makeText(ViewHistoryActivity.this, "Caught swipe left", Toast.LENGTH_SHORT).show();
                    }

                }
            });
            */

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
