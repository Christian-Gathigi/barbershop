package com.example.mybarbershop.ui.theme.screens.barberbooking

import android.R.attr.end
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mybarbershop.data.BarberAppointmentViewModel
import com.example.mybarbershop.model.BarberAppointmentModel

import com.example.mybarbershop.model.Hairstyle
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddBarberBookingScreen(navController: NavController, bookingViewModel: BarberAppointmentViewModel = viewModel()) {
    var clientName by remember { mutableStateOf("") }
    var selectedHairstyle by remember { mutableStateOf<Hairstyle?>(null) }
    var startTimeText by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Book a Barber Appointment", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = clientName,
            onValueChange = { clientName = it },
            label = { Text("Client Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Select Hairstyle", style = MaterialTheme.typography.titleMedium)
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        ) {
            items(bookingViewModel.hairstyles.size) { index ->
                val hairstyle = bookingViewModel.hairstyles[index]
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedHairstyle = hairstyle }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = hairstyle == selectedHairstyle,
                        onClick = { selectedHairstyle = hairstyle }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("${hairstyle.name} (${hairstyle.durationMinutes} mins)")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = startTimeText,
            onValueChange = { startTimeText = it },
            label = { Text("Start Time (yyyy-MM-dd HH:mm)") },
            placeholder = { Text("2025-05-13 09:00") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        val endTimeDisplay = remember(selectedHairstyle, startTimeText) {
            try {
                if (selectedHairstyle != null && startTimeText.isNotBlank()) {
                    val start = LocalDateTime.parse(startTimeText, formatter)
                    val end = start.plusMinutes(selectedHairstyle!!.durationMinutes.toLong())
                    "End Time: ${end.format(formatter)}"
                } else {
                    ""
                }
            } catch (e: Exception) {
                ""
            }
        }

        Text(endTimeDisplay, style = MaterialTheme.typography.bodyLarge)

        errorMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { navController.popBackStack() }) {
                Text("Go Back")
            }
            Button(onClick = {
                try {
                    // Validate inputs
                    if (clientName.isBlank()) {
                        errorMessage = "Please enter client name"
                        return@Button
                    }
                    if (selectedHairstyle == null) {
                        errorMessage = "Please select a hairstyle"
                        return@Button
                    }

                    // Parse start time
                    val proposedStart = LocalDateTime.parse(startTimeText, formatter)

                    // Calculate end time based on hairstyle duration
                    val proposedEnd = proposedStart.plusMinutes(selectedHairstyle!!.durationMinutes.toLong())

                    // Check if the time slot is available before saving
                    val available = bookingViewModel.isTimeSlotAvailable(proposedStart, proposedEnd)

                    if (!available) {
                        errorMessage = "Selected time overlaps with another booking."
                        return@Button
                    }

                    // Generate booking ID
                    val bookingId = "BKG${System.currentTimeMillis()}"

                    // Create appointment object
                    val appointment = BarberAppointmentModel(
                        bookingid = bookingId,
                        clientName = clientName,
                        hairstyle = selectedHairstyle!!,
                        startTime = proposedStart,
                        endTime = proposedEnd
                    )

                    // Save appointment
                    bookingViewModel.saveAppointmentToFirebase(appointment)

                    // Clear error and navigate back
                    errorMessage = null
                    navController.popBackStack()

                } catch (e: Exception) {
                    errorMessage = "Invalid start time format"
                }
            }) {
                Text("Save")
            }}}}
