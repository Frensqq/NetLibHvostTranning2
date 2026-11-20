package com.example.netlib.data.repository

import com.example.netlib.data.mapper.toDomain
import com.example.netlib.data.mapper.toRequest
import com.example.netlib.data.remote.PBApi
import com.example.netlib.data.storage.TokenStorageImpl
import com.example.netlib.domain.models.Appointment
import com.example.netlib.domain.models.AppointmentList
import com.example.netlib.domain.models.AuthRequest
import com.example.netlib.domain.models.AuthResponse
import com.example.netlib.domain.models.Cancel
import com.example.netlib.domain.models.ClinicList
import com.example.netlib.domain.models.DoctorList
import com.example.netlib.domain.models.NetworkResult
import com.example.netlib.domain.models.RegisterRequest
import com.example.netlib.domain.models.ServiceList
import com.example.netlib.domain.models.Slots
import com.example.netlib.domain.models.UpdateAppointment
import com.example.netlib.domain.models.UploadFiles
import com.example.netlib.domain.models.User
import com.example.netlib.domain.models.UserUpdate
import com.example.netlib.domain.network.Network
import com.example.netlib.domain.repository.AppointmentRepository
import com.example.netlib.domain.repository.AuthRepository
import com.example.netlib.domain.repository.CatalogRepository
import com.example.netlib.domain.repository.UserRepository

internal class AppointmentRepositoryImpl(
    private val api: PBApi,
    network: Network
): PBBaseRepository(network), AppointmentRepository
{
    override suspend fun getSlots(
        doctorId: String,
        date: String
    ): NetworkResult<Slots> {
        return saveApiCall {
            api.getSlots(
                doctorId, date
            ).toDomain()
        }
    }

    override suspend fun getAppointments(
        filter: String?,
        sort: String?,
        page: Int?,
        perPage: Int?
    ): NetworkResult<AppointmentList> {
        return saveApiCall {
            api.getAppointments(
                filter,
                sort,
                page,
                perPage
            ).toDomain()
        }
    }

    override suspend fun postAppointments(data: UpdateAppointment): NetworkResult<Appointment> {
        return saveApiCall {
            api.postAppointments(
                data.toRequest()
            ).toDomain()
        }
    }

    override suspend fun canselAppointment(
        id: String,
        data: Cancel
    ): NetworkResult<Appointment> {
        return saveApiCall {
            api.canselAppointment(
                id,
                data.toRequest()
            ).toDomain()
        }
    }
}