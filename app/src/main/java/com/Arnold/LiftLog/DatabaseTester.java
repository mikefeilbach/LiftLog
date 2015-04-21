package com.Arnold.LiftLog;

import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by Mike Feilbach on 4/3/2015. Has methods which exercise the
 * app's database with automated tests.
 */
public class DatabaseTester
{
    // For tagging messages in Log Cat.
    private static final String TAG = "MyActivity";

    // The app's context.
    private Context context;

    /**
     * Default constructor. Do not use, we must populate context when
     * instantiating this class.
     */
    public DatabaseTester()
    {
    }


    /**
     * Constructor that initializes the context field. The context must
     * be given in order to instantiate a DatabaseHandler, which is used
     * to interact with the app's database.
     *
     * @param context The app's context.
     */
    public DatabaseTester(Context context)
    {
        this.context = context;
    }


    /**
     * Used to test the database functionality. Will return early if one
     * of the tests does not pass. Tests are self-commenting via Log print
     * statements.
     */
    public void testDatabase()
    {
        // Create database handler. This is done whenever we want to interact
        // with the database. It gives access to the database functions in
        // my database API. It can add Bubbles, search for Bubbles, delete
        // Bubbles, etc. It also does all Log interaction, with similar
        // functions.
        DatabaseHandler db = new DatabaseHandler(this.context);

        // Delete & recreate the database.
        db.recreateDatabase(this.context);

        // Start test.
        Log.v(TAG, "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
                + "@@@@@@@@@@");
        Log.v(TAG, "@@ START: Database Test.");
        Log.v(TAG, "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
                + "@@@@@@@@@@");

        //*********************************************************************
        // Testing Bubble Table.
        //*********************************************************************

        Log.v(TAG, "Deleting all Bubbles in the Bubble table.");
        db.deleteAllBubbles();

        Log.v(TAG, "Make sure there are zero Bubbles in the Bubble table.");
        if (db.getAllBubbles().size() == 0)
        {
            Log.v(TAG, "Passed");
        }
        else
        {
            Log.v(TAG, "**************** FAILED");
            return;
        }

        Log.v(TAG, "Inserting: Mike, Paul, Zheng, Lauro.");
        db.addBubble(new Bubble("Mike", Bubble.BUBBLE_TYPE_REPS_SETS));
        db.addBubble(new Bubble("Paul", Bubble.BUBBLE_TYPE_WEIGHT_REST));
        db.addBubble(new Bubble("Zheng", Bubble.BUBBLE_TYPE_REPS_SETS));
        db.addBubble(new Bubble("Lauro", Bubble.BUBBLE_TYPE_EXERCISE));

        Log.v(TAG, "Checking there are four records in Bubble table.");
        if (db.getAllBubbles().size() == 4)
        {
            Log.v(TAG, "Passed");
        }
        else
        {
            Log.v(TAG, "**************** FAILED.");
            return;
        }

        // Print out all Bubbles in Bubble table.
        List<Bubble> bubbles = db.getAllBubbles();

        Log.v(TAG, "Bubbles in table printed below. Should be Mike, Paul, Zheng, Lauro.");
        for (Bubble bubble : bubbles)
        {
            String log = "ID: " + bubble.getBubbleID()
                    + ", Content: " + bubble.getBubbleContent()
                    + ", Type: " + bubble.getBubbleType();

            // Write this Bubble to the Log.
            Log.v(TAG, log);
        }

        Log.v(TAG, "Deleting: Paul, Lauro. Should be able to delete both.");
        boolean del1 = db.deleteBubble("Paul");
        boolean del2 = db.deleteBubble("Lauro");

        if (del1 & del2)
        {
            Log.v(TAG, "Passed");
        }
        else
        {
            Log.v(TAG, "**************** FAILED");
            return;
        }

        Log.v(TAG, "Trying to delete Paul again. Should not be able to " +
                "delete--this record no longer exists.");
        if (!db.deleteBubble("Paul"))
        {
            Log.v(TAG, "Passed");
        }
        else
        {
            Log.v(TAG, "**************** FAILED");
            return;
        }

        // Print out all Bubbles in Bubble table.
        bubbles = db.getAllBubbles();
        Log.v(TAG, "Bubbles in table printed below. Should be Mike, Zheng.");
        for (Bubble bubble : bubbles)
        {
            String log = "ID: " + bubble.getBubbleID()
                    + ", Content: " + bubble.getBubbleContent()
                    + ", Type: " + bubble.getBubbleType();

            // Write this Bubble to the Log.
            Log.v(TAG, log);
        }

        Log.v(TAG, "Making sure there are only two records in Bubble table.");
        if (db.getAllBubbles().size() == 2)
        {
            Log.v(TAG, "Passed");
        }
        else
        {
            Log.v(TAG, "**************** FAILED");
            return;
        }

        Log.v(TAG, "Try to lookup Mike. Should work.");
        if (db.getBubble("Mike").getBubbleContent().equals("Mike"))
        {
            Log.v(TAG, "Passed");
        }
        else
        {
            Log.v(TAG, "**************** FAILED");
            return;
        }

        Log.v(TAG, "Try to lookup Zheng. Should work.");
        if (db.getBubble("Zheng").getBubbleContent().equals("Zheng"))
        {
            Log.v(TAG, "Passed");
        }
        else
        {
            Log.v(TAG, "**************** FAILED");
            return;
        }

        Log.v(TAG, "Try to lookup Paul. Shouldn't work.");
        if (db.getBubble("Paul") ==  null)
        {
            Log.v(TAG, "Passed");
        }
        else
        {
            Log.v(TAG, "**************** FAILED");
            return;
        }

        Log.v(TAG, "Try to lookup Lauro. Shouldn't work.");
        if (db.getBubble("Lauro") == null)
        {
            Log.v(TAG, "Passed");
        }
        else
        {
            Log.v(TAG, "**************** FAILED");
            return;
        }

        Log.v(TAG, "Trying to insert Mike. Shouldn't work, no duplicates allowed.");
        if (!db.addBubble(new Bubble("Mike", Bubble.BUBBLE_TYPE_WEIGHT_REST)))
        {
            Log.v(TAG, "Passed");
        }
        else
        {
            Log.v(TAG, "**************** FAILED");
            return;
        }

        Log.v(TAG, "Trying to insert Zheng. Shouldn't work, no duplicates allowed.");
        if (!db.addBubble(new Bubble("Zheng", Bubble.BUBBLE_TYPE_REPS_SETS)))
        {
            Log.v(TAG, "Passed");
        }
        else
        {
            Log.v(TAG, "**************** FAILED");
            return;
        }

        Log.v(TAG, "Add three new records: Ke$ha, Wiz Khalifa, Demi Lovato.");
        db.addBubble(new Bubble("Ke$ha", Bubble.BUBBLE_TYPE_REPS_SETS));
        db.addBubble(new Bubble("Wiz Khalifa", Bubble.BUBBLE_TYPE_REPS_SETS));
        db.addBubble(new Bubble("Demi Lovato", Bubble.BUBBLE_TYPE_WEIGHT_REST));

        // Print out all Bubbles in Bubble table.
        bubbles = db.getAllBubbles();
        Log.v(TAG, "Bubbles in table printed below. Should be Mike, Zheng, Ke$ha, " +
                "Wiz Khalifa, Demi Lovato.");
        for (Bubble bubble : bubbles)
        {
            String log = "ID: " + bubble.getBubbleID()
                    + ", Content: " + bubble.getBubbleContent()
                    + ", Type: " + bubble.getBubbleType();

            // Write this Bubble to the Log.
            Log.v(TAG, log);
        }

        Log.v(TAG, "Making sure there are five records in Bubble table.");
        if (db.getAllBubbles().size() == 5)
        {
            Log.v(TAG, "Passed");
        }
        else
        {
            Log.v(TAG, "**************** FAILED");
            return;
        }

        Log.v(TAG, "Update Ke$ha's bubble type from REPS_SETS to WEIGHT_REST. Should fail--can't update Bubbles");
        if (db.updateBubble(4, new Bubble("Ke$ha", Bubble.BUBBLE_TYPE_WEIGHT_REST)))
        {

            Log.v(TAG, "**************** FAILED");
            return;
        }
        else
        {
            Log.v(TAG, "Passed");
        }

        // Print out all Bubbles in Bubble table.
        bubbles = db.getAllBubbles();
        Log.v(TAG, "Bubbles in table printed below. Should be Mike, Zheng, Ke$ha, " +
                "Wiz Khalifa, Demi Lovato.");
        for (Bubble bubble : bubbles)
        {
            String log = "ID: " + bubble.getBubbleID()
                    + ", Content: " + bubble.getBubbleContent()
                    + ", Type: " + bubble.getBubbleType();

            // Write this Bubble to the Log.
            Log.v(TAG, log);
        }

        Log.v(TAG, "Try to update the Bubble Ke$ha to  Demi Lovato.");
        if (!db.updateBubble(4, new Bubble("Demi Lovato", Bubble.BUBBLE_TYPE_WEIGHT_REST)))
        {
            Log.v(TAG, "Passed");
        }
        else
        {
            Log.v(TAG, "**************** FAILED");
            return;
        }

        //*********************************************************************
        // Testing WorkoutLog Table.
        //*********************************************************************

        Log.v(TAG, "Deleting all WorkoutLogs in the WorkoutLog table.");
        db.deleteAllWorkoutLogs();

        Log.v(TAG, "Make sure there are zero WorkoutLogs in the WorkoutLog table.");
        if (db.getAllWorkoutLogs().size() == 0)
        {
            Log.v(TAG, "Passed");
        }
        else
        {
            Log.v(TAG, "**************** FAILED");
            return;
        }

        Log.v(TAG, "Inserting (title, body): (Ab, 10), (Chest, 20), (Triceps 35)");
        db.addWorkoutLog(new WorkoutLog("Ab", "10"));
        db.addWorkoutLog(new WorkoutLog("Chest", "20"));
        db.addWorkoutLog(new WorkoutLog("Triceps", "35"));

        Log.v(TAG, "Checking there are three records in Bubble table.");
        if (db.getAllWorkoutLogs().size() == 3)
        {
            Log.v(TAG, "Passed");
        }
        else
        {
            Log.v(TAG, "**************** FAILED.");
            return;
        }

        // Print out all WorkoutLogs in WorkoutLog table.
        List<WorkoutLog> logs = db.getAllWorkoutLogs();

        Log.v(TAG, "WorkoutLogs in table printed below. Should be (title, body): (Ab, 10), (Chest, 20), (Triceps 35)");
        for (WorkoutLog log : logs)
        {
            // Write this WorkoutLog to the Log.
            Log.v(TAG, log.toString());
        }

        Log.v(TAG, "Deleting (title, body): (Chest, 20) and (Ab, 10). Should be able to delete both.");
        boolean del3 = db.deleteWorkoutLog("Chest", "20");
        boolean del4 = db.deleteWorkoutLog("Ab", "10");

        if (del3 & del4)
        {
            Log.v(TAG, "Passed");
        }
        else
        {
            Log.v(TAG, "**************** FAILED");
            return;
        }

        Log.v(TAG, "Trying to delete (Chest, 20) again. Should not be able to " +
                "delete--this record no longer exists.");
        if (!db.deleteWorkoutLog("Chest", "20"))
        {
            Log.v(TAG, "Passed");
        }
        else
        {
            Log.v(TAG, "**************** FAILED");
            return;
        }

        // Print out all WorkoutLogs in WorkoutLog table.
        logs = db.getAllWorkoutLogs();
        Log.v(TAG, "WorkoutLogs in table printed below. Should be (title, body): (Triceps, 35).");
        Log.v(TAG, logs.get(0).toString());
        if ((!logs.get(0).getLogTitle().equals("Triceps")) || (!logs.get(0).getLogBody().equals(("35"))))
        {
            Log.v(TAG, "**************** FAILED");
            return;
        }
        else
        {
            Log.v(TAG, "Passed");
        }

        Log.v(TAG, "Making sure there is only one record in WorkoutLog table.");
        if (db.getAllWorkoutLogs().size() == 1)
        {
            Log.v(TAG, "Passed");
        }
        else
        {
            Log.v(TAG, "**************** FAILED");
            return;
        }

        Log.v(TAG, "Try to lookup (title, body): (Triceps, 35). Should work.");
        if (db.getWorkoutLog("Triceps", "35") != null)
        {
            Log.v(TAG, "Passed");
        }
        else
        {
            Log.v(TAG, "**************** FAILED");
            return;
        }

        Log.v(TAG, "Try to lookup (title, body): (Ab, 10). Shouldn't work.");
        if (db.getWorkoutLog("Ab", "10") ==  null)
        {
            Log.v(TAG, "Passed");
        }
        else
        {
            Log.v(TAG, "**************** FAILED");
            return;
        }

        Log.v(TAG, "Try to lookup (title, body): (Chest, 20). Shouldn't work.");
        if (db.getWorkoutLog("Chest", "20") == null)
        {
            Log.v(TAG, "Passed");
        }
        else
        {
            Log.v(TAG, "**************** FAILED");
            return;
        }

        Log.v(TAG, "Trying to insert (title, body): (Triceps, 35). Shouldn't work, no duplicates allowed.");
        if (!db.addWorkoutLog(new WorkoutLog("Triceps", "35")))
        {
            Log.v(TAG, "Passed");
        }
        else
        {
            Log.v(TAG, "**************** FAILED");
            return;
        }

        Log.v(TAG, "Add two new records (title, body): (Shoulders, 20) and (Forearms, 15).");
        db.addWorkoutLog(new WorkoutLog("Shoulders", "20"));
        db.addWorkoutLog(new WorkoutLog("Forearms", "15"));

        // Print out all WorkoutLogs in WorkoutLog table.
        logs = db.getAllWorkoutLogs();
        Log.v(TAG, "WorkoutLogs in table printed below. Should be (title, body): (Triceps, 35), (Shoulders, 20), and (Forearms, 15).");
        for (WorkoutLog log : logs)
        {
            // Write this Bubble to the Log.
            Log.v(TAG, log.toString());
        }

        // Check these results autonomously.
        if ((logs.get(0).getLogTitle().equals("Triceps")) && (logs.get(0).getLogBody().equals("35"))
                && (logs.get(1).getLogTitle().equals("Shoulders")) && (logs.get(1).getLogBody().equals("20"))
                && (logs.get(2).getLogTitle().equals("Forearms")) && (logs.get(2).getLogBody().equals("15")))
        {
            Log.v(TAG, "Passed");
        }
        else
        {
            Log.v(TAG, "**************** FAILED");
            return;
        }

        Log.v(TAG, "Making sure there are three records in WorkoutLog table.");
        if (db.getAllWorkoutLogs().size() == 3)
        {
            Log.v(TAG, "Passed");
        }
        else
        {
            Log.v(TAG, "**************** FAILED");
            return;
        }

        Log.v(TAG, "Try to get WorkoutLog with ID = 1. Should fail.");
        if (db.getWorkoutLog(1) == null)
        {
            Log.v(TAG, "Passed");
        }
        else
        {
            Log.v(TAG, "**************** FAILED");
            return;
        }

        Log.v(TAG, "Try to get WorkoutLog with ID = 2. Should fail.");
        if (db.getWorkoutLog(2) == null)
        {
            Log.v(TAG, "Passed");
        }
        else
        {
            Log.v(TAG, "**************** FAILED");
            return;
        }

        Log.v(TAG, "Try to get WorkoutLog with ID = 3. Should pass.");
        if (db.getWorkoutLog(3) != null)
        {
            Log.v(TAG, "Passed");
        }
        else
        {
            Log.v(TAG, "**************** FAILED");
            return;
        }

        Log.v(TAG, "Try to get WorkoutLog with ID = 5. Should pass.");
        if (db.getWorkoutLog(5) != null)
        {
            Log.v(TAG, "Passed");
        }
        else
        {
            Log.v(TAG, "**************** FAILED");
            return;
        }

        Log.v(TAG, "Try to get WorkoutLog with ID = 6. Should fail.");
        if (db.getWorkoutLog(6) == null)
        {
            Log.v(TAG, "Passed");
        }
        else
        {
            Log.v(TAG, "**************** FAILED");
            return;
        }

        Log.v(TAG, "Try to update WorkoutLog with ID = 5 to (Abs, 5). Should pass.");
        WorkoutLog temp = new WorkoutLog("Abs", "5");
        boolean tempBool = db.updateWorkoutLog(5, temp);
        String testString;
        if (tempBool)
        {
            testString = "Successfully updated.";
        }
        else
        {
            testString = "FAILURE -- NOT updated.";
        }
        Log.v(TAG, testString);
        if (db.getWorkoutLog(5).getLogBody().equals("5") && db.getWorkoutLog(5).getLogTitle().equals("Abs"))
        {
            Log.v(TAG, "Passed");
        }
        else
        {
            Log.v(TAG, "**************** FAILED");
            return;
        }

        // Print out all WorkoutLogs in WorkoutLog table.
        logs = db.getAllWorkoutLogs();
        Log.v(TAG, "WorkoutLogs in table printed below. Should be (title, body): (Triceps, 35), (Shoulders, 20), and (Abs, 5).");
        for (WorkoutLog log : logs)
        {
            // Write this Bubble to the Log.
            Log.v(TAG, log.toString());
        }

        // End test! If we get here, all tests have passed. If any tests
        // fail, they return right away.
        Log.v(TAG, "********************************************************"
                + "**********");
        Log.v(TAG, "** END: Database Test. All tests passed!");
        Log.v(TAG, "********************************************************"
                + "**********");
    }
}
