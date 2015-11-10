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

    private static final String KEY_INDEX = "index";

    private static final String TAG = "QuizActivity";

    private int myCurrentIndex = 0;

    private Button myFalseButton;

    private ImageButton myNextButton;

    private ImageButton myPreviousButton;

    private TrueFalse[] myQuestionBank = new TrueFalse[] { new TrueFalse( R.string.question_oceans, true ),
            new TrueFalse( R.string.question_mideast, false ), new TrueFalse( R.string.question_africa, false ),
            new TrueFalse( R.string.question_americas, true ), new TrueFalse( R.string.question_asia, true ) };

    private TextView myQuestionTextView;;

    private Button myTrueButton;

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

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        if( null != savedInstanceState )
        {
            myCurrentIndex = savedInstanceState.getInt( KEY_INDEX );
        }
        Log.d( TAG, "onCreate(Bundle) called" );
        setContentView( R.layout.activity_quiz );

        myQuestionTextView = ( TextView ) findViewById( R.id.question_text_view );
        int question = myQuestionBank[myCurrentIndex].getQuestion();
        myQuestionTextView.setText( question );

        myTrueButton = initializeOption( R.id.true_button, true );
        myFalseButton = initializeOption( R.id.false_button, false );
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

    private Button initializeOption( int id, final boolean answer )
    {
        Button button = ( Button ) findViewById( id );
        button.setOnClickListener( new OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                checkAnswer( answer );
            }
        } );
        return button;
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.quiz, menu );
        return true;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.d( TAG, "onDestroy() called" );
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

    @Override
    protected void onPause()
    {
        super.onPause();
        Log.d( TAG, "onPause() called" );
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d( TAG, "onResume() called" );
    }

    @Override
    protected void onSaveInstanceState( Bundle savedInstanceState )
    {
        super.onSaveInstanceState( savedInstanceState );
        Log.i( TAG, "onSaveInstanceState" );
        savedInstanceState.putInt( KEY_INDEX, myCurrentIndex );
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Log.d( TAG, "onStart() called" );
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Log.d( TAG, "onStop() called" );
    }

    private void updateQuestion()
    {
        int question = myQuestionBank[myCurrentIndex].getQuestion();
        myQuestionTextView.setText( question );
    }
}
