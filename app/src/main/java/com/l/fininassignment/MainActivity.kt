package com.l.fininassignment

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.l.fininassignment.helper.*
import com.l.fininassignment.viewModel.DataViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    lateinit var model: DataViewModel
    private var adapter = MainAdapter(this)
    private val subscriptions = CompositeDisposable()

    enum class UiState { LOADING, LOADED, ERROR, EMPTY }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        model = ViewModelProvider(this).get(DataViewModel::class.java)
        setUpSearchViews()
        setState(UiState.LOADING)
        fetchData()
        initializeVideoView()
    }

    private fun initializeVideoView() {
        if (FlobizApplication.pref.getBoolean("is_closed", false)) {
            flLayout.gone()
        } else {
            videoView.setVideoURI(Uri.parse(Constants.VIDEO_URL))
            videoView.requestFocus()
            videoView.start()
        }

        cross.click {
            flLayout.gone()
            FlobizApplication.pref.put("is_closed" to true)
        }
    }


    private fun setUpSearchViews() {
        shimmer_recycler_view?.layoutManager = LinearLayoutManager(this)
        shimmer_recycler_view.adapter = adapter
        shimmer_recycler_view.addItemDecoration(CardPaddingItemDecoration(this))
    }

    private fun fetchData(clearList: Boolean = false, paginated: Boolean = false) {
        if (isConnectedToInternet().not()) {
            setState(UiState.ERROR)
            showNoNetworkSnack()
            return
        }
        adapter.noMoreData = true
        model.getData()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                if (it?.isEmpty() == true) {
                    if (adapter.itemCount == 0) setState(UiState.EMPTY)
                } else {
                    setState(UiState.LOADED)
                    it?.let { list -> adapter.setData(list, clearList) }
                }
            }, {
                setState(UiState.ERROR)
            })?.let { subscriptions.add(it) }
    }


    private fun setState(uiState: UiState) {
        when (uiState) {
            UiState.LOADING -> {
                shimmer_recycler_view.show()
                shimmer_recycler_view.showShimmerAdapter()
            }
            UiState.LOADED -> {
                shimmer_recycler_view.show()
                shimmer_recycler_view.hideShimmerAdapter()
            }
            UiState.EMPTY -> {
                shimmer_recycler_view.hideShimmerAdapter()
                shimmer_recycler_view.gone()
            }
            UiState.ERROR -> {
                shimmer_recycler_view.hideShimmerAdapter()
                shimmer_recycler_view.gone()

            }
        }
    }

    private fun isConnectedToInternet(): Boolean {
        val cm =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        val netInfo = cm?.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }

    private fun showNoNetworkSnack() {
        val snackBar = Snackbar
            .make(main_layout, string(R.string.no_internet_connection), Snackbar.LENGTH_LONG)
            .setAction(string(R.string.text_retry)) {
                fetchData(clearList = true, paginated = false)
            }
        val textView =
            snackBar.view.findViewById<View>(com.google.android.material.R.id.snackbar_action) as? TextView
        textView?.setTextColor(Color.YELLOW)
        snackBar.show()
    }

    override fun getCurrentFocus(): View? {
        return if (window != null) window.currentFocus else null
    }


    override fun onDestroy() {
        super.onDestroy()
        subscriptions.clear()
    }

}