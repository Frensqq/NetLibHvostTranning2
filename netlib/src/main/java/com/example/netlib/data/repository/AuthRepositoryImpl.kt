package com.example.netlib.data.repository

import com.example.netlib.data.mapper.toDomain
import com.example.netlib.data.mapper.toRequest
import com.example.netlib.data.remote.PBApi
import com.example.netlib.data.storage.TokenStorageImpl
import com.example.netlib.domain.models.AuthRequest
import com.example.netlib.domain.models.AuthResponse
import com.example.netlib.domain.models.NetworkResult
import com.example.netlib.domain.models.RegisterRequest
import com.example.netlib.domain.models.User
import com.example.netlib.domain.network.Network
import com.example.netlib.domain.repository.AuthRepository

internal class AuthRepositoryImpl(
    private val api: PBApi,
    private val tokenStorageImpl: TokenStorageImpl,
    network: Network
): PBBaseRepository(network), AuthRepository
{
    override suspend fun register(data: RegisterRequest): NetworkResult<User> {
        return saveApiCall {
            api.register(data.toRequest()).toDomain()
        }
    }

    override suspend fun auth(data: AuthRequest): NetworkResult<AuthResponse> {
        return saveApiCall {
            val result  = api.auth(data.toRequest()).toDomain()

            tokenStorageImpl.saveToken(result.token)

            result
        }
    }

    override suspend fun tokenRefresh(): NetworkResult<AuthResponse> {
        return saveApiCall {
            val result  = api.tokenRefresh().toDomain()

            tokenStorageImpl.saveToken(result.token)

            result
        }
    }
}