package com.Arnold.LiftLog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EditBubbleActivity extends ActionBarActivity implements Comparable{

    private List<Bubble> bubbles = new ArrayList<>();

    private EditText bubbleContentInput;

    final DatabaseHandler db = new DatabaseHandler(this);

    private final int max_bubble_length = 20;

    private boolean editMode = false;

    private int bubbleIdEdit;

    private Spinner bubble_types;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bubble);

        // Set up text input handlers
        this.bubbleContentInput = (EditText) findViewById(R.id.save_bubble_content);
        this.bubbleContentInput.setText("");

        //Updates the list of bubbles
        this.updateBubbles();

        // Initialize the 'Save Bubble' button
        final Button save_button = (Button) findViewById(R.id.save_bubble_button);

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBubble();
            }
        });

        bubble_types = (Spinner) findViewById(R.id.type_spinner);
    }

    public void updateBubbles() {

        // Initialize the Layout
        LinearLayout layout = (LinearLayout) findViewById(R.id.View_Bubbles);

        layout.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);

        layout.removeAllViews();

        this.bubbles = db.getAllBubbles();

        //Collections.sort(bubbles);

        //loop that creates buttons based on the number of logs stored in the database
        //these buttons will be scrollable because of the xml file. Clicking on a button will
        //bring you to another activity in which you can see the contents of the log

        // The double while loop was part of an earlier attempt with the TableLayout,
        // disregard the unnecessary inner loop

        //sets button parameters
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);

        // Scrollview for showing exercise bubbles
        ScrollView exercise_scroll = new ScrollView(this);
        LinearLayout exercise_bubs = new LinearLayout(this);
        exercise_bubs.setOrientation(LinearLayout.VERTICAL);
        exercise_bubs.setVerticalScrollBarEnabled(true);
        exercise_scroll.addView(exercise_bubs, lp);
        //layout.addView(exercise_bubs, lp);

        // Scrollview for showing repetition bubbles
        ScrollView reps_sets_scroll = new ScrollView(this);
        LinearLayout reps_sets_bubs = new LinearLayout(this);
        reps_sets_bubs.setOrientation(LinearLayout.VERTICAL);
        reps_sets_bubs.setVerticalScrollBarEnabled(true);
        reps_sets_scroll.addView(reps_sets_bubs, lp);
        //layout.addView(reps_sets_bubs, lp);

        // Scrollview for showing duration bubbles
        ScrollView duration_scroll = new ScrollView(this);
        LinearLayout duration_bubs = new LinearLayout(this);
        duration_bubs.setOrientation(LinearLayout.VERTICAL);
        duration_bubs.setVerticalScrollBarEnabled(true);
        duration_scroll.addView(duration_bubs, lp);
        //layout.addView(duration_bubs, lp);

        //int tester = 0;

        for (final Bubble curr_bubble : bubbles) {

            //new button being created for bubble
            final Button myButton = new Button(this);

            //set the text of the log to be the logs title (will add date later)
            myButton.setText(curr_bubble.getBubbleContent());

            myButton.setClickable(true);
                myButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        bubbleContentInput.setText("");
                        editMode = true;
                        editBubbles(curr_bubble);
                    }
                });

            myButton.setPadding(2, 2, 2, 2);

            myButton.setMaxWidth(350);

            if (curr_bubble.getBubbleType() == 0) {
                myButton.getBackground().setColorFilter(0xFF00DD00, PorterDuff.Mode.MULTIPLY);
                exercise_bubs.addView(myButton);
            } else if (curr_bubble.getBubbleType() == 1) {
                myButton.getBackground().setColorFilter(0xFFFE5000, PorterDuff.Mode.MULTIPLY);
                reps_sets_bubs.addView(myButton);
            } else {
                myButton.getBackground().setColorFilter(0xFF00DDDD, PorterDuff.Mode.MULTIPLY);
                duration_bubs.addView(myButton);


            }
            myButton.setOnLongClickListener(new View.OnLongClickListener(){
                    public boolean onLongClick(View v){AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditBubbleActivity.this);
                        alertDialogBuilder.setTitle("Delete Bubble");
                        alertDialogBuilder.setMessage("Do you wish to delete this bubble?");

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
                                //delete bubble from database
                                bubbleContentInput.setText("");
                                db.deleteBubble(curr_bubble.getBubbleContent());
                                updateBubbles();

                                Toast.makeText(EditBubbleActivity.this, "Bubble Deleted", Toast.LENGTH_SHORT).show();
                            }
                        });
                        alertDialogBuilder.create().show();
                        return true;
                }
                              });
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
        layout.addView(duration_scroll, scroll);
    }

    public void saveBubble(){

        String bubble_type_string = String.valueOf(bubble_types.getSelectedItem());

        int bubble_type = -1;

        if (bubble_type_string.equals("Exercise")) {
            bubble_type = 0;
        } else if (bubble_type_string.equals("Reps/Sets")) {
            bubble_type = 1;
        } else if (bubble_type_string.equals("Weight/Rest")) {
            bubble_type = 2;
        } else {
            this.bubbleContentInput.setError("Invalid Bubble type.");
            return;
        }

        boolean success;
        //Add code to save the bubble
        String bubbleContent = this.bubbleContentInput.getText().toString();

        if (bubbleContent.equals("") || bubbleContent.length() > max_bubble_length) {
            this.bubbleContentInput.setError("Bubble content cannot be empty or exceed 20 characters.");
            this.bubbleContentInput.setText("");

            return;
        }

        if (!editMode) {
            success = db.addBubble(new Bubble(bubbleContent, bubble_type));
        }
        else {
            success = db.updateBubble(bubbleIdEdit,new Bubble(bubbleContent,bubble_type));
            editMode=false;
        }

        if (success) {
            Toast.makeText(this, "Yeah, Bubble Saved!", Toast.LENGTH_SHORT).show();
            this.bubbleContentInput.setText("");
            this.updateBubbles();
        }
        else {
            Toast.makeText(this, "Error Saving Bubble. Please, try again.", Toast.LENGTH_SHORT).show();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_bubble, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        //If there is text in the input bubble, then shows confirm dialog
        String bubbleContent = this.bubbleContentInput.getText().toString();
        if (!bubbleContent.equals("")){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditBubbleActivity.this);
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
                    //Return to Main Screen
                    Intent intent = new Intent(EditBubbleActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            alertDialogBuilder.create().show();
        }
        else{
            //If there's no text, just returns to Home Screen
            this.finish();
        }
    }

    public void editBubbles(Bubble bubble) {
        if (editMode){
            bubbleIdEdit = bubble.getBubbleID();
            bubbleContentInput.setText(bubble.getBubbleContent());
            bubble_types.setSelection(bubble.getBubbleType());
        }
    }

    @Override
    public int compareTo(Object another) {
        return 0;
    }
}
