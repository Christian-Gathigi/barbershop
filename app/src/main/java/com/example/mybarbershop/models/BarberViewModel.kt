package com.example.mybarbershop.models

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.mybarbershop.R
import com.example.mybarbershop.data.Booking
import com.example.mybarbershop.data.Hairstyle

@RequiresApi(Build.VERSION_CODES.O)
open class BarberViewModel : ViewModel() {

    open val hairstyles = listOf(
        Hairstyle(id = "1", name = "Buzz Cut", durationMinutes = 30, imageResId = R.drawable.buzzcut),
        Hairstyle(id = "2", name = "Afro Mohawk", durationMinutes = 45, imageResId = R.drawable.afromohawk),
        Hairstyle(id = "3", name = "Braids", durationMinutes = 50, imageResId = R.drawable.braids),
        Hairstyle(id = "4", name = "Mid Taper", durationMinutes = 40, imageResId = R.drawable.midtaper),
        Hairstyle(id = "5", name = "Afro Taper", durationMinutes = 25, imageResId = R.drawable.afrotaper),
        Hairstyle(id = "6", name = "Haircut", durationMinutes = 25, imageResId = R.drawable.haircut),
        Hairstyle(id = "7", name = "Beard trim", durationMinutes = 25, imageResId = R.drawable.afrotaper)
    )



    internal val bookings = mutableListOf<Booking>()



    open fun createBooking(booking: Booking): Boolean {
        val conflicting = bookings.any {
                    it.dateTime < booking.dateTime.plusMinutes(
                hairstyles.find { style -> style.id == booking.hairstyleId }?.durationMinutes?.toLong() ?: 0
            ) &&
                    it.dateTime.plusMinutes(
                        hairstyles.find { style -> style.id == it.hairstyleId }?.durationMinutes?.toLong() ?: 0
                    ) > booking.dateTime
        }

        return if (!conflicting) {
            bookings.add(booking)
            true
        } else {
            false
        }
    }

    fun getBookingById(id: String): Booking? {
        return bookings.find { it.id == id }
    }
    fun getHairstyleById(id: String): Hairstyle? =
        hairstyles.find { it.id == id }



    fun updateBooking(updatedBooking: Booking): Boolean {
        val index = bookings.indexOfFirst { it.id == updatedBooking.id }
        if (index != -1) {
            val conflicts = bookings.any {
                it.id != updatedBooking.id &&
                        it.dateTime < updatedBooking.dateTime.plusMinutes(
                    getHairstyleById(updatedBooking.hairstyleId)?.durationMinutes?.toLong() ?: 0
                ) &&
                        updatedBooking.dateTime < it.dateTime.plusMinutes(
                    getHairstyleById(it.hairstyleId)?.durationMinutes?.toLong() ?: 0
                )
            }
            if (!conflicts) {
                bookings[index] = updatedBooking
                return true
            }
        }
        return false
    }
    fun deleteBooking(bookingId: String) {
        bookings.removeIf { it.id == bookingId }
    }

    fun getAllBookings(): List<Booking> = bookings
}
