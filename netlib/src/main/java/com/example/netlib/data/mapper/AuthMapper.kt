package com.example.netlib.data.mapper

import com.example.netlib.data.remote.DTO.AuthRequestDTO
import com.example.netlib.data.remote.DTO.AuthResponseDTO
import com.example.netlib.data.remote.DTO.RegistrationDTO
import com.example.netlib.data.remote.DTO.UserDTO
import com.example.netlib.domain.models.AuthRequest
import com.example.netlib.domain.models.AuthResponse
import com.example.netlib.domain.models.RegisterRequest
import com.example.netlib.domain.models.User

internal fun RegisterRequest.toRequest(): RegistrationDTO =
    RegistrationDTO(
        email =email,
        name = name,
        phone = phone,
        password = password,
        passwordConfirm = passwordConfirm
    )

internal fun UserDTO.toDomain(): User =
    User(
        id = id,
        email = email,
        name = name,
        phone = phone,
        avatar = avatar,
        verified = verified,
        created = created,
        updated = updated
    )


internal fun AuthResponseDTO.toDomain(): AuthResponse =
    AuthResponse(
        token = token,
        record = record.toDomain()
    )

internal fun AuthRequest.toRequest(): AuthRequestDTO =
    AuthRequestDTO(
        identity = identity,
        password = password
    )