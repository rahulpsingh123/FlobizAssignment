package com.l.fininassignment.repo

import com.l.fininassignment.network.APINewsManager
import com.l.fininassignment.network.NetworkClient
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class NewsRepo : BaseRepo() {

    fun getPaginationData(patch: Map<String, Int>): Single<String>? {
        return APINewsManager.instance?.getPaginationData(patch)?.let {
            NetworkClient
                .getResult(it)
                .subscribeOn(Schedulers.io())
        }
    }
}