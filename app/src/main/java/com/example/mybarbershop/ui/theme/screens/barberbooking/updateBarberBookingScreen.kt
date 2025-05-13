package com.example.mybarbershop.ui.theme.screens.barberbooking

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
import com.example.mybarbershop.model.Hairstyle
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import androidx.compose.material.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.foundation.lazy.items

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UpdateBarberBookingScreen(
    navController: NavController,
    bookingId: String,
    barberAppointmentViewModel: BarberAppointmentViewModel = viewModel()
) {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    // Load appointment when screen opens
    LaunchedEffect(bookingId) {
        barberAppointmentViewModel.loadAppointmentById(bookingId)
    }

    val currentAppointment by barberAppointmentViewModel.currentAppointment.collectAsState()

    var clientName by remember { mutableStateOf("") }
    var selectedHairstyle by remember { mutableStateOf<Hairstyle?>(null) }
    var startTimeText by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Sync UI state with loaded appointment
    LaunchedEffect(currentAppointment) {
        currentAppointment?.let {
            clientName = it.clientName
            selectedHairstyle = it.hairstyle
            startTimeText = it.startTime.format(formatter)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Update Booking") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = clientName,
                onValueChange = {
                    clientName = it
                    barberAppointmentViewModel.updateClientName(it)
                },
                label = { Text("Client Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Select Hairstyle")
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                items(barberAppointmentViewModel.hairstyles) { hairstyle ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedHairstyle = hairstyle
                                barberAppointmentViewModel.updateHairstyle(hairstyle)
                            }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = hairstyle == selectedHairstyle,
                            onClick = {
                                selectedHairstyle = hairstyle
                                barberAppointmentViewModel.updateHairstyle(hairstyle)
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("${hairstyle.name} (${hairstyle.durationMinutes} mins)")
                    }
                }}

            OutlinedTextField(
                value = startTimeText,
                onValueChange = {
                    startTimeText = it
                    try {
                        val parsed = LocalDateTime.parse(it, formatter)
                        barberAppointmentViewModel.updateStartTime(parsed)
                        errorMessage = null
                    } catch (e: Exception) {
                        errorMessage = "Invalid date/time format"
                    }
                },
                label = { Text("Start Time (yyyy-MM-dd HH:mm)") },
                modifier = Modifier.fillMaxWidth()
            )

            errorMessage?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { navController.popBackStack() }) {
                    Text("Cancel")
                }
                Button(onClick = {
                    if (clientName.isBlank()) {
                        errorMessage = "Client name cannot be empty"
                        return@Button
                    }
                    if (selectedHairstyle == null) {
                        errorMessage = "Please select a hairstyle"
                        return@Button
                    }
                    try {
                        val start = LocalDateTime.parse(startTimeText, formatter)
                        val end = start.plusMinutes(selectedHairstyle!!.durationMinutes.toLong())

                        val isAvailable = barberAppointmentViewModel.isTimeSlotAvailable(
                            proposedStart = start,
                            proposedEnd = end,
                            excludeBookingId = bookingId
                        )

                        if (!isAvailable) {
                            errorMessage = "This time slot overlaps with another booking."
                            return@Button
                        }

                        val updatedAppointment = currentAppointment?.copy(
                            clientName = clientName,
                            hairstyle = selectedHairstyle!!,
                            startTime = start,
                            endTime = end
                        ) ?: return@Button

                        barberAppointmentViewModel.saveAppointmentToFirebase(updatedAppointment) {
                            errorMessage = null
                            navController.popBackStack()
                        }
                    } catch (e: Exception) {
                        errorMessage = "Invalid start time"
                    }
                }) {
                    Text("Save")
                }
            }
        }}}

@Composable
fun HairstyleSelectionList(
    hairstyles: List<Hairstyle>,
    selectedHairstyle: Hairstyle?,
    onHairstyleSelected: (Hairstyle) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
    ) {
        items(hairstyles) { hairstyle ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onHairstyleSelected(hairstyle)
                    }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = hairstyle == selectedHairstyle,
                    onClick = {
                        onHairstyleSelected(hairstyle)
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "${hairstyle.name} (${hairstyle.durationMinutes} mins)")
            }
        }
    }
}

