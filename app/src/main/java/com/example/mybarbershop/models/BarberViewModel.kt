package com.example.mybarbershop.models

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.mybarbershop.R
import com.example.mybarbershop.data.Barber
import com.example.mybarbershop.data.Booking
import com.example.mybarbershop.data.Hairstyle
import java.time.LocalDateTime

open class BarberViewModel : ViewModel() {

    private val _barbers = mutableStateListOf<Barber>()
    val barbers: List<Barber> get() = _barbers

    private val _hairstyles = mutableStateListOf<Hairstyle>()
    open val hairstyles: List<Hairstyle> get() = _hairstyles

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
        fun getHairstyles(): List<Hairstyle> = listOf(
            Hairstyle(id = "1", name = "Buzz Cut", durationMinutes = 30,R.drawable.buzzcut),
            Hairstyle(id = "2", name = "Afro Mohawk", durationMinutes = 45,R.drawable.afromohawk),
            Hairstyle(id = "3", name = "Braids", durationMinutes = 60,R.drawable.braids),
            Hairstyle(id = "4", name = "Mid Taper", durationMinutes = 35,R.drawable.midtaper),
            Hairstyle(id = "5", name = "Afro Taper", durationMinutes = 50,R.drawable.afrotaper),
            Hairstyle(id = "6", name = "Haircut", durationMinutes = 40,R.drawable.haircut)
        )


    }

    @RequiresApi(Build.VERSION_CODES.O)
    open fun getAvailableBarbers(desiredTime: LocalDateTime, hairstyle: Hairstyle): List<Barber> {
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

    @RequiresApi(Build.VERSION_CODES.O)
    open fun createBooking(booking: Booking): Boolean {
        val available = getAvailableBarbers(booking.dateTime, _hairstyles.first { it.id == booking.hairstyleId })
            .any { it.id == booking.barberId }

        return if (available) {
            _bookings.add(booking)
            true
        } else {
            false
        }
    }


    fun deleteBooking(id: String) {
        _bookings.removeAll { it.id == id }
    }
    // Fetch hairstyle by ID
    fun getHairstyleById(id: String): Hairstyle? {
        return hairstyles.find { it.id == id }
    }

    // Fetch barber by ID
    fun getBarberById(id: String): Barber? {
        return barbers.find { it.id == id }
    }

    // Update booking by matching ID
    fun updateBooking(updated: Booking): Boolean {
        val index = bookings.indexOfFirst { it.id == updated.id }
        return if (index != -1) {

            true
        } else {
            false
        }
    }




}
