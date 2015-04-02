package com.Arnold.LiftLog;

import java.util.Calendar;

/**
 * Created by Mike Feilbach on 3/21/2015.
 * This class describes a Log object. Logs will be held in a Log
 * Table within the app's database.
 */
public class Log
{
    // Log ID. Unique among Logs. This field is assigned (within
    // the database) when this Log is added to the database. Until
    // this Log is pulled from the database, this field will be invalid.
    private int _logID;

    // Log's title and body.
    private String _logTitle;
    private String _logBody;

    // Millisecond description of this Log's date (milliseconds since
    // epoch). This field is assigned when this Log is first placed
    // in the database (until then, this field is invalid).
    private long _logDateMilliseconds;

    // String description of Log's date. This field is assigned when
    // this Log is first pulled from the database (this field will
    // be invalid until then).
    private String _logDateString;

    /**
     * Default Log constructor. Note that this constructor will
     * assign default (invalid) values to all Log fields.
     */
    public Log()
    {
    }

    /**
     * Log constructor. Note that the Log's date in milliseconds (since
     * epoch) is assigned automatically within this constructor.
     *
     * @param logTitle - the Log's title.
     *
     * @param logBody - the Log's body.
     */
    public Log(String logTitle, String logBody)
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
    // Access Log ID.
    //*************************************************************************
    public int getLogID()
    {
        return this._logID;
    }

    //*************************************************************************
    // Modify or access Log title.
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
    // Modify or access Log body.
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
    // Access Log millisecond date description.
    //*************************************************************************
    public long getLogDateMilliseconds()
    {
        return this._logDateMilliseconds;
    }

    //*************************************************************************
    // Access Log String date description.
    //*************************************************************************
    public String getLogDateString()
    {
        return this._logDateString;
    }
}
