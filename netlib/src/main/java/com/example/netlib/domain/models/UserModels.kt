package com.example.netlib.domain.models

import com.example.netlib.domain.storage.TokenStorage

data class RegisterRequest(
    val email: String,
val password: String,
val passwordConfirm: String,
val name: String,
val phone: String,
)

data class User(
    val id: String,
val email: String,
val name: String,
val phone: String,
val avatar: String,
val verified: Boolean,
val created: String,
val updated: String,
)

data class AuthRequest(
    val identity: String,
val password: String,
)

data class AuthResponse(
    val token: String,
val record: User,
)

data class UserUpdate(
    val name: String? = null,
    val phone: String? = null,
)
