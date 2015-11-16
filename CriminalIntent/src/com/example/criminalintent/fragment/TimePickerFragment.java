
package com.example.criminalintent.fragment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.example.criminalintent.R;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

public class TimePickerFragment extends DialogFragment
{
    public static final String EXTRA_TIME = "com.example.criminalintent.fragment.time";

    public static TimePickerFragment newInstance( Date dateTime )
    {
        Bundle args = new Bundle();
        args.putSerializable( EXTRA_TIME, dateTime );
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments( args );
        return fragment;
    }

    private Calendar myCalendar;

    private Date myDateTime;

    @SuppressLint( "NewApi" )
    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState )
    {
        myDateTime = ( Date ) getArguments().get( EXTRA_TIME );
        myCalendar = Calendar.getInstance();
        myCalendar.setTime( myDateTime );
        View v = getActivity().getLayoutInflater().inflate( R.layout.dialog_time, null );
        int hour = myCalendar.get( Calendar.HOUR_OF_DAY );
        int minute = myCalendar.get( Calendar.MINUTE );

        TimePicker timePicker = ( TimePicker ) v.findViewById( R.id.dialog_timePicker );
        timePicker.setOnTimeChangedListener( new OnTimeChangedListener()
        {
            @Override
            public void onTimeChanged( TimePicker view, int hourOfDay, int minute )
            {
                int year = myCalendar.get( Calendar.YEAR );
                int month = myCalendar.get( Calendar.MONTH );
                int day = myCalendar.get( Calendar.DAY_OF_MONTH );
                myDateTime = new GregorianCalendar( year, month, day, hourOfDay, minute ).getTime();
                Log.d( "CriminalIntent",
                        "T updated to: " + DateFormat.format( "EEE, MMM dd, yyyy - hh:mm aa", myDateTime ) );
                getArguments().putSerializable( EXTRA_TIME, myDateTime );

            }
        } );
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M )
        {
            timePicker = setTime( timePicker, hour, minute );
        }
        else
        {
            timePicker = setCurrentTime( timePicker, hour, minute );
        }

        Builder builder = new AlertDialog.Builder( getActivity() );
        builder.setView( timePicker );
        builder.setTitle( R.string.time_picker_title );
        builder.setPositiveButton( android.R.string.ok, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick( DialogInterface dialog, int which )
            {
                Log.d( "CriminalIntent", "Time change confirmed!" );
                sendResult( Activity.RESULT_OK );
                FragmentManager fm = getFragmentManager();
                Fragment fragment = fm.findFragmentByTag( CrimeFragment.DIALOG_DATETIME );
                ( ( DialogFragment ) fragment ).dismiss();
            }
        } );
        return builder.create();
    }

    private void sendResult( int resultCode )
    {
        Log.d( "CriminalIntent", "Target fragment of timepicker: " + getTargetFragment().getClass().getSimpleName() );
        if( null == getTargetFragment() )
        {
            return;
        }
        Intent i = new Intent();
        i.putExtra( EXTRA_TIME, myDateTime );
        getTargetFragment().onActivityResult( getTargetRequestCode(), resultCode, i );
    }

    @SuppressWarnings( "deprecation" )
    private TimePicker setCurrentTime( TimePicker timePicker, int hour, int minute )
    {
        timePicker.setCurrentHour( hour );
        timePicker.setCurrentMinute( minute );
        return timePicker;
    }

    @TargetApi( 23 )
    private TimePicker setTime( TimePicker timePicker, int hour, int minute )
    {
        timePicker.setHour( hour );
        timePicker.setMinute( minute );
        return timePicker;
    }
}
