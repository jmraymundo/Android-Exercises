
package com.example.runtracker.fragment;

import com.example.runtracker.R;
import com.example.runtracker.activity.RunActivity;
import com.example.runtracker.database.RunDatabaseHelper.RunCursor;
import com.example.runtracker.manager.RunManager;
import com.example.runtracker.object.Run;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class RunListFragment extends ListFragment
{

    public static final int REQUEST_NEW_RUN = 0;

    private static final String TAG = "RunListFragment";

    private RunCursor mCursor;

    @Override
    public void onActivityResult( int requestCode, int resultCode, Intent data )
    {
        switch( requestCode )
        {
            case REQUEST_NEW_RUN:
                mCursor.requery();
                ( ( RunCursorAdapter ) getListAdapter() ).notifyDataSetChanged();
                break;
            default:
                super.onActivityResult( requestCode, resultCode, data );
        }
    }

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setHasOptionsMenu( true );
        mCursor = RunManager.get( getActivity() ).queryRuns();
        try
        {
            RunCursorAdapter adapter = new RunCursorAdapter( getActivity(), mCursor );
            setListAdapter( adapter );
        }
        catch( IllegalArgumentException e )
        {
            Log.e( TAG, "Exception caught! " + e );
        }
    }

    @Override
    public void onCreateOptionsMenu( Menu menu, MenuInflater inflater )
    {
        super.onCreateOptionsMenu( menu, inflater );
        inflater.inflate( R.menu.run_list_options, menu );
    }

    @Override
    public void onDestroy()
    {
        mCursor.close();
        super.onDestroy();
    }

    @Override
    public void onListItemClick( ListView l, View v, int position, long id )
    {
        Intent i = new Intent( getActivity(), RunActivity.class );
        i.putExtra( RunActivity.EXTRA_RUN_ID, id );
        startActivity( i );
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        switch( item.getItemId() )
        {
            case R.id.menu_item_new_run:
                Intent i = new Intent( getActivity(), RunActivity.class );
                startActivityForResult( i, REQUEST_NEW_RUN );
                return true;
            default:
                return super.onOptionsItemSelected( item );
        }
    }

    public class RunCursorAdapter extends CursorAdapter
    {

        private RunCursor mRunCursor;

        public RunCursorAdapter( Context context, RunCursor cursor )
        {
            super( context, cursor, 0 );
            mRunCursor = cursor;
        }

        @Override
        public void bindView( View view, Context context, Cursor cursor )
        {
            Run run = mRunCursor.getRun();
            TextView startDateView = ( TextView ) view;
            String cellText = context.getString( R.string.cell_text, run.getStartDate() );
            startDateView.setText( cellText );
        }

        @Override
        public View newView( Context context, Cursor cursor, ViewGroup parent )
        {
            LayoutInflater inflater = ( LayoutInflater ) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            return inflater.inflate( android.R.layout.simple_list_item_1, parent, false );
        }

    }
}
