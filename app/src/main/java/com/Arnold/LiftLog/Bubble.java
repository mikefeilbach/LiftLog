package com.Arnold.LiftLog;

/**
 * Created by Mike Feilbach on 3/21/2015.
 * This class describes a Bubble object. Bubbles will be held in a Bubble
 * table within the app's database.
 */
public class Bubble
{
    // Bubble ID. Unique among Bubbles. This field is assigned (within
    // the database) when this Bubble is added to the database. Until
    // this Bubble is pulled from the database, this field will be invalid.
    private int _bubbleID;

    // Bubble's content. This is what the user sees within the physical
    // Bubble.
    private String _bubbleContent;

    /**
     * Default Bubble constructor. Note that this constructor will
     * assign default (invalid) values to all Bubble fields.
     */
    public Bubble()
    {
    }

    /**
     * Bubble constructor.
     *
     * @param bubbleContent The Bubble's content.
     */
    public Bubble(String bubbleContent)
    {
        this._bubbleContent = bubbleContent;
    }

    //*************************************************************************
    // Modify or access Bubble ID.
    //*************************************************************************
    public int getBubbleID()
    {
        return this._bubbleID;
    }

    /**
     * Sets this Bubble's ID to the given value. Note that this method should
     * only be used by the database's internal code. Do not use this method
     * elsewhere.
     *
     * @param bubbleID The ID to give this Bubble.
     */
    public void setBubbleID(int bubbleID)
    {
        this._bubbleID = bubbleID;
    }

    //*************************************************************************
    // Modify or access Bubble content.
    //*************************************************************************
    public void setBubbleContent(String bubbleContent)
    {
        this._bubbleContent = bubbleContent;

        // Make this change within the database.
    }

    public String getBubbleContent()
    {
        return this._bubbleContent;
    }
}
