package com.example.netlib.data.mapper

import android.R.attr.name
import com.example.netlib.data.remote.DTO.FileUploadDTO
import com.example.netlib.domain.models.UploadFiles


/**
 * Преобразование UploadFiles -> FileUploadDTO
 */
internal fun UploadFiles.toRequest() =
    FileUploadDTO(
        bytes = bytes,
        name = name,
        mimeType = mimeType
    )