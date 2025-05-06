package com.example.mybarbershop.models

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.mybarbershop.data.BarberAppointmentModel.Barber
import com.example.mybarbershop.data.BarberAppointmentModel.BarberBooking
import com.example.mybarbershop.data.BarberAppointmentModel.Hairstyle
import com.example.mybarbershop.ui.theme.screens.barberbooking.AddBarberBookingScreen
import java.time.LocalDateTime

// Simulated data (should be in ViewModel or repository)
val bookings = mutableListOf<BarberBooking>()
val barbers = listOf(
    Barber(id = "1", name = "Joe"),
    Barber(id = "2", name = "Mike")
)
val hairstyles = listOf(
    Hairstyle(id = "1", name = "Buzz Cut", durationMinutes = 30),
    Hairstyle(id = "2", name = "Fade", durationMinutes = 45)
)

fun getHairstyleById(id: String): Hairstyle {
    return hairstyles.first { it.id == id }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getAvailableBarbers(requestedTime: LocalDateTime, hairstyle: Hairstyle): List<Barber> {
    return barbers.filter { barber ->
        bookings.none { booking ->
            booking.barberId == barber.id &&
                    requestedTime < booking.dateTime.plusMinutes(getHairstyleById(booking.hairstyleId).durationMinutes.toLong()) &&
                    requestedTime.plusMinutes(hairstyle.durationMinutes.toLong()) > booking.dateTime
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun createBooking(booking: BarberBooking): Boolean {
    val hairstyle = getHairstyleById(booking.hairstyleId)
    val availableBarbers = getAvailableBarbers(booking.dateTime, hairstyle)
    return if (availableBarbers.any { it.id == booking.barberId }) {
        bookings.add(booking)
        true
    } else {
        false
    }
}

