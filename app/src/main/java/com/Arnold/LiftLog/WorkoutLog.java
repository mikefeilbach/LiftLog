package com.Arnold.LiftLog;

import java.util.Calendar;

/**
 * Created by Mike Feilbach on 3/21/2015.
 * This class describes a WorkoutLog object. WorkoutLogs will be held in a
 * WorkoutLog table within the app's database.
 */
public class WorkoutLog
{
    // WorkoutLog ID. Unique among WorkoutLogs. This field is assigned (within
    // the database) when this WorkoutLog is added to the database. Until
    // this WorkoutLog is pulled from the database, this field will be invalid.
    private int _logID;

    // WorkoutLog's title and body.
    private String _logTitle;
    private String _logBody;

    // Millisecond description of this WorkoutLog's date (milliseconds since
    // epoch). This field is assigned when this WorkoutLog is first placed
    // in the database (until then, this field is invalid).
    private long _logDateMilliseconds;

    // String description of WorkoutLog's date. This field is assigned when
    // this WorkoutLog is first pulled from the database (this field will
    // be invalid until then).
    private String _logDateString;


    /**
     * Default WorkoutLog constructor. Note that this constructor will
     * assign default (invalid) values to all Log fields.
     */
    public WorkoutLog()
    {
    }


    /**
     * Log constructor. Note that the WorkoutLog's date in milliseconds (since
     * epoch) is assigned automatically within this constructor.
     *
     * @param logTitle The WorkoutLog's title.
     *
     * @param logBody The WorkoutLog's body.
     */
    public WorkoutLog(String logTitle, String logBody)
    {
        this._logTitle = logTitle;
        this._logBody = logBody;

        // Note that this millisecond time is not necessarily in the
        // user's timezone. When a Log is pulled from the database
        // (and the String Log date description is created), the proper
        // timezone conversion will be done. However, all millisecond
        // date descriptions will be in the default timezone. Note that
        // this will not affect sorting Log entries among other Log entries,
        // since they will all be in the same default timezone.
        Calendar calendar = Calendar.getInstance();
        this._logDateMilliseconds = calendar.getTimeInMillis();
    }


    //*************************************************************************
    // Modify or access WorkoutLog ID.
    //*************************************************************************
    public int getLogID()
    {
        return this._logID;
    }


    public void setLogID(int ID)
    {
        this._logID = ID;
    }


    //*************************************************************************
    // Modify or access WorkoutLog title.
    //*************************************************************************
    public void setLogTitle(String logTitle)
    {
        this._logTitle = logTitle;
    }


    public String getLogTitle()
    {
        return this._logTitle;
    }


    //*************************************************************************
    // Modify or access WorkoutLog body.
    //*************************************************************************
    public void setLogBody(String logBody)
    {
        this._logBody = logBody;
    }


    public String getLogBody()
    {
        return this._logBody;
    }


    //*************************************************************************
    // Modify or access WorkoutLog millisecond date description.
    //*************************************************************************
    public long getLogDateMilliseconds()
    {
        return this._logDateMilliseconds;
    }


    public void setLogDateMilliseconds(long dateMilliseconds)
    {
        this._logDateMilliseconds = dateMilliseconds;
    }


    //*************************************************************************
    // Modify or access WorkoutLog String date description.
    //*************************************************************************
    public String getLogDateString()
    {
        return this._logDateString;
    }


    public void setLogDateString(String dateString)
    {
        this._logDateString = dateString;
    }


    //*************************************************************************
    // Misc.
    //*************************************************************************

    @Override
    public String toString()
    {
        return "ID: " + getLogID() + ",  "
                + "Title: " + getLogTitle() + ",  "
                + "Body: " + getLogBody() + ",  "
                + "msec Date: " + getLogDateMilliseconds() + ",  "
                + "String Date: " + getLogDateString();
    }
}
