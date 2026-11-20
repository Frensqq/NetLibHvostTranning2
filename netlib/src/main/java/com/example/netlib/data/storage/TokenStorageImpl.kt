package com.example.netlib.data.storage

import android.content.Context
import com.example.netlib.domain.storage.TokenStorage

class TokenStorageImpl(private val context: Context): TokenStorage {

    protected val preferences = context.getSharedPreferences("net_lib_hvost", Context.MODE_PRIVATE)

    override fun getToken(): String? {
        return preferences.getString("token_auth", null)
    }

    override fun saveToken(token: String?) {
        if (token == null) preferences.edit().remove("token_auth")
        else preferences.edit().putString("token_auth", token)
    }


}