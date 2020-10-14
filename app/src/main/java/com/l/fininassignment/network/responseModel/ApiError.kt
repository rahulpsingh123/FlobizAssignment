package com.l.fininassignment.network.responseModel

class ApiError {
    val message: String? = ""
    val code: Int? = null
    val name: String? = ""
    val path: List<String>? = listOf()
}

class NullResponse : Exception()
class QueryAlreadyInProgress : Exception()
class ErrorNoMoreData : Exception()
