package com.example.netlib.domain.models

import io.ktor.websocket.CloseReason

data class Slot(
    val time: String,
val start: String,
val free: Boolean,
)

data class Slots(
    val doctor: String,
val date: String,
val weekday: Int,
val working: Boolean,
val slots: String,
)

data class AppointmentExpand(
    val pet: Pet? = null,
    val doctor: Doctor? = null,
    val service: Service? = null,
    val clinic: Clinic? = null,
)

data class Appointment(
    val id: String,
val number: Int,
val user: String,
val pet: String,
val doctor: String,
val service: String,
val clinic: String,
val start: String,
val status: String,
val comment: String,
val cancelReason: String,
val created: String,
val updated: String,
    val expand: AppointmentExpand? = null
)

data class UpdateAppointment(
    val pet: String,
val doctor: String,
val service: String,
val start: String,
val comment: String,

)


data class AppointmentList(
    val page: Int,
    val perPage: Int,
    val totalPages: Int,
    val totalItems: Int,
    val items: List<Appointment>,
)

data class Cancel(
    val reason: String
)