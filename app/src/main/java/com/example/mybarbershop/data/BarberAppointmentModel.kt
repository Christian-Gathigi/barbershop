package com.example.mybarbershop.data

import java.time.LocalDateTime
import java.util.UUID

data class Hairstyle(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val durationMinutes: Int
)

data class Barber(
    val id: String = UUID.randomUUID().toString(),
    val name: String
)

data class Booking(
    val id: String = UUID.randomUUID().toString(),
    val clientName: String,
    val dateTime: LocalDateTime,
    val barberId: String,
    val hairstyleId: String
)

