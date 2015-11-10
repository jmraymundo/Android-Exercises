package com.example.geoquiz;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends Activity
{

    private Button myTrueButton;

    private Button myFalseButton;

    private ImageButton myNextButton;

    private ImageButton myPreviousButton;

    private TextView myQuestionTextView;

    private static final String TAG = "QuizActivity";

    private TrueFalse[] myQuestionBank = new TrueFalse[] { new TrueFalse( R.string.question_oceans, true ),
            new TrueFalse( R.string.question_mideast, false ), new TrueFalse( R.string.question_africa, false ),
            new TrueFalse( R.string.question_americas, true ), new TrueFalse( R.string.question_asia, true ) };

    private int myCurrentIndex = 0;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );

        Log.d( TAG, "onCreate(Bundle) called" );
        setContentView( R.layout.activity_quiz );

        myQuestionTextView = ( TextView ) findViewById( R.id.question_text_view );
        int question = myQuestionBank[myCurrentIndex].getQuestion();
        myQuestionTextView.setText( question );

        myTrueButton = ( Button ) findViewById( R.id.true_button );
        myTrueButton.setOnClickListener( new OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                checkAnswer( true );
            }
        } );
        myFalseButton = ( Button ) findViewById( R.id.false_button );
        myFalseButton.setOnClickListener( new OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                checkAnswer( false );
            }
        } );
        myPreviousButton = ( ImageButton ) findViewById( R.id.previous_button );
        myPreviousButton.setOnClickListener( new OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                if( myCurrentIndex == 0 )
                {
                    myCurrentIndex = myQuestionBank.length - 1;
                }
                else
                {
                    myCurrentIndex--;
                }
                updateQuestion();
            }
        } );
        myNextButton = ( ImageButton ) findViewById( R.id.next_button );
        myNextButton.setOnClickListener( new OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                myCurrentIndex = ( myCurrentIndex + 1 ) % myQuestionBank.length;
                updateQuestion();
            }
        } );
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Log.d( TAG, "onStart() called" );
    }

    @Override
    public void onPause()
    {
        super.onPause();
        Log.d( TAG, "onPause() called" );
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Log.d( TAG, "onResume() called" );
    }

    @Override
    public void onStop()
    {
        super.onStop();
        Log.d( TAG, "onStop() called" );
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.d( TAG, "onDestroy() called" );
    }

    private void checkAnswer( Boolean answer )
    {
        boolean correctAnswer = myQuestionBank[myCurrentIndex].isTrueQuestion();
        int resourceId;
        if( answer == correctAnswer )
        {
            resourceId = R.string.correct_toast;
        }
        else
        {
            resourceId = R.string.incorrect_toast;
        }
        Toast.makeText( this, resourceId, Toast.LENGTH_SHORT ).show();
    }

    private void updateQuestion()
    {
        int question = myQuestionBank[myCurrentIndex].getQuestion();
        myQuestionTextView.setText( question );
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.quiz, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if( id == R.id.action_settings )
        {
            return true;
        }
        return super.onOptionsItemSelected( item );
    }
}
