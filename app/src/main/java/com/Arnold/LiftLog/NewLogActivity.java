package com.Arnold.LiftLog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.CountDownTimer;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Console;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class NewLogActivity extends ActionBarActivity {

    /*Class variables*/
    private EditText logTitleInput;
    private EditText logBodyInput;
    DatabaseHandler db = new DatabaseHandler(this);
    private List<Bubble> bubbles = new ArrayList<>();
    private final int max_title_length = 40;
    private CountDownTimer timer;
    private int timerLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_log);
        timerLength = 0;

                                            //set focus layout for log_title(edit text box)
                                            //when receive focus, layout changes to drawable/focus_border_style.xml
                                            //when lost  focus, layout  changes to drawable/lost_focus_style.xml
        TextView tv=(TextView)findViewById(R.id.log_title);
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

                                            //set focus layout for log_body(edit text box)
                                            //when receive focus, layout changes to drawable/focus_border_style.xml
                                            //when lost  focus, layout  changes to drawable/lost_focus_style.xml
        TextView tv2=(TextView)findViewById(R.id.log_body);
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



        /*Create new objects for the inputs*/
        this.logTitleInput = (EditText) findViewById(R.id.log_title);
        this.logBodyInput = (EditText) findViewById(R.id.log_body);

        /*Set empty string for both inputs*/
        this.logTitleInput.setText("");
        this.logBodyInput.setText("");

        bubbleSetUp();
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
            case R.id.save_log: //When presses Save button
                this.saveLog();
                return true;
            case android.R.id.home: //When presses Return button
                this.onBackPressed();
                return true;
            case R.id.set_timer:
               setTimer();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This function resets the timer to the previous time. Can only be accessed if the timer
     * has been previously set.
     * @param view
     */
    public void resetTimer(View view) {
        timer.cancel();
        startTimer();
    }

    /**
     * This function is responsible to set up the rest timer by creating a dialog box that will
     * allow the user to choose the amount of time they want.
     */
    public void setTimer(){

//        final NumberPicker minutes = (NumberPicker) findViewById(R.id.timer_NumberPicker);
//        final NumberPicker seconds = (NumberPicker) findViewById(R.id.timer_NumberPickerSec);
//        minutes.setMinValue(0);
//        minutes.setMaxValue(30);
//        minutes.setWrapSelectorWheel(true);
//        seconds.setMinValue(0);
//        seconds.setMaxValue(59);
//        seconds.setWrapSelectorWheel(true);


        //this will allow the user to pick the timer for the timer in seconds
        final NumberPicker np = new NumberPicker(NewLogActivity.this);
        np.setMaxValue(90);
        np.setMinValue(0);
        np.setWrapSelectorWheel(true);

        //creates the alert dialog box that allows the user to set the timer timer
        final AlertDialog.Builder timerDialog = new AlertDialog.Builder(NewLogActivity.this);
        timerDialog.setTitle("Rest Timer");
        timerDialog.setNegativeButton("Go", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //cancels any lingering timer that might be going on
                if(timer != null) {
                    timer.cancel();
                }

                //gets the pointer to the textview object that will display the timer
                final TextView timerTextView = (TextView) findViewById(R.id.timer_textview);

                final Button resetButton = (Button) findViewById(R.id.timer_button_reset);

                //makes the button visible
                resetButton.setVisibility(View.VISIBLE);
                resetButton.getBackground().setColorFilter(0xffffffff,PorterDuff.Mode.MULTIPLY);

                //makes the display visible
                timerTextView.setVisibility(View.VISIBLE);
                //timerTextView.getBackground().setColorFilter(0xffffffff,PorterDuff.Mode.MULTIPLY);

                //gets the new value of the timer from the number-picker
                timerLength = np.getValue()*1000;
  //              timerLength = minutes.getValue()*10000 + seconds.getValue()*1000;

                //start the timer
                startTimer();


            }
        });
        timerDialog.setPositiveButton("Cancel Timer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //gets the text and button views
                TextView timerView = (TextView) findViewById(R.id.timer_textview);
                Button resetButton = (Button) findViewById(R.id.timer_button_reset);

                //disables the timer text view and button view
                if(timerView.getVisibility() == View.VISIBLE) {
                    timerView.setVisibility(View.GONE);
                    resetButton.setVisibility(View.GONE);
                }

                if(timer != null) {
                    //cancels the timer if it was currently running
                    timer.cancel();
                }

                Toast.makeText(NewLogActivity.this, "Disable Timer", Toast.LENGTH_SHORT).show();
            }
        });

        timerDialog.setView(np);
 //       timerDialog.setView(getLayoutInflater().inflate(R.layout.timer_layout,null));
        timerDialog.create().show();
    }

    /**
     * This function will start the rest timer seen in the new log activity. It will display the
     * remaining time on the screen in hh:mm:ss format. When finished, the timer will display
     * the total time it counted down.
     */
    public void startTimer() {

        //gets the pointer to the textview object that will display the timer
        final TextView timerTextView = (TextView) findViewById(R.id.timer_textview);

        //creates a timer that on each second will update the textview with the remaining time left
        timer = new CountDownTimer((long)timerLength,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                //converts the milisecond's left into a string with format hh:mm:ss
                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % TimeUnit.HOURS.toMinutes(1),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % TimeUnit.MINUTES.toSeconds(1));

                //sets the view to show the current time on the timer
                timerTextView.setText(hms);
            }

            @Override
            public void onFinish() {

                //gets a string format of hh:mm:ss of the final time of the timer
                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(timerLength),
                        TimeUnit.MILLISECONDS.toMinutes(timerLength) % TimeUnit.HOURS.toMinutes(1),
                        TimeUnit.MILLISECONDS.toSeconds(timerLength) % TimeUnit.MINUTES.toSeconds(1));

                //sets the textview to show the final timer of the timer
                timerTextView.setText(hms);

                // Get instance of Vibrator from current Context
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                // Start without a delay
                // Vibrate for 300 milliseconds
                // Sleep for 300 milliseconds
                long[] pattern = {0, 300, 300, 300, 300, 300, 300};

                //makes the phone vibrate for the above pattern only once
                v.vibrate(pattern, -1);
            }
        }.start();

    }

    /**
     * Saves a new workout log into the database
     */
    public void saveLog(){
        /*Gets the text from the inputs*/
        String logTitle = this.logTitleInput.getText().toString();
        String logBody = this.logBodyInput.getText().toString();

        /*Verify if log title is not empty or not bigger than 40 char*/
        if (logTitle.equals("") || logTitle.length()>max_title_length){
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

    public void bubbleSetUp() {
        // Initialize the Layout
        LinearLayout layout = (LinearLayout) findViewById(R.id.View_Bubs_New);

        layout.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);

        layout.removeAllViews();

        this.bubbles = db.getAllBubbles();

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

            //set the text of the log to be the logs title (will add date later)
            myButton.setText(curr_bubble.getBubbleContent());
            myButton.setTextColor(0xFFFFFFFF);

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
//        exercise_scroll.addView(exercise_bubs);
//        reps_sets_scroll.addView(reps_sets_bubs);
//        duration_scroll.addView(duration_bubs);

        ScrollView.LayoutParams scroll = new ScrollView.LayoutParams(ScrollView.LayoutParams.WRAP_CONTENT, ScrollView.LayoutParams.MATCH_PARENT);

//        ScrollView.LayoutParams ex_scroll = new ScrollView.LayoutParams(, ScrollView.LayoutParams.MATCH_PARENT);
//        ScrollView.LayoutParams rep_scroll = new ScrollView.LayoutParams(ScrollView.LayoutParams.WRAP_CONTENT, ScrollView.LayoutParams.MATCH_PARENT);
//        ScrollView.LayoutParams time_scroll = new ScrollView.LayoutParams(ScrollView.LayoutParams.WRAP_CONTENT, ScrollView.LayoutParams.MATCH_PARENT);

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

    /**
     * Creates a dialog and asks for the user if he wants to leave without saving
     * if so, returns to the Home Screen without saving. Else, stays in the new log screen
     */
    @Override
    public void onBackPressed() {
        //If there's text in the one of the fields, ask if user wants to lost unsaved data
        String logTitle = this.logTitleInput.getText().toString();
        String logBody = this.logBodyInput.getText().toString();
        if(!logTitle.equals("") || !logBody.equals("")) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NewLogActivity.this);
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
