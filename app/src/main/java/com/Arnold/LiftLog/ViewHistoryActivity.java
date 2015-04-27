package com.Arnold.LiftLog;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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


    private ArrayList<WorkoutLog> logHistory;   //list holding the pre-sorted logs stored in database
    DatabaseHandler db;                         //database where all logs are stored
    private ArrayList<ArrayList<WorkoutLog>> logsByMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_history);

        //initializes logHistory
        logHistory = new ArrayList<WorkoutLog>();

        // Create database handler. This allows easy interaction with the app's database.
        db = new DatabaseHandler(this);

        // Get sorted (by date) ArrayList of WorkoutLogs.
        logHistory = (ArrayList<WorkoutLog>) db.getAllWorkoutLogs();

        //************************logs for testing only*********************************************
//        for(int i= 0; i < 36; i++) {
//            String title = "Title: " + i;
//            if ( i % 12 > 9) {
//                WorkoutLog newLog = new WorkoutLog(title, "Test Body");
//                String date = (i % 12) + "/01/15";
//                newLog.setLogDateString(date);
//
//                WorkoutLog newLog1 = new WorkoutLog(title, "Test Body");
//                String date1 = (i % 12) + "/05/15";
//                newLog1.setLogDateString(date1);
//
//                WorkoutLog newLog2 = new WorkoutLog(title, "Test Body");
//                String date2 = (i % 12) + "/13/15";
//                newLog2.setLogDateString(date2);
//
//                WorkoutLog newLog3 = new WorkoutLog(title, "Test Body");
//                String date3 = (i % 12) + "/23/15";
//                newLog3.setLogDateString(date3);
//
//                WorkoutLog newLog4 = new WorkoutLog(title, "Test Body");
//                String date4 = (i % 12) + "/30/15";
//                newLog4.setLogDateString(date4);
//
//                logHistory.add(newLog);
//                logHistory.add(newLog1);
//                logHistory.add(newLog2);
//                logHistory.add(newLog3);
//                logHistory.add(newLog4);
//            }
//            else {
//                WorkoutLog newLog = new WorkoutLog(title, "Test Body");
//                String date = "0" + (i % 12) + "/01/15";
//                newLog.setLogDateString(date);
//
//                WorkoutLog newLog1 = new WorkoutLog(title, "Test Body");
//                String date1 = "0" + (i % 12) + "/05/15";
//                newLog1.setLogDateString(date1);
//
//                WorkoutLog newLog2 = new WorkoutLog(title, "Test Body");
//                String date2 = "0" + (i % 12) + "/13/15";
//                newLog2.setLogDateString(date2);
//
//                WorkoutLog newLog3 = new WorkoutLog(title, "Test Body");
//                String date3 = "0" + (i % 12) + "/23/15";
//                newLog3.setLogDateString(date3);
//
//                WorkoutLog newLog4 = new WorkoutLog(title, "Test Body");
//                String date4 = "0" + (i % 12) + "/30/15";
//                newLog4.setLogDateString(date4);
//
//                logHistory.add(newLog);
//                logHistory.add(newLog1);
//                logHistory.add(newLog2);
//                logHistory.add(newLog3);
//                logHistory.add(newLog4);
//            }
//        }
        //**********************************end testing data****************************************



        if(logHistory.size() > 0) {

            logsByMonth = new ArrayList<>();
            int previousMonth = Character.getNumericValue(logHistory.get(0).getLogDateString().charAt(1));
            int currentIndex = 0;

            //loops through and sorts all of the logs by month
            for (int i = 0; i < logHistory.size(); i++) {

                //checks to see if the month of the current log is equal to the month of the previous log
                if (Character.getNumericValue(logHistory.get(i).getLogDateString().charAt(1)) == previousMonth) {

                    //checks to see if the list of logs for the current month exists
                    if (logsByMonth.size() <= currentIndex) {

                        //if the list of logs for the current month does not exist, initialize it
                        logsByMonth.add(new ArrayList<WorkoutLog>());
                    }

                    //add the current log to the list of logs for the specific month
                    logsByMonth.get(currentIndex).add(logHistory.get(i));
                }

                //if the month of the current log is not equal, then this is a new month
                else {

                    //updates the month of the previous log
                    previousMonth = Character.getNumericValue(logHistory.get(i).getLogDateString().charAt(1));

                    //initializes another list for the new month
                    logsByMonth.add(new ArrayList<WorkoutLog>());

                    //updates the current index - indicating the list for the next month
                    currentIndex++;

                    //adds the current logs to the list of logs for the new specific month
                    logsByMonth.get(currentIndex).add(logHistory.get(i));
                }
            }

            //loop that will create buttons for the current months logs, as well as buttons for all
            //previous months that logs exist. You can click on a month, and its logs will then be
            //displayed. J is the index in logsByMonth that details which month is currently being worked
            //on.
            for (int j = 0; j < logsByMonth.size(); j++) {

                //if the month is the most current one
                if (j == 0) {

                    //creates buttons for all of the logs in the current months list
                    for (int i = 0; i < logsByMonth.get(j).size(); i++) {

                        //new button being created for log
                        final Button myButton = new Button(this);
                        myButton.setTextColor(0xFFFFFFFF);
                        //myButton.getBackground().setColorFilter(0xFF0099FF, PorterDuff.Mode.MULTIPLY);
                        myButton.setBackgroundResource(R.drawable.button_viewhistory);
                        myButton.setText(logsByMonth.get(j).get(i).getLogTitle() + "\n" +
                                logsByMonth.get(j).get(i).getLogDateString() + "\n\n\n");

                        final int test = i;         //current log in month
                        final int test2 = j;        //current month in list of months

                        //allows user to click on it
                        myButton.setClickable(true);

                        //when the button is click on by the user, starts the new activity with the detailed
                        //log information.
                        myButton.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                Intent intent = new Intent(ViewHistoryActivity.this, ViewHistoryDetailed.class);
                                intent.putExtra("logID", String.valueOf(logsByMonth.get(test2).get(test).getLogID()));
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
                                        db.deleteWorkoutLog(logsByMonth.get(test2).get(test).getLogTitle(), logsByMonth.get(test2).get(test).getLogBody());
                                        myButton.setVisibility(View.GONE);

                                        Toast.makeText(ViewHistoryActivity.this, "Deleted log", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                alertDialogBuilder.create().show();

                                return true;
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
                //if the month j is not the current month, just makes 1 bubble for the entire month
                else {
                    //new button being created for the specific month
                    final Button myButton = new Button(this);
                    myButton.setTextColor(0xFFFFFFFF);
                    myButton.getBackground().setColorFilter(0xFF0099FF, PorterDuff.Mode.MULTIPLY);

                    //the month and year of the log button
                    String monthYear[] = logsByMonth.get(j).get(0).getLogDateString().split("/");

                    //sets the button title to be its month and year
                    myButton.setText("Logs from \n" + monthYear[0] + "/" + monthYear[2] + "\n\n\n");


                    final int test2 = j;        //specific month that button is being made for

                    //allows user to click on it
                    myButton.setClickable(true);

                    //When the button is clicked on by the user, it creates buttons for all of the logs
                    //created in that month and gets rid of the general month button
                    myButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {

                            //get rid of the button for the month
                            myButton.setVisibility(View.GONE);

                            //loops through and creates buttons for all the logs during the specific month
                            for (int i = logsByMonth.get(test2).size() - 1; i >= 0; i--) {

                                //new button being created for log
                                final Button myNewButton = new Button(ViewHistoryActivity.this);
                                myButton.setTextColor(0xFFFFFFFF);
                                myButton.getBackground().setColorFilter(0xFF0099FF, PorterDuff.Mode.MULTIPLY);
                                myNewButton.setText(logsByMonth.get(test2).get(i).getLogTitle() + "\n" +
                                        logsByMonth.get(test2).get(i).getLogDateString() + "\n\n\n");

                                final int test = i;         //current log that button is being made for in specific month

                                //allows user to click on it
                                myNewButton.setClickable(true);

                                //when the button is click on by the user, starts the new activity with the detailed
                                //log information.
                                myNewButton.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {
                                        Intent intent = new Intent(ViewHistoryActivity.this, ViewHistoryDetailed.class);
                                        intent.putExtra("logID", String.valueOf(logsByMonth.get(test2).get(test).getLogID()));
                                        startActivity(intent);
                                    }
                                });

                                //when the button is held by the user, asks them if they wish to delete the log, if no,
                                //just returns them to the list, if yes, then deletes the log from the list.
                                myNewButton.setOnLongClickListener(new View.OnLongClickListener() {
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
                                                db.deleteWorkoutLog(logsByMonth.get(test2).get(test).getLogTitle(), logsByMonth.get(test2).get(test).getLogBody());
                                                myNewButton.setVisibility(View.GONE);

                                                Toast.makeText(ViewHistoryActivity.this, "Deleted log", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        alertDialogBuilder.create().show();

                                        return true;
                                    }
                                });

                                //gets the layout of this class, which has been nested inside a scrollable interface
                                LinearLayout layout = (LinearLayout) findViewById(R.id.View_History);

                                //sets button parameters
                                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                //adds button to the top of the layout (the 0 is the index in the linear layout)
                                layout.addView(myNewButton, 0, lp);
                            }

                            //scrolls page to the top of the layout to view latest buttons
                            ScrollView scrollView = (ScrollView) findViewById(R.id.VH_scrollview);
                            scrollView.fullScroll(ScrollView.FOCUS_UP);
                        }
                    });

                    //when the button is held by the user, asks them if they wish to delete the log, if no,
                    //just returns them to the list, if yes, then deletes the log from the list.
                    myButton.setOnLongClickListener(new View.OnLongClickListener() {
                        public boolean onLongClick(View v) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ViewHistoryActivity.this);
                            alertDialogBuilder.setTitle("Delete Log");
                            alertDialogBuilder.setMessage("Are you sure? This will delete all logs saved during the month!");

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

                                    for (int k = 0; k < logsByMonth.get(test2).size(); k++) {
                                        //delete log from database
                                        db.deleteWorkoutLog(logsByMonth.get(test2).get(k).getLogTitle(), logsByMonth.get(test2).get(k).getLogBody());

                                    }

                                    myButton.setVisibility(View.GONE);

                                    Toast.makeText(ViewHistoryActivity.this, "Deleted all logs", Toast.LENGTH_SHORT).show();
                                }
                            });
                            alertDialogBuilder.create().show();

                            return true;
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

    @Override
    public void onBackPressed() {
        //Return to home screen
        Intent intent = new Intent(ViewHistoryActivity.this, MainActivity.class);
        startActivity(intent);
    }

}
