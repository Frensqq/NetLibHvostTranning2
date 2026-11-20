package com.example.netlib.data.mapper

import com.example.netlib.data.remote.DTO.PatchUserDTO
import com.example.netlib.domain.models.UserUpdate

/**
 * Преобразование UserUpdate -> PatchUserDTO
 */
internal fun UserUpdate.toRequest(): PatchUserDTO =
    PatchUserDTO(
        name = name,
        phone = phone
    )