package com.crysoft.me.autobot.helpers;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.crysoft.me.autobot.R;

/**
 * Created by Maxx on 8/3/2016.
 */
public class CustomViewPager extends ViewPager {

    private boolean mSwipable = false;

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomViewPager);
        try {
            mSwipable = a.getBoolean(R.styleable.CustomViewPager_swipeable, false);
        } finally {
            a.recycle();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mSwipable ? super.onInterceptTouchEvent(event) : false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mSwipable ? super.onTouchEvent(event) : false;
    }

    public boolean isSwipable() {
        return mSwipable;
    }

    public void setSwipable(boolean swipable) {
        mSwipable = swipable;
    }
}