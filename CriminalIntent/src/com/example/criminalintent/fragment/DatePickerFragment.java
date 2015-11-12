
package com.example.criminalintent.fragment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.example.criminalintent.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

public class DatePickerFragment extends DialogFragment
{
    public static final String EXTRA_DATE = "com.example.criminalintent.fragment.date";

    public static DatePickerFragment newInstance( Date dateTime )
    {
        Bundle args = new Bundle();
        args.putSerializable( EXTRA_DATE, dateTime );
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments( args );
        return fragment;
    }

    private Calendar myCalendar;

    private Date myDateTime;

    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState )
    {
        myDateTime = ( Date ) getArguments().get( EXTRA_DATE );
        myCalendar = Calendar.getInstance();
        myCalendar.setTime( myDateTime );
        View v = getActivity().getLayoutInflater().inflate( R.layout.dialog_date, null );
        int year = myCalendar.get( Calendar.YEAR );
        int month = myCalendar.get( Calendar.MONTH );
        int day = myCalendar.get( Calendar.DAY_OF_MONTH );

        DatePicker datePicker = ( DatePicker ) v.findViewById( R.id.dialog_datePicker );
        datePicker.init( year, month, day, new OnDateChangedListener()
        {
            @Override
            public void onDateChanged( DatePicker view, int year, int monthOfYear, int dayOfMonth )
            {
                int hour = myCalendar.get( Calendar.HOUR_OF_DAY );
                int minute = myCalendar.get( Calendar.MINUTE );
                myDateTime = new GregorianCalendar( year, monthOfYear, dayOfMonth, hour, minute ).getTime();
                Log.d( "CriminalIntent",
                        "Date updated to: " + DateFormat.format( "EEE, MMM dd, yyyy - hh:mm aa", myDateTime ) );
                getArguments().putSerializable( EXTRA_DATE, myDateTime );
            }
        } );

        Builder builder = new AlertDialog.Builder( getActivity() );
        builder.setView( datePicker );
        builder.setTitle( R.string.date_picker_title );
        builder.setPositiveButton( android.R.string.ok, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick( DialogInterface dialog, int which )
            {
                Log.d( "CriminalIntent", "Date change confirmed!" );
                sendResult( Activity.RESULT_OK );
            }
        } );
        return builder.create();
    }

    private void sendResult( int resultCode )
    {
        Log.d( "CriminalIntent", "Target fragment of datepicker: " + getTargetFragment().getClass().getSimpleName() );
        if( null == getTargetFragment() )
        {
            return;
        }
        Intent i = new Intent();
        i.putExtra( EXTRA_DATE, myDateTime );
        getTargetFragment().onActivityResult( getTargetRequestCode(), resultCode, i );
    }
}
