package com.example.netlib.domain.models

data class ApiErrorFields(
    val code: String?,
    val message: String?
)

data class ApiError(
    val status: Int? = null,
    val message: String,
    val data: Map<String , ApiErrorFields>? = null,
)