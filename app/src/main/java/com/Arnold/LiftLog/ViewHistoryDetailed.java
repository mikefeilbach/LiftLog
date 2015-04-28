package com.Arnold.LiftLog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;


public class ViewHistoryDetailed extends ActionBarActivity {

    private EditText logTitleInput;         // Makin' does changes for gainz
    private EditText logBodyInput;

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

        bubbleSetUp();

        //gets the old (read only) title and body views
        TextView oldBody = (TextView) findViewById(R.id.old_log_body);
        TextView oldTitle = (TextView) findViewById(R.id.old_log_title);
        View separator = (View) findViewById(R.id.separator);
        View v = this.findViewById(R.id.view_history_detailed);
        v.setBackgroundColor(0xFF000000);

        //gets the new (read/write) title and body views
        EditText newBody = (EditText) findViewById(R.id.new_log_body);
        EditText newTitle = (EditText) findViewById(R.id.new_log_title);

        //Set the EditText variables for adding new text
        logTitleInput = newTitle;
        logBodyInput = newBody;

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

        //gets the old (read only) title and body views and the new (R/W)body and title
        EditText newLogBody = (EditText) findViewById(R.id.new_log_body);
        EditText newLogTitle = (EditText) findViewById(R.id.new_log_title);

        //verifies if the title is not empty or bigger than 40 chars
        if (newLogTitle.getText().toString().equals("")
                || newLogTitle.getText().toString().length()>max_title_length){
            newLogTitle.setError("Log Title content cannot be empty or exceed 40 characters.");
            //newLogTitle.setText(oldLog.getLogTitle());
            return;
        }

        //no longer editing log, just viewing your changes
        editLog = false;

        //saves the old workout log in the database
        oldLog.setLogBody(newLogBody.getText().toString());
        oldLog.setLogTitle(newLogTitle.getText().toString());
        db.updateWorkoutLog(oldLogID,oldLog);

        //prints "log saved" and sends user back to the main View History screen
        Intent intent = new Intent(ViewHistoryDetailed.this,ViewHistoryDetailed.class);
        intent.putExtra("logID", String.valueOf(oldLogID));
        Toast.makeText(this, "Log saved", Toast.LENGTH_SHORT).show();
        startActivity(intent);

    }

    public void bubbleSetUp() {
        // Initialize the Layout
        LinearLayout layout = (LinearLayout) findViewById(R.id.View_Bubs_Detailed);

        layout.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);

        // Idk i this is necessary
        layout.removeAllViews();

        // Make the bubble view visible
        layout.setVisibility(View.VISIBLE);

        List<Bubble> bubbles = db.getAllBubbles();

        //sets button parameters
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);

        // Scrollview for showing exercise bubbles
        ScrollView exercise_scroll = new ScrollView(this);
        LinearLayout exercise_bubs = new LinearLayout(this);
        exercise_bubs.setOrientation(LinearLayout.VERTICAL);
        exercise_bubs.setVerticalScrollBarEnabled(true);
        exercise_scroll.addView(exercise_bubs, lp);

        // Scrollview for showing repetition bubbles
        ScrollView reps_sets_scroll = new ScrollView(this);
        LinearLayout reps_sets_bubs = new LinearLayout(this);
        reps_sets_bubs.setOrientation(LinearLayout.VERTICAL);
        reps_sets_bubs.setVerticalScrollBarEnabled(true);
        reps_sets_scroll.addView(reps_sets_bubs, lp);

        // Scrollview for showing duration bubbles
        ScrollView weight_rest_scroll = new ScrollView(this);
        LinearLayout weight_rest_bubs = new LinearLayout(this);
        weight_rest_bubs.setOrientation(LinearLayout.VERTICAL);
        weight_rest_bubs.setVerticalScrollBarEnabled(true);
        weight_rest_scroll.addView(weight_rest_bubs, lp);

        for (final Bubble curr_bubble : bubbles) {

            //new button being created for bubble
            final Button myButton = new Button(this);
            myButton.setTextColor(0xFFFFFFFF);

            myButton.setText(curr_bubble.getBubbleContent());

            myButton.setClickable(true);

            // Set the appropriate padding so the text doesn't extend
            // beyond the boundary of the bubble button
            myButton.setPadding(5,2,5,2);

            myButton.setWidth(325);

            // Any Click automatically deletes the first bubble, this will be fixed with Lauro's stuff
            myButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    insertText(myButton.getText(), curr_bubble.getBubbleType());
                }
            });

            // Adding color to the bubs and placing them in the right column
            if (curr_bubble.getBubbleType() == Bubble.BUBBLE_TYPE_EXERCISE) {
                //myButton.getBackground().setColorFilter(0xFF00DD00, PorterDuff.Mode.MULTIPLY);
                myButton.setBackgroundResource(R.drawable.button_bubble1);
                exercise_bubs.addView(myButton);
            } else if (curr_bubble.getBubbleType() == Bubble.BUBBLE_TYPE_REPS ||
                    curr_bubble.getBubbleType() == Bubble.BUBBLE_TYPE_SETS ) {

                if (curr_bubble.getBubbleType() == Bubble.BUBBLE_TYPE_REPS) {
                    //myButton.getBackground().setColorFilter(0xFFFE5000, PorterDuff.Mode.MULTIPLY);
                    myButton.setBackgroundResource(R.drawable.button_bubble2);
                } else {
                    //myButton.getBackground().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
                    myButton.setBackgroundResource(R.drawable.button_bubble3);
                }
                reps_sets_bubs.addView(myButton);
            } else {

                if (curr_bubble.getBubbleType() == Bubble.BUBBLE_TYPE_WEIGHT) {
                    //myButton.getBackground().setColorFilter(0xFF00CCEE, PorterDuff.Mode.MULTIPLY);
                    myButton.setBackgroundResource(R.drawable.button_bubble4);
                } else {
                    //myButton.getBackground().setColorFilter(0xFF0000EE, PorterDuff.Mode.MULTIPLY);
                    myButton.setBackgroundResource(R.drawable.button_bubble5);
                }
                weight_rest_bubs.addView(myButton);
            }
        }

        ScrollView.LayoutParams scroll = new ScrollView.LayoutParams(ScrollView.LayoutParams.WRAP_CONTENT, ScrollView.LayoutParams.MATCH_PARENT);

        layout.addView(exercise_scroll, scroll);
        layout.addView(reps_sets_scroll, scroll);
        layout.addView(weight_rest_scroll, scroll);
    }

    public void insertText(CharSequence content, int bubbleType) {
        if (logTitleInput.hasFocus()){
            logTitleInput.setText(content);
        }
        else if (logBodyInput.hasFocus()){
            if (bubbleType!=Bubble.BUBBLE_TYPE_EXERCISE){
                logBodyInput.append("\t\t"+content+"\n");
            }
            else{
                logBodyInput.append(content+"\n");
            }
        }
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
                        Intent intent = new Intent(ViewHistoryDetailed.this, ViewHistoryDetailed.class);
                        intent.putExtra("logID", String.valueOf(oldLogID));
                        startActivity(intent);
                    }
                });
                alertDialogBuilder.create().show();
            }
            else{
                Intent intent = new Intent(ViewHistoryDetailed.this, ViewHistoryDetailed.class);
                intent.putExtra("logID", String.valueOf(oldLogID));
                startActivity(intent);
            }
        }
        else{
            //If it is not in edit mode, just return to the View History
            Intent intent = new Intent(ViewHistoryDetailed.this, ViewHistoryActivity.class);
            startActivity(intent);
        }
    }
}

