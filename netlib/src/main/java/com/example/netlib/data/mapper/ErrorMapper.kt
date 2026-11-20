package com.example.netlib.data.mapper

import com.example.netlib.data.remote.DTO.ApiErrorDTO
import com.example.netlib.domain.models.ApiError
import com.example.netlib.domain.models.ApiErrorFields

/**
 * Преобразование ApiErrorDTO -> ApiError
 */
internal fun ApiErrorDTO.toDomain(): ApiError =
    ApiError(
        status = status,
        message = message,
        data = data.mapValues { (_, error) ->
            ApiErrorFields(
                code = error.code,
                message = error.message
            )
        }
    )