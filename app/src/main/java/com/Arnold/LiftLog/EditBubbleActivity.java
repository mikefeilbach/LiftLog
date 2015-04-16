package com.Arnold.LiftLog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class EditBubbleActivity extends ActionBarActivity {

    private List<Bubble> bubbles = new ArrayList<>();

    private EditText bubbleContentInput;

    final DatabaseHandler db = new DatabaseHandler(this);

    private final int max_bubble_length = 20;

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
        Button save_button = (Button) findViewById(R.id.save_bubble_button);

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveBubble();
            }
        });
    }

    public void updateBubbles() {

        // Initialize the Grid
        //TableLayout tableLayout = (TableLayout) findViewById(R.id.View_Bubbles); <--Futile attempt
        //LinearLayout layout = (LinearLayout) findViewById(R.id.View_Bubbles); <-- Somewhat futile
        GridLayout gridLayout = (GridLayout) findViewById(R.id.View_Bubbles);

        //tableLayout.removeAllViews();
        gridLayout.removeAllViews();

        gridLayout.setColumnCount(3); // Default, get rid of MAGIC

        this.bubbles = db.getAllBubbles();

        //List<Button> rowBubbles = new ArrayList<>();
        //List<TableRow> rows = new ArrayList<>();

        int bubbleListIndex = 0;
        //loop that creates buttons based on the number of logs stored in the database
        //these buttons will be scrollable because of the xml file. Clicking on a button will
        //bring you to another activity in which you can see the contents of the log

        // The double while loop was part of an earlier attempt with the TableLayout,
        // disregard the unnecessary inner loop
        while (bubbleListIndex < bubbles.size()) {

            int widthMax = 300;
            int currWidth = 0;

            while (currWidth < widthMax) {

                if (bubbleListIndex == bubbles.size()) break;
                if ((currWidth + 90) > widthMax) break;

                //new button being created for log
                Button myButton = new Button(this);

                //set the text of the log to be the logs title (will add date later)
                myButton.setText(bubbles.get(bubbleListIndex).getBubbleContent());
                myButton.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT));

                //int bubbleWidth = bubbles.get(bubbleListIndex).getBubbleContent().length();

                myButton.setClickable(true);

                myButton.setWidth(200);

                // Any Click automatically deletes the first bubble, this will be fixed with Lauro's stuff
                myButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        db.deleteBubble(bubbles.get(0).getBubbleContent());
                        updateBubbles();
                    }
                });

                gridLayout.addView(myButton, currWidth);
                currWidth++;
                bubbleListIndex++;
            }
        }
    }

    public void saveBubble(){

        //Add code to save the bubble
        String bubbleContent = this.bubbleContentInput.getText().toString();

        if (bubbleContent.equals("") || bubbleContent.length() > max_bubble_length) {
            this.bubbleContentInput.setError("Bubble content cannot be empty or exceed 20 characters.");
            this.bubbleContentInput.setText("");

            return;
        }

        boolean success = db.addBubble(new Bubble(bubbleContent));

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
}
