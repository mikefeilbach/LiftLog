package com.Arnold.LiftLog;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mike Feilbach on 3/21/2015.
 * Handles the app's database. Offers methods to add/query/delete records
 * from the database's different tables. Acts as an abstraction of the SQLite
 * database.
 */
public class DatabaseHandler extends SQLiteOpenHelper
{
    //*************************************************************************
    // Database attributes.
    //*************************************************************************
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "liftLogDatabase";

    //*************************************************************************
    // Bubble table attributes.
    //*************************************************************************

    // Name of Bubble table within the database.
    private static final String TABLE_BUBBLES = "bubbles";

    // Columns in the Bubble table.
    public static final String COLUMN_BUBBLE_ID = "id";
    public static final String COLUMN_BUBBLE_CONTENT = "bubbleContent";

    //*************************************************************************
    // Log table attributes.
    //*************************************************************************

    // Name of the Log table within the database.
    private static final String TABLE_LOGS = "logs";

    // Columns in the Bubble table.
    public static final String COLUMN_LOG_ID = "id";
    public static final String COLUMN_LOG_TITLE = "logTitle";
    public static final String COLUMN_LOG_BODY = "logBody";
    public static final String COLUMN_LOG_DATE = "logDate";

    /**
     * Default constructor.
     * @param context
     */
    public DatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //*************************************************************************
    // Creating tables in this app's database.
    //*************************************************************************
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // The Bubble table is created when the database is first initialized.
        String CREATE_BUBBLES_TABLE = "CREATE TABLE " +
                TABLE_BUBBLES + "("
                + COLUMN_BUBBLE_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_BUBBLE_CONTENT + " TEXT)";
        db.execSQL(CREATE_BUBBLES_TABLE);

        // The Log table is created when the database is first initialized.
        String CREATE_LOGS_TABLE = "CREATE TABLE " +
                TABLE_LOGS + "("
                + COLUMN_LOG_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_LOG_TITLE + " TEXT,"
                + COLUMN_LOG_BODY + " TEXT,"
                + COLUMN_LOG_DATE + " INT)";
        db.execSQL(CREATE_LOGS_TABLE);
    }


    //*************************************************************************
    // The onUpgrade() method is called when the handler is invoked with a
    // greater database version number from the one previously used. We will
    // simply remove the old database and create a new one.
    //*************************************************************************
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Delete all of database's tables.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUBBLES + TABLE_LOGS);

        // Create fresh tables.
        onCreate(db);
    }


    //*************************************************************************
    // Bubble Table operations.
    //*************************************************************************

    /**
     * Add a single Bubble to the Bubble table. Adding a duplicate Bubble
     * is not allowed and will return false.
     *
     * Note that this will properly assign a unique ID for the Bubble. An
     * ID should not be assigned when a Bubble object is created, and is done
     * at the time the Bubble is added to the Bubble table.
     *
     * @param bubble The Bubble to add to the Bubble table.
     *
     * @return true iff the Bubble was added successfully to the
     *         Bubble table, else false.
     */
    public boolean addBubble(Bubble bubble)
    {
        // Assume Bubble insertion will be successful.
        boolean retVal = true;

        // Check if this Bubble is within the Bubble table already.
        // If so, return false, add nothing to the Bubble table.
        if (getBubble(bubble.getBubbleContent()) != null) {
            return false;
        }

        // Get a reference to our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // Create key-value pairs for the columns in the Bubble table.
        ContentValues values = new ContentValues();

        // Note: do not use the Bubble's ID. The Bubble table will
        // automatically generate them, since it is the primary key.

        // Bubble content field.
        values.put(COLUMN_BUBBLE_CONTENT, bubble.getBubbleContent());

        // Insert new row (Bubble) into the Bubble table. If it fails,
        // we will return false.
        if (db.insert(TABLE_BUBBLES, null, values) == -1)
        {
            retVal = false;
        }

        // Close out database.
        db.close();

        return retVal;
    }


    /**
     * Returns the Bubble whose content field is given. If no such Bubble
     * is found, null is returned.
     *
     * @param bubbleContent The content field of the Bubble being
     *                      searched for.
     *
     * @return null if Bubble was not found, else a Bubble object, filled
     *         with the Bubble's data, which is pulled from the database.
     */
    public Bubble getBubble(String bubbleContent)
    {
        // Create a Bubble to put the matching Bubble's data in.
        // This will hold the return value.
        // Note: We will return the first matching Bubble we find,
        //       however, there should not be duplicates--this is
        //       an invariant.
        Bubble bubble = new Bubble();

        // Get a reference to our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // Query to find the Bubble with the given content.
        String query = "SELECT * FROM " + TABLE_BUBBLES + " WHERE "
                + COLUMN_BUBBLE_CONTENT + " =  \"" + bubbleContent + "\"";

        Cursor cursor = db.rawQuery(query, null);

        // Find the first matching Bubble.
        if (cursor.moveToFirst())
        {
            // ID is in column 0.
            bubble.setBubbleID(Integer.parseInt(cursor.getString(0)));

            // Content is in column 1.
            bubble.setBubbleContent(cursor.getString(1));
        }
        else
        {
            // No Bubble found. Return null.
            bubble = null;
        }

        // Close out the database and cursor.
        db.close();
        cursor.close();

        return bubble;
    }


    /**
     * Returns a List<Bubble> with all of the Bubbles in the Bubble table.
     *
     * @return a List<Bubble> with all of the Bubbles in the Bubble table.
     */
    public List<Bubble> getAllBubbles()
    {
        // Return value.
        List<Bubble> bubbles = new ArrayList<Bubble>();

        // Get a reference to our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // Query to select all rows of Bubble table.
        String query = "Select * FROM " + TABLE_BUBBLES;

        Cursor cursor = db.rawQuery(query, null);

        // Loop through all rows, adding Bubbles to our list as we go.
        if (cursor.moveToFirst())
        {
            do
            {
                Bubble bubble = new Bubble();

                // Bubble ID is in column 0.
                bubble.setBubbleID(Integer.parseInt(cursor.getString(0)));

                // Bubble content is in column 1.
                bubble.setBubbleContent(cursor.getString(1));

                // Add this Bubble to the list.
                bubbles.add(bubble);
            }
            while (cursor.moveToNext());
        }

        // Close out database and cursor.
        db.close();
        cursor.close();

        return bubbles;
    }


    /**
     * Deletes the Bubble, specified by content, within the Bubble table.
     *
     * @param bubbleContent the Bubble to be deleted from the
     *                      Bubble table.
     *
     * @return true iff this Bubble was deleted, else false.
     */
    public boolean deleteBubble(String bubbleContent)
    {
        // Assume we won't find this Bubble.
        boolean result = false;

        // The query within the Bubble Table.
        String query = "Select * FROM " + TABLE_BUBBLES + " WHERE "
                + COLUMN_BUBBLE_CONTENT + " =  \"" + bubbleContent + "\"";

        // Get a reference of our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // For scanning the Bubble table.
        Cursor cursor = db.rawQuery(query, null);

        // Find the first instance of this Bubble.
        // Note: duplicates Bubbles are now allowed, so there should only
        //       ever be one of each Bubble in the Bubble table.
        if (cursor.moveToFirst())
        {
            // Delete the Bubble whose ID matche the first found Bubble.
            int numDeleted = db.delete(TABLE_BUBBLES, COLUMN_BUBBLE_ID + " = ?",
                    new String[] { String.valueOf(Integer.parseInt(cursor.getString(0))) });

            // We found a Bubble to delete, and it was deleted.
            if (numDeleted > 0)
            {
                result = true;
            }
        }

        // Close database and cursor.
        db.close();
        cursor.close();

        return result;
    }


    /**
     * Deletes all Bubbles in the Bubble Table.
     *
     * @return true iff all Bubbles are deleted from the Bubble table.
     */
    public boolean deleteAllBubbles()
    {
        // Get a reference of our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // Delete all records from Bubble table.
        db.execSQL("DELETE FROM " + TABLE_BUBBLES);

        return true;
    }


    //*************************************************************************
    // Log Table operations.
    //*************************************************************************

}
