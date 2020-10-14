package com.l.fininassignment.helper

import androidx.core.view.children
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import com.l.fininassignment.MainActivity
import timber.log.Timber

/**
 * Base recycler view adapter for easy access to common things like
 * 1. recycler view instance
 * 2. last item implementation
 *
 * Also optional connection to Lifecycle if fragment is provided.
 * Clients get access to clearHolders method which is invoked on lifecycle's onStart and onStop methods.
 */

@Suppress("LeakingThis")
abstract class BaseAdapter<T : RecyclerView.ViewHolder?>(private var activity: MainActivity? = null) : RecyclerView.Adapter<T>(), LifecycleObserver {

    protected var rv: RecyclerView? = null

    //common functionality
    protected val TYPE_LAST_ITEM = 999
    open var noMoreData: Boolean = false
        set(value) {
            field = value
            notifyItemChanged(itemCount - 1)
        }

    init {
        activity?.lifecycle?.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onStart() {
        Timber.d("onResume")
        notifyDataSetChanged()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun onStop() {
        Timber.d("onPause")
        rv?.children
                ?.mapIndexed { _, view -> rv?.getChildViewHolder(view) }
                ?.filterNotNull()
                ?.forEach { clearHolder(it as T) }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    open fun onPause() {
        Timber.d("onPause")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    open fun onResume() {
        Timber.d("onResume")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onDestroy() {
        Timber.d("onDestroy")
        activity = null
        rv = null
    }

    /**
     * Clients can override this method to clear of resources for all view holders available
     * in recycler view at the moment
     * Helpful to release glide resources
     */
    open fun clearHolder(holder: T) {
        Timber.d("clearHolder clearing $holder at position ${holder?.adapterPosition}")
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        rv = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        rv = null
    }

    protected fun isValidPos(position: Int): Boolean {
        return position != -1 && position < itemCount
    }
}