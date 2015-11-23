
package com.example.draganddraw.object;

import android.graphics.PointF;

public class Box
{
    private PointF mOrigin;

    private PointF mCurrent;

    public Box( PointF origin )
    {
        mOrigin = origin;
    }

    public void setCurrent( PointF current )
    {
        mCurrent = current;
    }

    public PointF getOrigin()
    {
        return mOrigin;
    }

    public PointF getCurrent()
    {
        return mCurrent;
    }

    public String toString()
    {
        return "Origin x=" + mOrigin.x + ", y=" + mOrigin.y + " Current x=" + mCurrent.x + ", y=" + mCurrent.y;
    }
}