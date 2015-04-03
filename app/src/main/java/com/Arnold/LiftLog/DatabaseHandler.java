package com.Arnold.LiftLog;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;

/**
 * Created by Mike Feilbach on 3/21/2015.
 * Handles the app's database. Offers methods to add/query/delete records
 * from the database's different tables. Acts as an abstraction of the SQLite
 * database.
 */
public class DatabaseHandler extends SQLiteOpenHelper
{
    // For tagging messages in Log Cat.
    private static final String TAG = "MyActivity";

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
    public static final String COLUMN_BUBBLE_ID = "bubbleId";
    public static final String COLUMN_BUBBLE_CONTENT = "bubbleContent";

    //*************************************************************************
    // WorkoutLog table attributes.
    //*************************************************************************

    // Name of the WorkoutLog table within the database.
    private static final String TABLE_WORKOUT_LOGS = "workoutLogs";

    // Columns in the Bubble table.
    public static final String COLUMN_LOG_ID = "logId";
    public static final String COLUMN_LOG_TITLE = "logTitle";
    public static final String COLUMN_LOG_BODY = "logBody";
    public static final String COLUMN_LOG_DATE_SEC = "logDateSeconds";

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
        Log.v(TAG, "$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$"
                + "$$$$$$$$$$");
        Log.v(TAG, "$$ onCreate called: creating database.");
        Log.v(TAG, "$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$"
                + "$$$$$$$$$$");

        // The Bubble table.
        String CREATE_BUBBLES_TABLE = "CREATE TABLE " +
                TABLE_BUBBLES + "("
                + COLUMN_BUBBLE_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_BUBBLE_CONTENT + " TEXT)";

        // The Log table.
        String CREATE_WORKOUT_LOGS_TABLE = "CREATE TABLE " +
                TABLE_WORKOUT_LOGS + "("
                + COLUMN_LOG_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_LOG_TITLE + " TEXT,"
                + COLUMN_LOG_BODY + " TEXT,"
                + COLUMN_LOG_DATE_SEC + " INTEGER)";

        // All tables are created when database is initialized.
        db.execSQL(CREATE_WORKOUT_LOGS_TABLE);
        db.execSQL(CREATE_BUBBLES_TABLE);
    }


    //*************************************************************************
    // The onUpgrade() method is called when the handler is invoked with a
    // greater database version number from the one previously used. We will
    // simply remove the old database and create a new one.
    //*************************************************************************
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Delete all tables.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUBBLES + TABLE_WORKOUT_LOGS);

        // Create fresh tables.
        onCreate(db);
    }

    /**
     * Delete the entire database (all tables) and recreates it.
     *
     * @param context the app's context. See the following:
     * http://stackoverflow.com/questions/7917947/get-context-in-android
     */
    public void recreateDatabase(Context context)
    {
        Log.v(TAG, "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
                + "!!!!!!!!!!");
        Log.v(TAG, "!! recreateDatabase called: deleting database.");
        Log.v(TAG, "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
                + "!!!!!!!!!!");

        // Close the SQLiteOpenHelper. This kills any open connections to the
        // database.
        this.close();

        // Delete this database.
        context.deleteDatabase(DATABASE_NAME);

        // Force the database to recreate.
        SQLiteDatabase dummy = this.getWritableDatabase();
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
                + COLUMN_BUBBLE_CONTENT + " = \"" + bubbleContent + "\"";

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
                + COLUMN_BUBBLE_CONTENT + " = \"" + bubbleContent + "\"";

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
    // WorkoutLog Table operations.
    //*************************************************************************

    /**
     * Add a single WorkoutLog to the WorkoutLog table. Adding a duplicate
     * WorkoutLog is not allowed and will return false.
     *
     * Note that this will properly assign a unique ID for the WorkoutLog.
     * An ID should not be assigned when a WorkoutLog object is created, and
     * is done at the time the WorkoutLog is added to the WorkoutLog table.
     *
     * @param log The WorkoutLog to add to the WorkoutLog table.
     *
     * @return true iff the WorkoutLog was added successfully to the
     *         WorkoutLog table, else false.
     */
    public boolean addLog(WorkoutLog log)
    {
        // Assume Log insertion will be successful.
        boolean retVal = true;

        // Check if this Log is within the Log table already.
        // If so, return false, add nothing to the Log table.
        if (getWorkoutLog(log.getLogTitle(), log.getLogBody()) != null)
        {
            return false;
        }

        // Get a reference to our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // Create key-value pairs for the columns in the Log table.
        ContentValues values = new ContentValues();

        // Note: do not use the Log's ID. The Log table will
        // automatically generate it, since it is the primary key.

        // Log title and body.
        values.put(COLUMN_LOG_TITLE, log.getLogTitle());
        values.put(COLUMN_LOG_BODY, log.getLogBody());

        // Store millisecond data as seconds in the database.
        // This makes pulling a String description out, using SQLite functions,
        // much easier. First, cast the integer value (of milliseconds) to a
        // double, so when we divide by 1000, higher granularity is kept
        // (compared to integer division). This is crucial in keeping the
        // correct minute the Log was stored in the database.
        double ms = log.getLogDateMilliseconds();
        values.put(COLUMN_LOG_DATE_SEC, ((int)(ms / 1000)));

        // Insert new row (a single WorkoutLog) into the WorkoutLog table. If
        // insertion fails, return false.
        if (db.insert(TABLE_WORKOUT_LOGS, null, values) == -1)
        {
            retVal = false;
        }

        // Close out database.
        db.close();

        return retVal;
    }


    /**
     * Returns the WorkoutLog whose title and body field is given. If no such
     * WorkoutLog is found, null is returned.
     *
     * @param title The title of the WorkoutLog being searched for.
     *
     * @param body The body of the WorkoutLog being searched for.
     *
     * @return null if the WorkoutLog was not found, else a WorkoutLog object,
     *         filled with the WorkoutLogs's data, which is pulled from
     *         the database.
     */
    public WorkoutLog getWorkoutLog(String title, String body)
    {
        // Create a WorkoutLog to put the matching WorkoutLog's data in.
        // This is the return value.
        // Note: We will return the first matching WorkoutLog we find,
        //       however, there should not be duplicates--this is
        //       an invariant.
        WorkoutLog log = new WorkoutLog();

        // Get a reference to our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // Query to find the WorkoutLog with the given content. A
        // unique WorkoutLog has a distinct title, body combination.
        String query = "SELECT * FROM " + TABLE_WORKOUT_LOGS + " WHERE ("
                + COLUMN_LOG_TITLE + " = \"" + title + "\" AND "
                + COLUMN_LOG_BODY + " = \"" + body + "\")";

        Cursor cursor = db.rawQuery(query, null);

        // Find the first matching WorkoutLog.
        if (cursor.moveToFirst())
        {
            // ID is in column 0.
            log.setLogID(Integer.parseInt(cursor.getString(0)));

            // Title is in column 1.
            log.setLogTitle(cursor.getString(1));

            // Body is in column 2.
            log.setLogBody(cursor.getString(2));

            // The millisecond date field is in column 3, stored as seconds.
            // Multiply by 1000 to convert to seconds.
            log.setLogDateMilliseconds(cursor.getLong(3) * 1000);

            // Convert the seconds date field, using the Unix Epoch date
            // system, to get a textual description of the date. Convert
            // the seconds date field to local time. This is taking the seconds
            // date field and converting it to a textual description to show
            // the user.
            Cursor stringDateCursor = db.rawQuery("SELECT strftime('%d/%m/%Y %H:%M', " + cursor.getLong(3) + ", 'unixepoch', 'localtime')", null);

            // This is necessary to get the results of the query.
            stringDateCursor.moveToFirst();

            // Store this WorkoutLog's String date description.
            log.setLogDateString(stringDateCursor.getString(0));
        }
        else
        {
            // No WorkoutLog found. Return null.
            log = null;
        }

        // Close out the database and cursor.
        db.close();
        cursor.close();

        return log;
    }


    /**
     * Returns a List<WorkoutLog> with all of the WorkoutLogs in the
     * WorkoutLog table, sorted by date (and time) created. The first
     * item in the list will be the most recent.
     *
     * @return a List<WorkoutLog> with all of the WorkoutLogs in the
     *         WorkoutLog table.
     */
    public List<WorkoutLog> getAllWorkoutLogs()
    {
        // Return value.
        List<WorkoutLog> workoutLogs = new ArrayList<WorkoutLog>();

        // Get a reference to our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // Query to select all rows of Bubble table.
        String query = "Select * FROM " + TABLE_WORKOUT_LOGS + " ORDER BY "
                + COLUMN_LOG_DATE_SEC + " ASC";

        Cursor cursor = db.rawQuery(query, null);

        // Loop through all rows, adding WorkoutLogs to our list as we go.
        if (cursor.moveToFirst())
        {
            do
            {
                WorkoutLog log = new WorkoutLog();

                // ID is in column 0.
                log.setLogID(Integer.parseInt(cursor.getString(0)));

                // Title is in column 1.
                log.setLogTitle(cursor.getString(1));

                // Body is in column 2.
                log.setLogBody(cursor.getString(2));

                // The millisecond date field is in column 3, stored as seconds.
                // Multiply by 1000 to convert to seconds.
                log.setLogDateMilliseconds(cursor.getLong(3) * 1000);

                // Convert the seconds date field, using the Unix Epoch date
                // system, to get a textual description of the date. Convert
                // the seconds date field to local time. This is taking the seconds
                // date field and converting it to a textual description to show
                // the user.
                Cursor stringDateCursor = db.rawQuery("SELECT strftime('%d/%m/%Y %H:%M', " + cursor.getLong(3) + ", 'unixepoch', 'localtime')", null);

                // This is necessary to get the results of the query.
                stringDateCursor.moveToFirst();

                // Store this WorkoutLog's String date description.
                log.setLogDateString(stringDateCursor.getString(0));

                // Add this WorkoutLog to the list.
                workoutLogs.add(log);
            }
            while (cursor.moveToNext());
        }

        // Close out database and cursor.
        db.close();
        cursor.close();

        return workoutLogs;
    }

    /**
     * Returns the WorkoutLog whose ID is given. If no such WorkoutLog is
     * found, null is returned.
     *
     * @param ID The ID of the WorkoutLog being searched for.
     *
     * @return null if the WorkoutLog was not found, else a WorkoutLog object,
     *         filled with the WorkoutLogs's data, which is pulled from
     *         the database.
     */
    public WorkoutLog getWorkoutLog(int ID)
    {
        // TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO
        return null;
    }


//    /**
//     * Deletes the Bubble, specified by content, within the Bubble table.
//     *
//     * @param bubbleContent the Bubble to be deleted from the
//     *                      Bubble table.
//     *
//     * @return true iff this Bubble was deleted, else false.
//     */
//    public boolean deleteBubble(String bubbleContent)
//    {
//        // Assume we won't find this Bubble.
//        boolean result = false;
//
//        // The query within the Bubble Table.
//        String query = "Select * FROM " + TABLE_BUBBLES + " WHERE "
//                + COLUMN_BUBBLE_CONTENT + " =  \"" + bubbleContent + "\"";
//
//        // Get a reference of our database.
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        // For scanning the Bubble table.
//        Cursor cursor = db.rawQuery(query, null);
//
//        // Find the first instance of this Bubble.
//        // Note: duplicates Bubbles are now allowed, so there should only
//        //       ever be one of each Bubble in the Bubble table.
//        if (cursor.moveToFirst())
//        {
//            // Delete the Bubble whose ID matche the first found Bubble.
//            int numDeleted = db.delete(TABLE_BUBBLES, COLUMN_BUBBLE_ID + " = ?",
//                    new String[] { String.valueOf(Integer.parseInt(cursor.getString(0))) });
//
//            // We found a Bubble to delete, and it was deleted.
//            if (numDeleted > 0)
//            {
//                result = true;
//            }
//        }
//
//        // Close database and cursor.
//        db.close();
//        cursor.close();
//
//        return result;
//    }
//
//
//    /**
//     * Deletes all Bubbles in the Bubble Table.
//     *
//     * @return true iff all Bubbles are deleted from the Bubble table.
//     */
//    public boolean deleteAllBubbles()
//    {
//        // Get a reference of our database.
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        // Delete all records from Bubble table.
//        db.execSQL("DELETE FROM " + TABLE_BUBBLES);
//
//        return true;
//    }

}
