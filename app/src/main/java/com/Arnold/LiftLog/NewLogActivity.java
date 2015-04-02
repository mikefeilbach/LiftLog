package com.Arnold.LiftLog;

import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.Console;
import java.sql.SQLOutput;
import java.util.Calendar;


public class NewLogActivity extends ActionBarActivity {

    private EditText logTitleInput;
    private EditText logBodyInput;

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
        // Inflate the menu; this adds items to the action bar if it is present.
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void saveLog(){
        //Add code to save the log
        String logTitle = this.logTitleInput.getText().toString();
        String logBody = this.logBodyInput.getText().toString();

        //Calendar date = Calendar.getInstance();
        //Log log = new Log(logTitle, logBody, date.getTime);
        //DataSource db = new DataSource(this);

        if (logTitle.equals("") || logTitle.length()>40){
            this.logTitleInput.setError("Log Title cannot be empty and the maximum length is 40 characters.");
            this.logTitleInput.setText("");
            return;
        }

        boolean success = true;//db.save(logTitle, logBody);
        if (success) {
            Toast.makeText(this, "Yeah, Log Saved!", Toast.LENGTH_SHORT).show();
            this.finish();
        }
        else {
            Toast.makeText(this, "Error Saving Log. Please, try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
