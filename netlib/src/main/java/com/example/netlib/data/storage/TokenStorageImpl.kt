package com.example.netlib.data.storage

import android.content.Context
import com.example.netlib.domain.storage.TokenStorage

/**
 * Реализация хранилища токена
 *
 * @param context -Android context
 *
 */
internal class TokenStorageImpl(context: Context): TokenStorage {

    private val preferences = context.applicationContext.getSharedPreferences("net_lib_hvost", Context.MODE_PRIVATE)

    override fun getToken(): String? {
        return preferences.getString("token_auth", null)
    }

    override fun saveToken(token: String?) {
        preferences.edit().putString("token_auth", token).apply()
    }


}