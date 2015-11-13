
package com.example.criminalintent.fragment;

import java.util.ArrayList;

import com.example.criminalintent.R;
import com.example.criminalintent.activity.CrimePagerActivity;
import com.example.criminalintent.object.Crime;
import com.example.criminalintent.object.CrimeLab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

public class CrimeListFragment extends ListFragment
{
    private static final int REQUEST_CRIME = 0;

    private ArrayList< Crime > myCrimes;

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );

        myCrimes = CrimeLab.get( getActivity() ).getCrimes();

        CrimeAdapter adapter = new CrimeAdapter( myCrimes );
        setListAdapter( adapter );
    }

    @Override
    public void onListItemClick( ListView l, View v, int position, long id )
    {
        Crime crime = ( ( CrimeAdapter ) getListAdapter() ).getItem( position );
        Intent i = new Intent( getActivity(), CrimePagerActivity.class );
        i.putExtra( CrimeFragment.EXTRA_CRIME_ID, crime.getId() );
        startActivityForResult( i, REQUEST_CRIME );
    }

    @Override
    public void onActivityResult( int requestCode, int resultCode, Intent data )
    {
        if( REQUEST_CRIME == requestCode )
        {

        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        ( ( CrimeAdapter ) getListAdapter() ).notifyDataSetChanged();
    }

    private class CrimeAdapter extends ArrayAdapter< Crime >
    {
        public CrimeAdapter( ArrayList< Crime > crimes )
        {
            super( getActivity(), 0, crimes );
        }

        @Override
        public View getView( int position, View convertView, ViewGroup parent )
        {
            if( null == convertView )
            {
                convertView = getActivity().getLayoutInflater().inflate( R.layout.list_item_crime, null );
            }

            final Crime crime = getItem( position );

            TextView title = ( TextView ) convertView.findViewById( R.id.crime_list_item_titleTextView );
            title.setText( crime.getTitle() );
            TextView date = ( TextView ) convertView.findViewById( R.id.crime_list_item_dateTextView );
            date.setText( DateFormat.format( "EEE, MMM dd, yyyy - hh:mm aa", crime.getDateTime() ) );
            CheckBox solved = ( CheckBox ) convertView.findViewById( R.id.crime_list_item_solvedCheckBox );
            solved.setChecked( crime.isSolved() );
            solved.setOnCheckedChangeListener( new OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged( CompoundButton buttonView, boolean isChecked )
                {
                    crime.setSolved( isChecked );
                }
            } );

            return convertView;
        }

    }
}
