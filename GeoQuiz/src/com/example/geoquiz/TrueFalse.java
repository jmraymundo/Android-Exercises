package com.example.geoquiz;

public class TrueFalse
{
    private boolean myAnswerTrue;

    private boolean myCheated;

    private int myQuestion;

    public TrueFalse( int question, boolean answerTrue )
    {
        myQuestion = question;
        myAnswerTrue = answerTrue;
        myCheated = false;
    }

    public int getQuestion()
    {
        return myQuestion;
    }

    public boolean isAnswerTrue()
    {
        return myAnswerTrue;
    }

    public boolean isCheated()
    {
        return myCheated;
    }

    public void setAnswerTrue( boolean answerTrue )
    {
        myAnswerTrue = answerTrue;
    }

    public void setCheated( boolean cheated )
    {
        myCheated = cheated;
    }

    public void setQuestion( int question )
    {
        myQuestion = question;
    }
}
