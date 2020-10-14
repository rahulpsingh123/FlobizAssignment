package com.l.fininassignment.network.responseModel

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

data class DataResponse(
    var page: Int = 0,
    var data: List<UserData>? = null
)

open class UserData : RealmObject() {
    @PrimaryKey
    var id : Int = 0
    var email: String = ""
    var first_name: String = ""
    var last_name: String = ""
    var avatar: String = ""
}



