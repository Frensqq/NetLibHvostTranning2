package com.example.netlib.domain.models

import java.sql.Date

sealed class NetworkResult<T> {

    data class Success<T>(
        val date: T
    ): NetworkResult<T>()

    data class Error(
        val errorResponse: ApiError
    ): NetworkResult<Nothing>()

    object NoInternet: NetworkResult<Nothing>()

    object Timeout: NetworkResult<Nothing>()


}