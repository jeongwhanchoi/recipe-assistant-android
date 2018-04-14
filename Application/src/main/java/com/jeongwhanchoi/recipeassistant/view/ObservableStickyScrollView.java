package com.jeongwhanchoi.recipeassistant.view;

import android.content.Context;
import android.util.AttributeSet;

import com.emilsjolander.components.StickyScrollViewItems.StickyScrollView;

public class ObservableStickyScrollView extends StickyScrollView{

    private OnScrollViewListener mOnScrollViewListener = null;


    public interface OnScrollViewListener
    {
        void onScrollChanged(ObservableStickyScrollView scrollView, int x, int y, int oldx, int oldy);
    }


    public ObservableStickyScrollView(Context context)
    {
        super(context);
    }


    public ObservableStickyScrollView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }


    public ObservableStickyScrollView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }


    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy)
    {
        super.onScrollChanged(x, y, oldx, oldy);
        if(mOnScrollViewListener != null)
        {
            mOnScrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }


    public void setOnScrollViewListener(OnScrollViewListener onScrollViewListener)
    {
        this.mOnScrollViewListener = onScrollViewListener;
    }
}
