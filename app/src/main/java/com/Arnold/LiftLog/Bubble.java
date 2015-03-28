package com.Arnold.LiftLog;

/**
 * Created by Mike Feilbach on 3/21/2015.
 * This class describes a Bubble object. Bubbles will be held in a Bubble
 * table within the app's database.
 */
public class Bubble
{
    // Bubble ID. Unique among Bubbles.
    private int _bubbleID;

    // Bubble's content. This is what the user sees within the physical
    // Bubble.
    private String _bubbleContent;

    /**
     * Default Bubble constructor.
     */
    public Bubble()
    {
    }

    /**
     * Bubble constructor.
     * @param bubbleContent - the Bubble's content.
     */
    public Bubble(String bubbleContent)
    {
        this._bubbleContent = bubbleContent;
    }

    /**
     * Bubble constructor.
     * @param bubbleID - the Bubble's unique ID.
     * @param bubbleContent - the Bubble's content.
     */
    // NOTE: Bubble ID is generated when a Bubble is added to the Bubble
    //       table. The ID field is never updated in this class, unless
    //       we pull a Bubble from the Bubble Table and return its
    //       contents through a Bubble object. Therefore, never reference
    //       Bubble objects--always pull from the Bubble table itself
    //       when dealing with Bubble objects.
    //
    //public Bubble(int bubbleID, String bubbleContent)
    //{
    //    this._bubbleID = bubbleID;
    //    this._bubbleContent = bubbleContent;
    //}

    //*************************************************************************
    // Modify or access Bubble ID.
    //*************************************************************************
    public void setBubbleID(int bubbleID)
    {
        this._bubbleID = bubbleID;
    }

    public int getBubbleID()
    {
        return this._bubbleID;
    }

    //*************************************************************************
    // Modify or access Bubble content.
    //*************************************************************************
    public void setBubbleContent(String bubbleContent)
    {
        this._bubbleContent = bubbleContent;
    }

    public String getBubbleContent()
    {
        return this._bubbleContent;
    }
}
