
package com.example.photogallery.fragment;

import com.example.photogallery.services.PollService;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

public class VisibleFragment extends Fragment
{
    protected static final String TAG = "VisibleFragment";

    private BroadcastReceiver mOnShowNotification = new BroadcastReceiver()
    {
        @Override
        public void onReceive( Context context, Intent intent )
        {
            Log.i( TAG, "cancelling notification" );
            setResultCode( Activity.RESULT_CANCELED );
        }
    };

    @Override
    public void onPause()
    {
        super.onPause();
        getActivity().unregisterReceiver( mOnShowNotification );
    };

    @Override
    public void onResume()
    {
        super.onResume();
        IntentFilter filter = new IntentFilter( PollService.ACTION_SHOW_NOTIFICATION );
        getActivity().registerReceiver( mOnShowNotification, filter, PollService.PERMISSION_PRIVATE, null );
    }
}
