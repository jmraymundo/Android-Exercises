package com.example.geoquiz;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class QuizActivity extends Activity
{

    private Button myTrueButton;

    private Button myFalseButton;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_quiz );
        myTrueButton = ( Button ) findViewById( R.id.true_button );
        myTrueButton.setOnClickListener( new OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                Toast.makeText( QuizActivity.this, R.string.incorrect_toast, Toast.LENGTH_SHORT );
            }
        } );
        myFalseButton = ( Button ) findViewById( R.id.false_button );
        myFalseButton.setOnClickListener( new OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                Toast.makeText( QuizActivity.this, R.string.correct_toast, Toast.LENGTH_SHORT );
            }
        } );
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
