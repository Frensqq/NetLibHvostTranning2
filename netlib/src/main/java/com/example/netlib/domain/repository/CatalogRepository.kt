package com.example.netlib.domain.repository

import com.example.netlib.domain.models.ClinicList
import com.example.netlib.domain.models.DoctorList
import com.example.netlib.domain.models.NetworkResult
import com.example.netlib.domain.models.ServiceList

/**
 *
 * CatalogRepository - определяет методы для изменение и просмотр каталогов
 *
 */
interface CatalogRepository {

    suspend fun getService(
        page: Int? = null,
        perPage: Int? = null,
        sort: String? = null,
    ): NetworkResult< ServiceList>

    suspend fun getClinics(
        page: Int? = null,
        perPage: Int? = null,
        sort: String? = null,
    ): NetworkResult< ClinicList>

    suspend fun getDoctors(
        page: Int? = null,
        perPage: Int? = null,
        sort: String? = "-clinic",
        filter: String? = null,
    ): NetworkResult< DoctorList>

    suspend fun doctorsForService(
        serviceId: String, page: Int = 1, perPage: Int = 20
    ): NetworkResult<DoctorList>
}
