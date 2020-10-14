package com.l.fininassignment.network.responseModel

import com.l.fininassignment.network.responseModel.ApiError

class ApiErrorException(val error: ApiError) : Exception()
