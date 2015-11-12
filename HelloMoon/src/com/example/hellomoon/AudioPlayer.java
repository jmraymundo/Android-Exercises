
package com.example.hellomoon;

import com.example.hellomoon.R;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class AudioPlayer
{
    private MediaPlayer myMediaPlayer;

    private boolean isPaused;

    public void play( Context c )
    {
        stop();
        myMediaPlayer = MediaPlayer.create( c, R.raw.one_small_step );
        myMediaPlayer.setOnCompletionListener( new OnCompletionListener()
        {
            @Override
            public void onCompletion( MediaPlayer mp )
            {
                stop();
            }
        } );
        myMediaPlayer.start();
    }

    public void pause()
    {
        if( myMediaPlayer != null )
        {
            if( isPaused )
            {
                myMediaPlayer.start();
            }
            else
            {
                myMediaPlayer.pause();
            }
            isPaused = !isPaused;
        }
    }

    public void stop()
    {
        if( null != myMediaPlayer )
        {
            myMediaPlayer.release();
            myMediaPlayer = null;
        }
    }
}
