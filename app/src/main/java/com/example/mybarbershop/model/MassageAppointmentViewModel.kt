package com.example.mybarbershop.model

import java.time.LocalDateTime

data class Massage(
    val id: String,
    val name: String,
    val durationMinutes: Int,
    val cost: Double
)

data class MassageBooking(
    val clientName: String,
    val dateTime: LocalDateTime,
    val durationMinutes: Int,
    val massageName: String,
    val cost: Double
)
