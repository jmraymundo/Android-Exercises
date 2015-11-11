
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

    private Date myDate;

    public static DatePickerFragment newInstance( Date date )
    {
        Bundle args = new Bundle();
        args.putSerializable( EXTRA_DATE, date );
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState )
    {
        myDate = ( Date ) getArguments().get( EXTRA_DATE );
        View v = getActivity().getLayoutInflater().inflate( R.layout.dialog_date, null );

        Calendar c = Calendar.getInstance();
        c.setTime( myDate );
        int year = c.get( Calendar.YEAR );
        int month = c.get( Calendar.MONTH );
        int day = c.get( Calendar.DAY_OF_MONTH );

        DatePicker datePicker = ( DatePicker ) v.findViewById( R.id.dialog_date_datePicker );
        datePicker.init( year, month, day, new OnDateChangedListener()
        {
            @Override
            public void onDateChanged( DatePicker view, int year, int monthOfYear, int dayOfMonth )
            {
                myDate = new GregorianCalendar( year, monthOfYear, dayOfMonth ).getTime();
                Log.d( "CriminalIntent",
                        "Date updated to: " + DateFormat.format( "EEE, MMM dd, yyyy - hh:mm aa", myDate ) );
                getArguments().putSerializable( EXTRA_DATE, myDate );
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
        if( null == getTargetFragment() )
        {
            return;
        }
        Intent i = new Intent();
        i.putExtra( EXTRA_DATE, myDate );

        getTargetFragment().onActivityResult( getTargetRequestCode(), resultCode, i );
    }
}
