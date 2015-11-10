package com.example.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends Activity
{

    private boolean myTrueQuestion;

    private TextView myAnswerTextView;

    private Button myShowAnswerButton;

    public static final String EXTRA_ANSWER_IS_TRUE = "com.example.geoquiz.CheatActivity.answer_is_true";

    public static final String EXTRA_ANSWER_SHOWN = "com.example.geoquiz.CheatActivity.answer_shown";

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setAnswerShownResult( false );
        setContentView( R.layout.activity_cheat );
        myTrueQuestion = getIntent().getBooleanExtra( EXTRA_ANSWER_IS_TRUE, false );

        myAnswerTextView = ( TextView ) findViewById( R.id.answerTextView );
        myShowAnswerButton = ( Button ) findViewById( R.id.showAnswerButton );
        myShowAnswerButton.setOnClickListener( new OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                if( myTrueQuestion )
                {
                    myAnswerTextView.setText( R.string.true_button );
                }
                else
                {
                    myAnswerTextView.setText( R.string.false_button );
                }
                setAnswerShownResult( true );
            }
        } );
    }

    private void setAnswerShownResult( boolean isAnswerShown )
    {
        Intent data = new Intent();
        data.putExtra( EXTRA_ANSWER_SHOWN, isAnswerShown );
        setResult( RESULT_OK, data );
    }
}
