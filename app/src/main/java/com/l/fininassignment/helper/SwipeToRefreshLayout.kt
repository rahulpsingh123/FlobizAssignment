package com.l.fininassignment.helper

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlin.math.abs


/**
 * Simple wrapper around swipe to refresh layout to allow firing listener when refresh status is changed programmatically.
 */
class SwipeToRefreshLayout(context: Context, attrs: AttributeSet) : SwipeRefreshLayout(context, attrs) {

    private var mTouchSlop = 0
    private var mPrevX = 0f

    var listener: OnRefreshListener? = null

    init {
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    fun setRefreshing(refreshing: Boolean, fireCallback: Boolean) {
        super.setRefreshing(refreshing)
        if (fireCallback) listener?.onRefresh()
    }

    override fun setOnRefreshListener(listener: OnRefreshListener?) {
        super.setOnRefreshListener(listener)
        this.listener = listener
    }

    /*
    https://stackoverflow.com/questions/23989910/horizontalscrollview-inside-swiperefreshlayout
     */
    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                val motionEvent = MotionEvent.obtain(event)
                mPrevX = motionEvent.x
                motionEvent.recycle()
            }
            MotionEvent.ACTION_MOVE -> {
                val eventX: Float = event.x
                val xDiff: Float = abs(eventX - mPrevX)
                if (xDiff > mTouchSlop) {
                    return false
                }
            }
        }

        return super.onInterceptTouchEvent(event)
    }
}