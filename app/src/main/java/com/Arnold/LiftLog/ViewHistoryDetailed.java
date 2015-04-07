package com.Arnold.LiftLog;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class ViewHistoryDetailed extends ActionBarActivity {



    private WorkoutLog oldLog;              //the old log clicked on from View History, will be null if newLog is true
    private String stringOldLogID;          //the old log's ID in string form, will be null if newlog is true
    private int oldLogID;                   //the old log's ID in int form, will be -1 if newlog is true
    private boolean editLog;                //if true, log is being edited by user, not just viewed
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history_detailed);

        //starts false, meaning user is just viewing previous log
        editLog = false;

        //gets the database
        db = new DatabaseHandler(this);

        /*Create new objects for the inputs*/
        TextView logTitleInput = (TextView) findViewById(R.id.old_log_title);
        TextView logBodyInput = (TextView) findViewById(R.id.old_log_body);

        //gets the intent of this action
        Intent intent = getIntent();

        //attempts to get the intents extra message if sent from view history
        stringOldLogID = intent.getStringExtra("logID");

        //if no extra message was sent, then this is a new log
        if(stringOldLogID == null) {
            stringOldLogID = null;
            oldLogID = -1;

            //prints error message that log could not be retrieved
            Toast.makeText(this, "Error retrieving log information", Toast.LENGTH_SHORT).show();

            //sends user back to view history
            Intent intent1 = new Intent(this,ViewHistoryActivity.class);
            startActivity(intent1);
        }
        else {

            //get get ID of log
            oldLogID = Integer.parseInt(stringOldLogID);

            oldLog = db.getWorkoutLog(oldLogID);
            logTitleInput.setText(oldLog.getLogTitle());
            logBodyInput.setText(oldLog.getLogBody());

            //******************for testing purposes***********************************************
            //logTitleInput.setText("Title: " + "Test Abs");
            //logBodyInput.setText("Test something here");
            //*************************************************************************************
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_history_detailed, menu);

        //if the log is being edited, removes the pencil symbol and replaces it with the save symbol
        if(editLog) {

            //gets the edit log button
            MenuItem edit = menu.findItem(R.id.VHD_edit_log);

            //gets the save log button
            MenuItem save = menu.findItem(R.id.VHD_save_log);

            //turns the edit log button off and save log button on
            edit.setVisible(false);
            save.setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            //if the edit log button was pressed, do this
            case R.id.VHD_edit_log:
                //test purposes only
                //Toast.makeText(this, "Edit log", Toast.LENGTH_SHORT).show();

                //makes the text editable
                editLog();

                return true;

            //if the save log button was pressed, do this
            case R.id.VHD_save_log:
                //test purposes only
                Toast.makeText(this, "Need to finish saving log", Toast.LENGTH_SHORT).show();


                //db.updateWorkoutLog(oldLogID,oldLog);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This function changes the state of this screen so that the text in the title and body can be
     * edited
     */
    public void editLog() {

        //changes state of action to be editable
        editLog = true;

        //gets the old (read only) title and body views
        TextView oldBody = (TextView) findViewById(R.id.old_log_body);
        TextView oldTitle = (TextView) findViewById(R.id.old_log_title);

        //gets the new (read/write) title and body views
        EditText newBody = (EditText) findViewById(R.id.new_log_body);
        EditText newTitle = (EditText) findViewById(R.id.new_log_title);

        //erases the old (read only) views
        oldBody.setVisibility(View.GONE);
        oldTitle.setVisibility(View.GONE);

        //turns the new (R/W) editable views on
        newBody.setVisibility(View.VISIBLE);
        newTitle.setVisibility(View.VISIBLE);

        //sets the text of the body and title to be the logs body and title
        newBody.setText(oldLog.getLogBody());
        newTitle.setText(oldLog.getLogTitle());

        //this will recall onCreateOptionsMenu() so that the save button can be turned on
        invalidateOptionsMenu();
    }
}

