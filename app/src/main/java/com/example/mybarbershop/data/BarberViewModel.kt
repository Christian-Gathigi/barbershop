package com.example.mybarbershop.data

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybarbershop.model.BarberAppointmentModel
import com.example.mybarbershop.model.Hairstyle
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime



class BarberAppointmentViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    // Available hairstyles (could be loaded from repository)
    val hairstyles = listOf(
        Hairstyle(1, "Buzz Cut", 15),
        Hairstyle(2, "Classic Cut", 30),
        Hairstyle(3, "Fade", 45),
        Hairstyle(4, "Pompadour", 60)
    )

    // Internal mutable list of appointments
    private val _appointments = MutableStateFlow<List<BarberAppointmentModel>>(emptyList())
    val appointments: MutableStateFlow<List<BarberAppointmentModel>> = _appointments


    private val _currentAppointment = MutableStateFlow<BarberAppointmentModel?>(null)
    val currentAppointment: StateFlow<BarberAppointmentModel?> = _currentAppointment
    fun fetchAppointments() {
        viewModelScope.launch {
            firestore.collection("appointments")
                .get()
                .addOnSuccessListener { result ->
                    val list = result.map { doc ->
                        doc.toObject(BarberAppointmentModel::class.java)
                    }
                    _appointments.value = list
                }
                .addOnFailureListener {

                }
        }
    }
    fun addAppointment(appointment: BarberAppointmentModel) {
        firestore.collection("appointments")
            .document(appointment.bookingid)
            .set(appointment)
    }

    fun updateAppointment(appointment: BarberAppointmentModel) {
        firestore.collection("appointments")
            .document(appointment.bookingid)
            .set(appointment)
    }

    fun deleteAppointment(bookingId: String) {
        firestore.collection("appointments")
            .document(bookingId)
            .delete()
    }

    // Create a new appointment and add to list
    @RequiresApi(Build.VERSION_CODES.O)
    fun createAppointment(
        bookingId: String,
        clientName: String,
        hairstyle: Hairstyle,
        startTime: LocalDateTime
    ) {
        val endTime = startTime.plusMinutes(hairstyle.durationMinutes.toLong())
        val newAppointment = BarberAppointmentModel(
            bookingid = bookingId,
            clientName = clientName,
            hairstyle = hairstyle,
            startTime = startTime,
            endTime = endTime
        )
        _appointments.value = (_appointments.value + newAppointment)
        _currentAppointment.value = newAppointment
    }

    // Load an appointment by bookingId for editing/viewing
    fun loadAppointmentById(bookingId: String) {
        val appointment = _appointments.value.find { it.bookingid == bookingId }
        _currentAppointment.value = appointment
    }

    // Update client name in current appointment
    fun updateClientName(name: String) {
        _currentAppointment.value = _currentAppointment.value?.copy(clientName = name)
        updateAppointmentInList()
    }

    // Update hairstyle and recalculate end time
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateHairstyle(hairstyle: Hairstyle) {
        _currentAppointment.value = _currentAppointment.value?.let { current ->
            current.copy(
                hairstyle = hairstyle,
                endTime = current.startTime.plusMinutes(hairstyle.durationMinutes.toLong())
            )
        }
        updateAppointmentInList()
    }

    // Update start time and recalculate end time
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateStartTime(startTime: LocalDateTime) {
        _currentAppointment.value = _currentAppointment.value?.let { current ->
            current.copy(
                startTime = startTime,
                endTime = startTime.plusMinutes(current.hairstyle.durationMinutes.toLong())
            )
        }
        updateAppointmentInList()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun isTimeSlotAvailable(
        proposedStart: LocalDateTime,
        proposedEnd: LocalDateTime,
        excludeBookingId: String? = null // To exclude current booking when updating
    ): Boolean {
        val currentAppointments = _appointments.value // your list of appointments

        return currentAppointments.none { appointment ->
            if (excludeBookingId != null && appointment.bookingid == excludeBookingId) {
                false // skip current booking when updating
            } else {
                // Overlap condition
                proposedStart.isBefore(appointment.endTime) && proposedEnd.isAfter(appointment.startTime)
            }
        }
    }

    // Save current appointment (simulate persistence)
    fun saveAppointmentToFirebase(
        appointment: BarberAppointmentModel,
        onComplete: (() -> Unit)? = null
    ) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("appointments")

        ref.child(appointment.bookingid).setValue(appointment)
            .addOnSuccessListener {
                // Data saved successfully
                onComplete?.invoke()
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Failed to save data: ${e.message}")
            }
    }



    // Helper to update appointment in the list when currentAppointment changes
    private fun updateAppointmentInList() {
        val updated = _currentAppointment.value ?: return
        _appointments.value = _appointments.value.map {
            if (it.bookingid == updated.bookingid) updated else it
        }
    }
}
