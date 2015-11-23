
package com.example.photogallery.fragment;

import com.example.photogallery.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PhotoPageFragment extends VisibleFragment
{
    protected static final String TAG = "PhotoPageFragment";

    private String mUrl;

    private WebView mWebView;

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setRetainInstance( true );
        mUrl = getActivity().getIntent().getData().toString();
    }

    @SuppressLint( { "SetJavaScriptEnabled", "JavascriptInterface" } )
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        View v = inflater.inflate( R.layout.fragment_photo_page, container, false );
        ProgressBar progressBar = ( ProgressBar ) v.findViewById( R.id.progressBar );
        TextView textView = ( TextView ) v.findViewById( R.id.titleTextView );
        mWebView = ( WebView ) v.findViewById( R.id.webView );
        mWebView.getSettings().setJavaScriptEnabled( true );
        mWebView.setWebViewClient( new OwnWebViewClient() );
        mWebView.setWebChromeClient( new OwnWebChromeClient( progressBar, textView ) );
        mWebView.loadUrl( mUrl );
        return v;
    }

    private class OwnWebViewClient extends WebViewClient
    {
        public OwnWebViewClient()
        {
            super();
        }

        @Override
        public boolean shouldOverrideUrlLoading( WebView view, String url )
        {
            return false;
        }
    }

    private class OwnWebChromeClient extends WebChromeClient
    {
        final ProgressBar mProgressBar;

        final TextView mTextView;

        public OwnWebChromeClient( ProgressBar progressBar, TextView textView )
        {
            mProgressBar = progressBar;
            mProgressBar.setMax( 100 );
            mTextView = textView;
        }

        @Override
        public void onProgressChanged( WebView view, int newProgress )
        {
            if( newProgress == 100 )
            {
                mProgressBar.setVisibility( View.INVISIBLE );
            }
            else
            {
                mProgressBar.setVisibility( View.VISIBLE );
                mProgressBar.setProgress( newProgress );
            }
        }

        @Override
        public void onReceivedTitle( WebView view, String title )
        {
            mTextView.setText( title );
        }
    }
}
