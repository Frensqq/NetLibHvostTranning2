package com.example.netlib.domain.repository

import com.example.netlib.domain.models.NetworkResult
import com.example.netlib.domain.models.UploadFiles
import com.example.netlib.domain.models.User
import com.example.netlib.domain.models.UserUpdate


interface UserRepository {

    suspend fun getUser(
        userId: String
    ): NetworkResult< User>

    suspend fun patchUser(
        userId: String,
        data: UserUpdate,
        avatar: UploadFiles
    ): NetworkResult< User>

}