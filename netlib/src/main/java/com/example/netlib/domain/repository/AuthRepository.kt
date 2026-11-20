package com.example.netlib.domain.repository

import com.example.netlib.domain.models.AuthRequest
import com.example.netlib.domain.models.AuthResponse
import com.example.netlib.domain.models.NetworkResult
import com.example.netlib.domain.models.RegisterRequest
import com.example.netlib.domain.models.User

/**
 *
 * CatalogRepository - определяет методы для аутентификации
 *
 */
interface AuthRepository {

    suspend fun register(
        data:  RegisterRequest
    ): NetworkResult< User>

    suspend fun auth(
        data:  AuthRequest
    ): NetworkResult< AuthResponse>

    suspend fun tokenRefresh() : NetworkResult< AuthResponse>

}