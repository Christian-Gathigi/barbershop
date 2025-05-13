package com.example.mybarbershop.model

import java.time.LocalDateTime

data class Hairstyle(
    val id: Int,
    val name: String,
    val durationMinutes: Int
)

data class BarberAppointmentModel(
    var bookingid: String,
    var clientName: String,
    var hairstyle: Hairstyle,
    var startTime: LocalDateTime,
    var endTime: LocalDateTime,
)



