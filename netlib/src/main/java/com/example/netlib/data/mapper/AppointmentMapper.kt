package com.example.netlib.data.mapper

import com.example.netlib.data.remote.DTO.AppointmentDTO
import com.example.netlib.data.remote.DTO.AppointmentListDTO
import com.example.netlib.data.remote.DTO.AppointmentUpdateDTO
import com.example.netlib.data.remote.DTO.CancelDTO
import com.example.netlib.data.remote.DTO.SlotDTO
import com.example.netlib.data.remote.DTO.SlotsDTO
import com.example.netlib.domain.models.Appointment
import com.example.netlib.domain.models.AppointmentList
import com.example.netlib.domain.models.Cancel
import com.example.netlib.domain.models.Slot
import com.example.netlib.domain.models.Slots
import com.example.netlib.domain.models.UpdateAppointment

internal fun SlotDTO.toDomain(): Slot = Slot(
    time = time,
    start = start,
    free = free
)

internal fun SlotsDTO.toDomain(): Slots = Slots(
    doctor = doctor,
    date = date,
    weekday =  weekday,
    working =  working,
    slots = slots.map { it.toDomain() }
)


internal fun AppointmentDTO.toDomain(): Appointment = Appointment(
    id = id, number = number,
    user = user,
    pet = pet,
    doctor = doctor,
    service = service,
    clinic = clinic,
    start = start,
    status = status,
    comment = comment,
    cancelReason = cancelReason,
    created = created,
    updated = updated
)

internal fun AppointmentListDTO.toDomain(): AppointmentList
= AppointmentList(
    page = page,
    perPage = perPage,
    totalPages = totalPages,
    totalItems = totalItems,
    items = items.map { it.toDomain() }
)

internal fun UpdateAppointment.toRequest(): AppointmentUpdateDTO = AppointmentUpdateDTO(
    pet =pet,
    doctor =  doctor,
    service = service,
    start = start,
    comment =  comment
)

internal fun Cancel.toRequest(): CancelDTO =
    CancelDTO(
        reason = reason
    )