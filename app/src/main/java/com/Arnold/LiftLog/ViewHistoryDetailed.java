package com.Arnold.LiftLog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.security.PrivateKey;


public class ViewHistoryDetailed extends ActionBarActivity {



    private WorkoutLog oldLog;              //the old log clicked on from View History, will be null if newLog is true
    private String stringOldLogID;          //the old log's ID in string form, will be null if newlog is true
    private int oldLogID;                   //the old log's ID in int form, will be -1 if newlog is true
    private boolean editLog;                //if true, log is being edited by user, not just viewed
    private DatabaseHandler db;
    private final int max_title_length = 40;
    private String titleOld;
    private String bodyOld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history_detailed);

        //starts false, meaning user is just viewing previous log
        editLog = false;

                                        //set focus layout for new_log_title(edit text box)
                                        //when receive focus, layout changes to drawable/focus_border_style.xml
                                        //when lost  focus, layout  changes to drawable/lost_focus_style.xml
        TextView tv=(TextView)findViewById(R.id.new_log_title);
        tv.setBackgroundResource(R.drawable.lost_focus_style);
        tv.setOnFocusChangeListener( new View.OnFocusChangeListener(){

            public void onFocusChange( View view, boolean hasfocus){
                if(hasfocus){

                    view.setBackgroundResource( R.drawable.focus_border_style);
                }
                else{
                    view.setBackgroundResource( R.drawable.lost_focus_style);
                }
            }
        });

                                        //set focus layout for new_log_body(edit text box)
                                        //when receive focus, layout changes to drawable/focus_border_style.xml
                                        //when lost  focus, layout  changes to drawable/lost_focus_style.xml
        TextView tv2=(TextView)findViewById(R.id.new_log_body);
        tv2.setBackgroundResource(R.drawable.lost_focus_style);
        tv2.setOnFocusChangeListener( new View.OnFocusChangeListener(){

            public void onFocusChange( View view, boolean hasfocus){
                if(hasfocus){

                    view.setBackgroundResource( R.drawable.focus_border_style);
                }
                else{
                    view.setBackgroundResource( R.drawable.lost_focus_style);
                }
            }
        });

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
                this.editLog();

                return true;

            //if the save log button was pressed, do this
            case R.id.VHD_save_log:
                //updates the log
                this.saveLog();
                return true;

            case android.R.id.home:
                this.onBackPressed();
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
        View separator = (View) findViewById(R.id.separator);

        //gets the new (read/write) title and body views
        EditText newBody = (EditText) findViewById(R.id.new_log_body);
        EditText newTitle = (EditText) findViewById(R.id.new_log_title);

        //erases the old (read only) views
        oldBody.setVisibility(View.GONE);
        oldTitle.setVisibility(View.GONE);
        separator.setVisibility(View.GONE);

        //turns the new (R/W) editable views on
        newBody.setVisibility(View.VISIBLE);
        newTitle.setVisibility(View.VISIBLE);

        //sets the text of the body and title to be the logs body and title
        newBody.setText(oldLog.getLogBody());
        newTitle.setText(oldLog.getLogTitle());

        titleOld = newTitle.getText().toString();
        bodyOld = newBody.getText().toString();

        //this will recall onCreateOptionsMenu() so that the save button can be turned on
        invalidateOptionsMenu();
    }

    /**
     * This function updates in the database the new information for an edited log
     */
    private void saveLog() {
        EditText newLogBody = (EditText) findViewById(R.id.new_log_body);
        EditText newLogTitle = (EditText) findViewById(R.id.new_log_title);

        //verifies if the title is not empty or bigger than 40 chars
        if (newLogTitle.getText().toString().equals("")
                || newLogTitle.getText().toString().length()>max_title_length){
            newLogTitle.setError("Log Title content cannot be empty or exceed 40 characters.");
            newLogTitle.setText(oldLog.getLogTitle());
            return;
        }

        //saves the old workout log in the database
        oldLog.setLogBody(newLogBody.getText().toString());
        oldLog.setLogTitle(newLogTitle.getText().toString());
        db.updateWorkoutLog(oldLogID,oldLog);

        //prints "log saved" and sends user back to the main View History screen
        Intent intent = new Intent(ViewHistoryDetailed.this,ViewHistoryActivity.class);
        Toast.makeText(this, "Log saved", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        //If it is in edit mode, then show the dialog to confirm
        if(this.editLog){
            EditText newLogBody = (EditText) findViewById(R.id.new_log_body);
            EditText newLogTitle = (EditText) findViewById(R.id.new_log_title);

            if (!titleOld.equals(newLogTitle.getText().toString())
                    || !bodyOld.equals(newLogBody.getText().toString()) ) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ViewHistoryDetailed.this);
                alertDialogBuilder.setTitle("Return");
                alertDialogBuilder.setMessage("Do you want to return and lose your unsaved data?");


                alertDialogBuilder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                });

                alertDialogBuilder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Return to View History
                        Intent intent = new Intent(ViewHistoryDetailed.this, ViewHistoryActivity.class);
                        startActivity(intent);
                    }
                });
                alertDialogBuilder.create().show();
            }
            else{
                Intent intent = new Intent(ViewHistoryDetailed.this, ViewHistoryActivity.class);
                startActivity(intent);
            }
        }
        else{
            //If it is not in edit mode, just return to the View History
            this.finish();
        }
    }
}

