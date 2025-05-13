package com.example.mybarbershop.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.mybarbershop.model.Massage
import com.example.mybarbershop.model.MassageBooking

import java.time.LocalDateTime

class MassageBookingViewModel : ViewModel() {

    // Sample massage types
    val massageTypes = listOf(
        Massage(id = "1", name = "Swedish Massage", durationMinutes = 60, cost = 50.0),
        Massage(id = "2", name = "Deep Tissue Massage", durationMinutes = 90, cost = 70.0),
        Massage(id = "3", name = "Aromatherapy Massage", durationMinutes = 45, cost = 40.0)
    )

    // In-memory booking list (replace with Firebase or Room in real apps)
    private val bookings = mutableListOf<MassageBooking>()

    fun getAllBookings(): List<MassageBooking> = bookings

    // Check availability for a new massage booking
    @RequiresApi(Build.VERSION_CODES.O)
    fun isTimeAvailable(requestedTime: LocalDateTime, duration: Int): Boolean {
        val requestedEndTime = requestedTime.plusMinutes(duration.toLong())
        return bookings.none {
            val existingStart = it.dateTime
            val existingEnd = it.dateTime.plusMinutes(it.durationMinutes.toLong())
            requestedTime < existingEnd && requestedEndTime > existingStart
        }
    }

    // Add booking if time is available
    @RequiresApi(Build.VERSION_CODES.O)
    fun createMassageBooking(name: String, time: LocalDateTime, massage: Massage): Boolean {
        if (!isTimeAvailable(time, massage.durationMinutes)) {
            return false
        }
        val booking = MassageBooking(
            clientName = name,
            dateTime = time,
            durationMinutes = massage.durationMinutes,
            massageName = massage.name,
            cost = massage.cost
        )
        bookings.add(booking)
        return true
    }
}
