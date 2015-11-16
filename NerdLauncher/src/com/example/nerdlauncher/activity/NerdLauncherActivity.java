
package com.example.nerdlauncher.activity;

import com.example.nerdlauncher.fragment.NerdLauncherFragment;

import android.support.v4.app.Fragment;

public class NerdLauncherActivity extends SingleFragmentActivity
{

    @Override
    protected Fragment createFragment()
    {
        return new NerdLauncherFragment();
    }

}
