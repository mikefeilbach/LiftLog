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

    private TextView logTitleInput;
    private TextView logBodyInput;
    private WorkoutLog oldLog;              //the old log clicked on from View History, will be null if newLog is true
    private String stringOldLogID;          //the old log's ID in string form, will be null if newlog is true
    private int oldLogID;                   //the old log's ID in int form, will be -1 if newlog is true

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history_detailed);


        /*Create new objects for the inputs*/
        this.logTitleInput = (TextView) findViewById(R.id.log_title);
        this.logBodyInput = (TextView) findViewById(R.id.log_body);


        Intent intent = getIntent();

        //attempts to get the intents extra message if sent from view history
        stringOldLogID = intent.getStringExtra("logID");

        //if no extra message was sent, then this is a new log
        if(stringOldLogID == null) {
           //some type of error message
        }
        else {

            //get get ID of log
            oldLogID = Integer.parseInt(stringOldLogID);

            //oldLog = database.getWorkoutLog(oldLogID);
            //this.logTitleInput.setText(oldLog.getLogTitle());
            //this.logBodyInput.setText(oldLog.getLogBody());

            //******************for testing purposes***********************************************
            this.logTitleInput.setText("Title: " + "Test Abs");
            this.logBodyInput.setText("Test something here");
            //*************************************************************************************
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_history_detailed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.edit_log:
                //this.saveLog();
                Toast.makeText(this, "Yeah, Log Saved!", Toast.LENGTH_SHORT).show();
               // editLog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void editLog() {
//        TextView oldBody = (TextView) findViewById(R.id.old_log_body);
//        TextView oldTitle = (TextView) findViewById(R.id.old_log_title);
//        EditText newBody = (EditText) findViewById(R.id.new_log_body);
//        EditText newTitle = (EditText) findViewById(R.id.new_log_title);
//
//        oldBody.setVisibility(View.GONE);
//        oldTitle.setVisibility(View.GONE);
//
//        newBody.setVisibility(View.VISIBLE);
//        newTitle.setVisibility(View.VISIBLE);
//        newBody.setText("Test - Body");
//        newTitle.setText("Test - Title");





    }
}

