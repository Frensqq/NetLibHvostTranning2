package com.example.netlib.data.repository

import com.example.netlib.data.mapper.toDomain
import com.example.netlib.data.remote.PBApi
import com.example.netlib.domain.models.ClinicList
import com.example.netlib.domain.models.DoctorList
import com.example.netlib.domain.models.NetworkResult
import com.example.netlib.domain.models.ServiceList
import com.example.netlib.domain.network.Network
import com.example.netlib.domain.repository.CatalogRepository

/**
 * CatalogRepositoryImpl - реализация CatalogRepository
 *
 * @param api - экземпляр api клиента
 * @param network - проверка подключения к интернету
 *
 */
internal class CatalogRepositoryImpl(
    private val api: PBApi,
    network: Network
): PBBaseRepository(network), CatalogRepository
{
    override suspend fun getService(
        page: Int?,
        perPage: Int?,
        sort: String?
    ): NetworkResult<ServiceList> {
        return safeApiCall {
            api.getService(
                page,
                perPage,
                sort,
            ).toDomain()
        }
    }

    override suspend fun getClinics(
        page: Int?,
        perPage: Int?,
        sort: String?
    ): NetworkResult<ClinicList> {
        return safeApiCall {
            api.getClinics(
                page,
                perPage,
                sort
            ).toDomain()
        }
    }

    override suspend fun getDoctors(
        page: Int?,
        perPage: Int?,
        sort: String?,
        filter: String?
    ): NetworkResult<DoctorList> {
        return safeApiCall {
            api.getDoctors(
                page,
                perPage,
                sort,
                filter
            ).toDomain()
        }
    }

    override suspend fun doctorsForService(
        serviceId: String, page: Int, perPage: Int
    ): NetworkResult<DoctorList> {
        require(serviceId.matches(Regex("[a-zA-Z0-9]+"))) { "Invalid service ID" }
        return getDoctors(page, perPage, "name", "services~'$serviceId'")
    }
}
