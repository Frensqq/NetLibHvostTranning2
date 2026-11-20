package com.example.netlib

import com.example.netlib.domain.storage.TokenStorage

class FakeTokenImpl(): TokenStorage {

    private var savedToken: String? = null

    override fun getToken(): String? {
        return savedToken
    }

    override fun saveToken(token: String?) {
        savedToken = token
    }


}