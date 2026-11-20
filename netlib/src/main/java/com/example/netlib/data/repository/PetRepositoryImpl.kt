package com.example.netlib.data.repository

import com.example.netlib.data.mapper.toDomain
import com.example.netlib.data.mapper.toRequest
import com.example.netlib.data.remote.PBApi
import com.example.netlib.domain.models.NetworkResult
import com.example.netlib.domain.models.Pet
import com.example.netlib.domain.models.PetList
import com.example.netlib.domain.models.PetUpdate
import com.example.netlib.domain.models.UploadFiles
import com.example.netlib.domain.network.Network
import com.example.netlib.domain.repository.PetRepository

internal class PetRepositoryImpl(
    private val api: PBApi,
    network: Network
): PBBaseRepository(network), PetRepository
{
    override suspend fun getPets(
        page: Int?,
        perPage: Int?,
        sort: String?
    ): NetworkResult<PetList> {
        return saveApiCall {
            api.getPets(
                page, perPage, sort
            ).toDomain()
        }
    }

    override suspend fun postPet(
        data: PetUpdate,
        photo: UploadFiles
    ): NetworkResult<Pet> {
        return saveApiCall {
            api.postPet(
                data.toRequest(),
                photo.toRequest()
            ).toDomain()
        }
    }

    override suspend fun patchPet(
        id: String,
        data: PetUpdate,
        photo: UploadFiles
    ): NetworkResult<Pet> {
        return saveApiCall {
            api.patchPet(id,
                data.toRequest(),
                photo.toRequest()
            ).toDomain()
        }
    }

    override suspend fun deletePet(id: String): NetworkResult<Unit> {
        return saveApiCall {
            api.deletePet(id)
        }
    }


}