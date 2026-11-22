package com.example.netlib.domain.repository

import com.example.netlib.domain.models.Appointment
import com.example.netlib.domain.models.AppointmentPeriod
import com.example.netlib.domain.models.AppointmentList
import com.example.netlib.domain.models.Cancel
import com.example.netlib.domain.models.NetworkResult
import com.example.netlib.domain.models.Slots
import com.example.netlib.domain.models.UpdateAppointment

/**
 *
 * CatalogRepository - определяет методы для записи
 *
 */
interface AppointmentRepository {

    suspend fun getSlots(doctorId: String, date: String): NetworkResult< Slots>

    suspend fun getAppointments(
        filter: String? = null,
        sort: String? = null,
        page: Int? = 1,
        perPage: Int? = 20
    ): NetworkResult< AppointmentList>

    suspend fun postAppointments(
        data: UpdateAppointment
    ): NetworkResult< Appointment>



    suspend fun canselAppointment(
        id: String,
        data: Cancel, ): NetworkResult< Appointment>

    suspend fun appointmentsFor(
        period: AppointmentPeriod, page: Int = 1, perPage: Int = 20
    ): NetworkResult<AppointmentList>

    suspend fun cancelAppointment(id: String, data: Cancel): NetworkResult<Appointment> =
        canselAppointment(id, data)
}
