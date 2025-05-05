package com.example.mybarbershop.models

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.mybarbershop.data.BarberAppointmentModel.Barber
import com.example.mybarbershop.data.BarberAppointmentModel.Booking
import com.example.mybarbershop.data.BarberAppointmentModel.Hairstyle
import java.time.LocalDateTime

class BarberAppointmentViewModel : ViewModel() {

    private val _barbers = mutableStateListOf<Barber>()
    val barbers: List<Barber> get() = _barbers

    private val _hairstyles = mutableStateListOf<Hairstyle>()
    val hairstyles: List<Hairstyle> get() = _hairstyles

    private val _bookings = mutableStateListOf<Booking>()
    val bookings: List<Booking> get() = _bookings

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        _barbers.addAll(
            listOf(
                Barber(name = "John"),
                Barber(name = "Alex")
            )
        )
        _hairstyles.addAll(
            listOf(
                Hairstyle(name = "Fade", durationMinutes = 60),
                Hairstyle(name = "Buzz Cut", durationMinutes = 30)
            )
        )
    }

    fun getAvailableBarbers(desiredTime: LocalDateTime, hairstyle: Hairstyle): List<Barber> {
        val desiredEndTime = desiredTime.plusMinutes(hairstyle.durationMinutes.toLong())

        return _barbers.filter { barber ->
            _bookings.none { booking ->
                booking.barberId == barber.id &&
                        booking.dateTime < desiredEndTime &&
                        booking.dateTime.plusMinutes(
                            _hairstyles.first { it.id == booking.hairstyleId }.durationMinutes.toLong()
                        ) > desiredTime
            }
        }
    }

    fun createBooking(booking: Booking): Boolean {
        val available = getAvailableBarbers(booking.dateTime, _hairstyles.first { it.id == booking.hairstyleId })
            .any { it.id == booking.barberId }

        return if (available) {
            _bookings.add(booking)
            true
        } else {
            false
        }
    }

    fun updateBooking(updated: Booking) {
        _bookings.replaceAll { if (it.id == updated.id) updated else it }
    }

    fun deleteBooking(id: String) {
        _bookings.removeAll { it.id == id }
    }
}
