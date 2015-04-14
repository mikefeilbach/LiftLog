package com.Arnold.LiftLog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.Console;
import java.sql.SQLOutput;
import java.util.Calendar;


public class NewLogActivity extends ActionBarActivity {

    private EditText logTitleInput;
    private EditText logBodyInput;
    DatabaseHandler db = new DatabaseHandler(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_log);

        /*Create new objects for the inputs*/
        this.logTitleInput = (EditText) findViewById(R.id.log_title);
        this.logBodyInput = (EditText) findViewById(R.id.log_body);

        /*Set empty string for both inputs*/
        this.logTitleInput.setText("");
        this.logBodyInput.setText("");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*Add button save log to the menu bar*/
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_new_log, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.save_log:
                this.saveLog();
                return true;
            case android.R.id.home:
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void saveLog(){
        //Add code to save the log
        String logTitle = this.logTitleInput.getText().toString();
        String logBody = this.logBodyInput.getText().toString();

        /*Verify if log title is empty or bigger than 40 char*/
        if (logTitle.equals("") || logTitle.length()>40){
            this.logTitleInput.setError("Log Title content cannot be empty or exceed 40 characters.");
            this.logTitleInput.setText("");
            return;
        }

        /*Add log to database, if success displays success message and close,
          otherwise show error and stays in the screen*/
        boolean success = this.db.addWorkoutLog(new WorkoutLog(logTitle, logBody));
        if (success) {
            Toast.makeText(this, "Yeah, Log Saved!", Toast.LENGTH_SHORT).show();
            this.finish();
        }
        else {
            Toast.makeText(this, "Error Saving Log. Please, try again.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        //If there's text in the one of the fields, ask if user wants to lost unsaved data
        String logTitle = this.logTitleInput.getText().toString();
        String logBody = this.logBodyInput.getText().toString();
        if(!logTitle.equals("") || !logBody.equals("")) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NewLogActivity.this);
            alertDialogBuilder.setTitle("Return");
            alertDialogBuilder.setMessage("Do you want to return and lost your unsaved data?");

            alertDialogBuilder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //do nothing
                }
            });

            alertDialogBuilder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Return to home screen
                    Intent intent = new Intent(NewLogActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            alertDialogBuilder.create().show();
        }
        else{
            //If there's no text, just finish and return to home screen
            this.finish();
        }
    }
}
