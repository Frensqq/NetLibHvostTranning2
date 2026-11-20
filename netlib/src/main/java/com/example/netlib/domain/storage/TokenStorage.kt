package com.example.netlib.domain.storage

/**
 * TokenStorage - список функций хранилища токена
 */
interface TokenStorage {

    fun getToken(): String?

    fun saveToken(token: String?)

}