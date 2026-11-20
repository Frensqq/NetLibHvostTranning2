package com.example.netlib.domain.repository

import com.example.netlib.domain.models.NetworkResult
import com.example.netlib.domain.models.Pet
import com.example.netlib.domain.models.PetList
import com.example.netlib.domain.models.PetUpdate
import com.example.netlib.domain.models.UploadFiles

/**
 *
 * UserRepository - определяет методы для изменение и просмотр данных питомцев
 *
 */
interface PetRepository {

    suspend fun getPets(
        page: Int? = null,
        perPage: Int? = null,
        sort: String? = null,
    ): NetworkResult< PetList>

    suspend fun postPet(
        data: PetUpdate,
        photo: UploadFiles
    ): NetworkResult< Pet>
    suspend fun patchPet(
        id: String,
        data: PetUpdate,
        photo: UploadFiles
    ): NetworkResult< Pet>

    suspend fun deletePet(
        id: String
    ): NetworkResult< Unit>

}