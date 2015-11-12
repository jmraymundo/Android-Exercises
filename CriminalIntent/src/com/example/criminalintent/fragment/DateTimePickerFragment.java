
package com.example.criminalintent.fragment;

import java.util.Date;

import com.example.criminalintent.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DateTimePickerFragment extends DialogFragment
{
    public static final String EXTRA_DATE_TIME = "com.example.criminalintent.fragment.datetime";

    public static final String DIALOG_DATE = "com.example.criminalintent.fragment.date";

    public static final String DIALOG_TIME = "com.example.criminalintent.fragment.time";

    public Button mySetDate;

    public Button mySetTime;

    public static DateTimePickerFragment newInstance( Date date )
    {
        Log.d( "CriminalIntent", "DateTimePickerFragment newInstance!" );
        Bundle args = new Bundle();
        args.putSerializable( EXTRA_DATE_TIME, date );
        DateTimePickerFragment fragment = new DateTimePickerFragment();
        fragment.setArguments( args );
        return fragment;
    }

    private Date myDateTime;

    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState )
    {
        Log.d( "CriminalIntent", "DateTimePickerFragment created!" );
        myDateTime = ( Date ) getArguments().get( EXTRA_DATE_TIME );
        View v = getActivity().getLayoutInflater().inflate( R.layout.dialog_datetime, null );

        Log.d( "CriminalIntent", "DateTimePickerFragment view created!" );
        mySetDate = ( Button ) v.findViewById( R.id.dialog_datetime_setDateButton );
        mySetDate.setOnClickListener( new OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                Log.d( "CriminalIntent", "Changing date!" );
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment picker = DatePickerFragment.newInstance( myDateTime );
                Log.d( "CriminalIntent",
                        "Target fragment of datetimepicker: " + getTargetFragment().getClass().getSimpleName() );
                picker.setTargetFragment( getTargetFragment(), CrimeFragment.REQUEST_DATE );
                picker.show( fm, DIALOG_DATE );
            }
        } );
        Log.d( "CriminalIntent", "DateTimePickerFragment setDate button created!" );
        mySetTime = ( Button ) v.findViewById( R.id.dialog_datetime_setTimeButton );
        mySetTime.setOnClickListener( new OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                Log.d( "CriminalIntent", "Changing time!" );
                FragmentManager fm = getActivity().getSupportFragmentManager();
                TimePickerFragment picker = TimePickerFragment.newInstance( myDateTime );
                picker.setTargetFragment( getTargetFragment(), CrimeFragment.REQUEST_TIME );
                picker.show( fm, DIALOG_TIME );
            }
        } );
        Log.d( "CriminalIntent", "DateTimePickerFragment setTime button created!" );
        return createDialog( v );
    }

    private Dialog createDialog( View v )
    {
        Builder builder = new AlertDialog.Builder( getActivity() );
        builder.setView( v );
        builder.setTitle( R.string.datetime_picker_title );
        builder.setPositiveButton( android.R.string.ok, null );
        return builder.create();
    }
}
