package com.example.netlib.domain.models

data class UploadFiles(
    val bytes: ByteArray,
    val name: String,
    val mimeType: String = "image/jpeg"
)