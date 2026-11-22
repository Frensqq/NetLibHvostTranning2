package com.example.netlib.data.repository

import com.example.netlib.data.mapper.toDomain
import com.example.netlib.data.mapper.toRequest
import com.example.netlib.data.remote.PBApi
import com.example.netlib.data.storage.TokenStorageImpl
import com.example.netlib.domain.models.AuthRequest
import com.example.netlib.domain.models.AuthResponse
import com.example.netlib.domain.models.NetworkResult
import com.example.netlib.domain.models.RegisterRequest
import com.example.netlib.domain.models.UploadFiles
import com.example.netlib.domain.models.User
import com.example.netlib.domain.models.UserUpdate
import com.example.netlib.domain.network.Network
import com.example.netlib.domain.repository.AuthRepository
import com.example.netlib.domain.repository.UserRepository

/**
 * UserRepositoryImpl - реализация UserRepository
 *
 * @param api - экземпляр api клиента
 * @param network - проверка подключения к интернету
 *
 */
internal class UserRepositoryImpl(
    private val api: PBApi,
    network: Network
): PBBaseRepository(network), UserRepository
{
    override suspend fun getUser(userId: String): NetworkResult<User> {
        return safeApiCall { api.getUser(userId).toDomain() }
    }

    override suspend fun patchUser(
        userId: String,
        data: UserUpdate,
        avatar: UploadFiles?
    ): NetworkResult<User> {
        return safeApiCall { api.patchUser(
            userId,
            data.toRequest(),
            avatar?.toRequest()
        ).toDomain() }
    }

}