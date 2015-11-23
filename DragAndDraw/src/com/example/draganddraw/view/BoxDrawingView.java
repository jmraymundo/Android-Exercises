
package com.example.draganddraw.view;

import java.util.ArrayList;

import com.example.draganddraw.object.Box;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

@SuppressLint( "ClickableViewAccessibility" )
public class BoxDrawingView extends View
{
    private static final String TAG = "BoxDrawingView";

    private Box mCurrentBox;

    private ArrayList< Box > mBoxes = new ArrayList< Box >();

    private Paint mBoxPaint;

    private Paint mBackgroundPaint;

    public BoxDrawingView( Context context )
    {
        super( context, null );
    }

    public BoxDrawingView( Context context, AttributeSet attrs )
    {
        super( context, attrs );
        mBoxPaint = new Paint();
        mBoxPaint.setColor( 0x22ff0000 );
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor( 0xfff8efe0 );
    }

    @Override
    public boolean onTouchEvent( MotionEvent event )
    {
        PointF curr = new PointF( event.getX(), event.getY() );
        Log.i( TAG, "Received event at x=" + curr.x + ", y=" + curr.y + ": " );
        switch( event.getAction() )
        {
            case MotionEvent.ACTION_DOWN:
                Log.i( TAG, "ACTION_DOWN" );
                mCurrentBox = new Box( curr );
                mBoxes.add( mCurrentBox );
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i( TAG, "ACTION_MOVE" );
                if( mCurrentBox != null )
                {
                    mCurrentBox.setCurrent( curr );
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                Log.i( TAG, "ACTION_UP" );
                mCurrentBox = null;
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.i( TAG, "ACTION_CANCEL" );
                mCurrentBox = null;
                break;
        }
        return true;
    }

    @Override
    protected void onDraw( Canvas canvas )
    {
        canvas.drawPaint( mBackgroundPaint );
        for( Box box : mBoxes )
        {
            float left = Math.min( box.getOrigin().x, box.getCurrent().x );
            float right = Math.max( box.getOrigin().x, box.getCurrent().x );
            float top = Math.min( box.getOrigin().y, box.getCurrent().y );
            float bottom = Math.max( box.getOrigin().y, box.getCurrent().y );

            canvas.drawRect( left, top, right, bottom, mBoxPaint );
        }
    }

    @Override
    public Parcelable onSaveInstanceState()
    {
        Parcelable instanceState = super.onSaveInstanceState();
        MySavedState saveState = new MySavedState( instanceState );
        saveState.setBoxes( mBoxes );
        return saveState;
    }

    @Override
    public void onRestoreInstanceState( Parcelable bundle )
    {
        if( bundle instanceof MySavedState )
        {
            mBoxes = ( ( MySavedState ) bundle ).getBoxes();
        }
        super.onRestoreInstanceState( bundle );
    }

    private class MySavedState extends BaseSavedState
    {
        private ArrayList< Box > mSavedBoxes = new ArrayList< Box >();

        public MySavedState( Parcelable superState )
        {
            super( superState );
        }

        public ArrayList< Box > getBoxes()
        {
            return mSavedBoxes;
        }

        public void setBoxes( ArrayList< Box > boxes )
        {
            mSavedBoxes = boxes;
        }
    }
}
