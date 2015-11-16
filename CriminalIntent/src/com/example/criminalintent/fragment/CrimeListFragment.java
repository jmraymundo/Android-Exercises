
package com.example.criminalintent.fragment;

import java.util.ArrayList;

import com.example.criminalintent.R;
import com.example.criminalintent.activity.CrimePagerActivity;
import com.example.criminalintent.object.Crime;
import com.example.criminalintent.object.CrimeLab;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private static final int NEW_CRIME = 1;

    private static final int REQUEST_CRIME = 0;

    private ArrayList< Crime > myCrimes;

    private boolean isSubtitleVisible;

    @Override
    public void onActivityResult( int requestCode, int resultCode, Intent data )
    {
        switch( requestCode )
        {
            case REQUEST_CRIME:
                break;
            case NEW_CRIME:
                break;
        }

    }

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setHasOptionsMenu( true );
        getActivity().setTitle( R.string.crimes_title );

        myCrimes = CrimeLab.get( getActivity() ).getCrimes();

        CrimeAdapter adapter = new CrimeAdapter( myCrimes );
        setListAdapter( adapter );

        setRetainInstance( true );
        isSubtitleVisible = false;
    }

    @Override
    public void onCreateOptionsMenu( Menu menu, MenuInflater inflater )
    {
        super.onCreateOptionsMenu( menu, inflater );
        inflater.inflate( R.menu.fragment_crime_list, menu );
        MenuItem showSubtitle = menu.findItem( R.id.menu_item_show_subtitle );
        if( isSubtitleVisible && null != showSubtitle )
        {
            showSubtitle.setTitle( R.string.hide_subtitle );
        }
    }

    @Override
    public void onListItemClick( ListView l, View v, int position, long id )
    {
        Crime crime = ( ( CrimeAdapter ) getListAdapter() ).getItem( position );
        Intent i = new Intent( getActivity(), CrimePagerActivity.class );
        i.putExtra( CrimeFragment.EXTRA_CRIME_ID, crime.getId() );
        startActivityForResult( i, REQUEST_CRIME );
    }

    @TargetApi( Build.VERSION_CODES.HONEYCOMB )
    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        switch( item.getItemId() )
        {
            case R.id.menu_item_new_crime:
                Crime c = new Crime();
                CrimeLab.get( getActivity() ).addCrime( c );
                Intent i = new Intent( getActivity(), CrimePagerActivity.class );
                i.putExtra( CrimeFragment.EXTRA_CRIME_ID, c.getId() );
                startActivityForResult( i, NEW_CRIME );
                return true;
            case R.id.menu_item_show_subtitle:
                ActionBar actionBar = getActivity().getActionBar();
                if( null == actionBar.getSubtitle() )
                {
                    actionBar.setSubtitle( R.string.subtitle );
                    item.setTitle( R.string.hide_subtitle );
                    isSubtitleVisible = true;
                }
                else
                {
                    actionBar.setSubtitle( null );
                    item.setTitle( R.string.show_subtitle );
                    isSubtitleVisible = false;
                }
                return true;
            default:
                return super.onOptionsItemSelected( item );
        }
    }

    @TargetApi( Build.VERSION_CODES.HONEYCOMB )
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        View v = super.onCreateView( inflater, container, savedInstanceState );
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB )
        {
            if( isSubtitleVisible )
            {
                getActivity().getActionBar().setSubtitle( R.string.subtitle );
            }
        }
        return v;
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
