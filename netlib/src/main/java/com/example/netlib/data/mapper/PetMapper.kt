package com.example.netlib.data.mapper

import com.example.netlib.data.remote.DTO.PetDTO
import com.example.netlib.data.remote.DTO.PetListDTO
import com.example.netlib.data.remote.DTO.PetUpdateDTO
import com.example.netlib.domain.models.Pet
import com.example.netlib.domain.models.PetList
import com.example.netlib.domain.models.PetUpdate

/**
 * Преобразование PetDTO -> Pet
 */
internal fun PetDTO.toDomain(): Pet =
    Pet(
        id = id,
        owner = owner,
        name = name,
        species = species,
        breed = breed,
        birthDate = birthDate,
        weightKg = weightKg,
        photo = photo,
        created = created,
        updated = updated
    )

/**
 * Преобразование PetListDTO -> PetList
 */
internal fun PetListDTO.toDomain(): PetList =
    PetList(
        page = page,
        perPage =  perPage,
        totalPages = totalPages
        , totalItems = totalItems,
        items = items.map { it.toDomain() }
    )

/**
 * Преобразование PetUpdate -> PetUpdateDTO
 */
internal fun PetUpdate.toRequest(): PetUpdateDTO =
    PetUpdateDTO(
        name = name,
        species = species,
        breed = breed,
        birthDate = birthDate,
        weightKg = weightKg
    )