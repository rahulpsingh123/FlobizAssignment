package com.l.fininassignment.viewModel

import androidx.lifecycle.ViewModel
import com.l.fininassignment.FlobizApplication
import com.l.fininassignment.gson
import com.l.fininassignment.helper.Constants
import com.l.fininassignment.helper.isNullString
import com.l.fininassignment.network.responseModel.DataResponse
import com.l.fininassignment.network.responseModel.ErrorNoMoreData
import com.l.fininassignment.network.responseModel.QueryAlreadyInProgress
import com.l.fininassignment.network.responseModel.UserData
import com.l.fininassignment.repo.NewsRepo
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.realm.Realm


class DataViewModel : ViewModel() {
    private val repo = NewsRepo()

    var endCursor: String? = Constants.EMPTY_STRING
    private var queryInProgress: Boolean = false

    fun getData(): Single<List<UserData>?>? {
        return when {
            queryInProgress -> Single.error(QueryAlreadyInProgress())
            endCursor.isNullString() -> Single.error(ErrorNoMoreData())
            else -> {
                val patch = mapOf("per_page" to 25)
                repo.getPaginationData(patch)
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.map {
                        val response = gson.fromJson(it, DataResponse::class.java)
                        response.data
                    }
                    ?.doOnSubscribe { queryInProgress = true }
                    ?.doFinally { queryInProgress = false }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        repo.clear()
    }
}