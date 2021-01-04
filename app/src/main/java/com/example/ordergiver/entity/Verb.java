package com.example.ordergiver.entity;

public class Verb
{
    //****************************
    // Attributes
    //****************************

    private int mVerbId;
    private String mInfinitiveVerb;
    private String mImperativeVerbFirst;
    private String mImperativeVerbSecond;
    private String mImperativeVerbThird;

    // Constructor
    public Verb() {}

    //****************************
    // Accessors
    //****************************

    // Getters
    public int getVerbId() {
        return mVerbId;
    }
    public String getInfinitiveVerb() { return mInfinitiveVerb; }
    public String getImperativeVerbFirst()
    {
        return mImperativeVerbFirst;
    }
    public String getImperativeVerbSecond()
    {
        return mImperativeVerbSecond;
    }
    public String getImperativeVerbThird()
    {
        return mImperativeVerbThird;
    }

    // Setters
    public void setVerbId(int verbId)
    {
        mVerbId = verbId;
    }

    public void setInfinitiveVerb (String infinitiveVerb)
    {
        mInfinitiveVerb = infinitiveVerb;
    }

    public void setImperativeVerbFirst(String imperativeVerbFirst)
    {
        mImperativeVerbFirst = imperativeVerbFirst;
    }

    public void setImperativeVerbSecond(String imperativeVerbSecond)
    {
        mImperativeVerbSecond = imperativeVerbSecond;
    }

    public void setImperativeVerbThird(String imperativeVerbThird)
    {
        mImperativeVerbThird = imperativeVerbThird;
    }
}
