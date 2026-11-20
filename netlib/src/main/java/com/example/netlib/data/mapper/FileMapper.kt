package com.example.netlib.data.mapper

import android.R.attr.name
import com.example.netlib.data.remote.DTO.FileUploadDTO
import com.example.netlib.domain.models.UploadFiles


/**
 * Перевод из FileUpload в FileUploadDTO
 */
internal fun UploadFiles.toRequest() =
    FileUploadDTO(
        bytes = bytes,
        name = name,
        mimeType = mimeType
    )