package com.example.netlib.data.repository

import com.example.netlib.data.mapper.toDomain
import com.example.netlib.data.mapper.toRequest
import com.example.netlib.data.remote.PBApi
import com.example.netlib.domain.models.Appointment
import com.example.netlib.domain.models.AppointmentPeriod
import com.example.netlib.domain.models.AppointmentList
import com.example.netlib.domain.models.Cancel
import com.example.netlib.domain.models.NetworkResult
import com.example.netlib.domain.models.Slots
import com.example.netlib.domain.models.UpdateAppointment
import com.example.netlib.domain.network.Network
import com.example.netlib.domain.repository.AppointmentRepository


/**
 * AppointmentRepositoryImpl - реализация AppointmentRepository
 *
 * @param api - экземпляр api клиента
 * @param network - проверка подключения к интернету
 *
 */
internal class AppointmentRepositoryImpl(
    private val api: PBApi,
    network: Network
): PBBaseRepository(network), AppointmentRepository
{
    override suspend fun getSlots(
        doctorId: String,
        date: String
    ): NetworkResult<Slots> {
        return safeApiCall {
            api.getSlots(
                doctorId, date
            ).toDomain()
        }
    }

    override suspend fun getAppointments(
        filter: String?,
        sort: String?,
        page: Int? ,
        perPage: Int? ,
    ): NetworkResult<AppointmentList> {
        return safeApiCall {
            api.getAppointments(
                filter,
                sort,
                page,
                perPage
            ).toDomain()
        }
    }

    override suspend fun postAppointments(data: UpdateAppointment): NetworkResult<Appointment> {
        return safeApiCall {
            api.postAppointments(
                data.toRequest()
            ).toDomain()
        }
    }

    override suspend fun canselAppointment(
        id: String,
        data: Cancel
    ): NetworkResult<Appointment> {
        return safeApiCall {
            api.canselAppointment(
                id,
                data.toRequest()
            ).toDomain()
        }
    }

    override suspend fun appointmentsFor(
        period: AppointmentPeriod, page: Int, perPage: Int
    ): NetworkResult<AppointmentList> = when (period) {
        AppointmentPeriod.UPCOMING ->
            getAppointments("(status='pending' || status='confirmed')", "start", page, perPage)
        AppointmentPeriod.PAST ->
            getAppointments("(status='done' || status='cancelled')", "-start", page, perPage)
    }
}
