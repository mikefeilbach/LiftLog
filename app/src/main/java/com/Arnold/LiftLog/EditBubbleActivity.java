package com.Arnold.LiftLog;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class EditBubbleActivity extends ActionBarActivity {

    private List<Bubble> bubbles = new ArrayList<Bubble>();

    private EditText bubbleContentInput;

    final DatabaseHandler db = new DatabaseHandler(this);

    private final int max_bubble_length = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bubble);

//        GridView gridview = (GridView) findViewById(R.id.gridview);
//        gridview.setAdapter(new ImageAdapter(this));
//
//        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View v,
//                                    int position, long id) {
//                Toast.makeText(EditBubbleActivity.this, "" + position,
//                        Toast.LENGTH_SHORT).show();
//            }
//        });

        // Get all da bubbles
        //bubbles = db.getAllBubbles();

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
                /*Intent intent = new Intent(EditBubbleActivity.this, MainActivity.class);
                startActivity(intent);*/
            }
        });
    }

    public void updateBubbles(){
        // For testing
        /*Bubble bubble1 = new Bubble("Bubble 1");
        Bubble bubble2 = new Bubble("Bubble 2");
        Bubble bubble3 = new Bubble("Bubble 3");
        Bubble bubble4 = new Bubble("Bubble 4");
        Bubble bubble5 = new Bubble("Bubble 5");
        bubbles.add(bubble1);
        bubbles.add(bubble2);
        bubbles.add(bubble3);
        bubbles.add(bubble4);
        bubbles.add(bubble5);*/
        GridLayout gridView = (GridLayout) findViewById(R.id.View_Bubbles);
        gridView.removeAllViews();
        this.bubbles = db.getAllBubbles();
        //loop that creates buttons based on the number of logs stored in the database
        //these buttons will be scrollable because of the xml file. Clicking on a button will
        //bring you to another activity in which you can see the contents of the log
        for(int i = 0;i<bubbles.size();i++) {

            //new button being created for log
            Button myButton = new Button(this);

            //set the text of the log to be the logs title (will add date later)
            myButton.setText(bubbles.get(i).getBubbleContent());

            myButton.setClickable(true);

            // Allows for the bubbles to be clicked and viewed
            myButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(EditBubbleActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });

            /* Used for the GridView */
            //ArrayAdapter<String> array_adapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1);

            // Set up the gridview for laying out the bubbles


            /* All GridView changes are commented out for the time being as I figure out
               how to properly use it. For now, I'll use the GridLayout which behaves similarly
               to LinearLayout.
             */

            //gridView.setAdapter(array_adapter);

//            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                public void onItemClick(AdapterView<?> parent, View v,
//                                        int position, long id) {
//                    Toast.makeText(EditBubbleActivity.this, "" + position,
//                            Toast.LENGTH_SHORT).show();
//                }
//            });

            //adds button to the layout
            //array_adapter.add(bubble1.getBubbleContent());
            //array_adapter.insert(bubbles.get(i).getBubbleContent(), i);

            gridView.addView(myButton);
        }
     }

    public void saveBubble(){

        //Add code to save the bubble
        String bubbleContent = this.bubbleContentInput.getText().toString();

        //Calendar date = Calendar.getInstance();

        if (bubbleContent.equals("") || bubbleContent.length() > max_bubble_length) {
            this.bubbleContentInput.setError("Bubble content cannot be empty or exceed 20 characters.");
            this.bubbleContentInput.setText("");

            /*Intent intent = new Intent(EditBubbleActivity.this, EditBubbleActivity.class);
            startActivity(intent);*/
            return;
        }

        boolean success = db.addBubble(new Bubble(bubbleContent));

        if (success) {
            Toast.makeText(this, "Yeah, Bubble Saved!", Toast.LENGTH_SHORT).show();
            //this.finish();
            this.bubbleContentInput.setText("");
            this.updateBubbles();
        }
        else {
            Toast.makeText(this, "Error Saving Bubble. Please, try again.", Toast.LENGTH_SHORT).show();
        }
    }

//    public class ImageAdapter extends BaseAdapter {
//        private Context mContext;
//
//        public ImageAdapter(Context c) {
//            mContext = c;
//        }
//
//        public int getCount() {
//            return mThumbIds.length;
//        }
//
//        public Object getItem(int position) {
//            return null;
//        }
//
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        // create a new ImageView for each item referenced by the Adapter
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ImageView imageView;
//            if (convertView == null) {
//                // if it's not recycled, initialize some attributes
//                imageView = new ImageView(mContext);
//                imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
//                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                imageView.setPadding(8, 8, 8, 8);
//            } else {
//                imageView = (ImageView) convertView;
//            }
//
//            imageView.setImageResource(mThumbIds[position]);
//            return imageView;
//        }
//
//        // references to our images
//        private Integer[] mThumbIds = {
//                R.drawable.sample_2, R.drawable.sample_3,
//                R.drawable.sample_4, R.drawable.sample_5,
//                R.drawable.sample_6, R.drawable.sample_7,
//                R.drawable.sample_0, R.drawable.sample_1,
//                R.drawable.sample_2, R.drawable.sample_3,
//                R.drawable.sample_4, R.drawable.sample_5,
//                R.drawable.sample_6, R.drawable.sample_7,
//                R.drawable.sample_0, R.drawable.sample_1,
//                R.drawable.sample_2, R.drawable.sample_3,
//                R.drawable.sample_4, R.drawable.sample_5,
//                R.drawable.sample_6, R.drawable.sample_7
//        };
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_bubble, menu);
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
}
