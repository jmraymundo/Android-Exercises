
package com.example.hellomoon.fragment;

import com.example.hellomoon.AudioPlayer;
import com.example.hellomoon.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class HelloMoonFragment extends Fragment
{
    private AudioPlayer myAudioPlayer = new AudioPlayer();

    private Button myPlayButton;

    private Button myStopButton;

    private Button myPauseButton;

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState )
    {
        View v = inflater.inflate( R.layout.fragment_hello_moon, parent, false );
        myPlayButton = ( Button ) v.findViewById( R.id.hellomoon_playbutton );
        myPlayButton.setOnClickListener( new OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                myAudioPlayer.play( getActivity() );
            }
        } );
        myPauseButton = ( Button ) v.findViewById( R.id.hellomoon_pausebutton );
        myPauseButton.setOnClickListener( new OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                myAudioPlayer.pause();
            }
        } );
        myStopButton = ( Button ) v.findViewById( R.id.hellomoon_stopbutton );
        myStopButton.setOnClickListener( new OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                myAudioPlayer.stop();
            }
        } );
        return v;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        myAudioPlayer.stop();
    }
}
