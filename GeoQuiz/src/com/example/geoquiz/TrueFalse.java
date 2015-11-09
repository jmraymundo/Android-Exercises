package com.example.geoquiz;

public class TrueFalse
{
    private int myQuestion;

    private boolean myTrueQuestion;

    public TrueFalse( int question, boolean trueQuestion )
    {
        myQuestion = question;
        myTrueQuestion = trueQuestion;
    }

    public int getQuestion()
    {
        return myQuestion;
    }

    public void setQuestion( int question )
    {
        myQuestion = question;
    }

    public boolean isTrueQuestion()
    {
        return myTrueQuestion;
    }

    public void setTrueQuestion( boolean trueQuestion )
    {
        myTrueQuestion = trueQuestion;
    }
}
