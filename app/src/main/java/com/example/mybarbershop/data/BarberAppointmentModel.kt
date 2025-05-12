package com.example.mybarbershop.data

import android.util.Log

import java.time.LocalDateTime

data class Hairstyle(
    val id: String,
    val name: String,
    val durationMinutes: Int,
    val imageResId: Int
)

data class Booking(
    val id: String,
    val clientName: String,
    val dateTime: LocalDateTime,
    val hairstyleId: String
)

