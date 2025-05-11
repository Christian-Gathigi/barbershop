package com.example.mybarbershop.ui.theme.screens.barberbooking

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mybarbershop.data.Booking
import com.example.mybarbershop.models.BarberViewModel
import java.time.LocalDateTime
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UpdateBarberBookingScreen(
    navController: NavController,
    viewModel: BarberViewModel,
    bookingToEdit: Booking,
    onUpdateComplete: () -> Unit
) {
    val context = LocalContext.current

    val name = remember { mutableStateOf(bookingToEdit.clientName) }
    val timeInput = remember { mutableStateOf(bookingToEdit.dateTime.toLocalTime().toString()) }
    val selectedHairstyle = remember {
        mutableStateOf(viewModel.getHairstyleById(bookingToEdit.hairstyleId))
    }

    val selectedDateTime = remember(timeInput.value) {
        try {
            val parsedTime = LocalTime.parse(timeInput.value)
            LocalDateTime.of(LocalDateTime.now().toLocalDate(), parsedTime)
        } catch (e: Exception) {
            null
        }
    }


    Column(Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = name.value,
            onValueChange = { name.value = it },
            label = { Text("Client Name") },
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = timeInput.value,
            onValueChange = { timeInput.value = it },
            label = { Text("Time (HH:mm)") },
            placeholder = { Text("e.g. 14:00") },
            modifier = Modifier.padding(bottom = 8.dp)
        )

        DropdownSelector(
            label = "Select Hairstyle",
            options = viewModel.hairstyles,
            selectedOption = selectedHairstyle.value,
            onOptionSelected = { selectedHairstyle.value = it },
            optionLabel = { it.name }
        )


        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val hairstyle = selectedHairstyle.value

            val dateTime = selectedDateTime

            if (name.value.isNotBlank() && hairstyle != null && dateTime != null) {
                val updatedBooking = bookingToEdit.copy(
                    clientName = name.value,
                    dateTime = dateTime,
                    hairstyleId = hairstyle.id,

                )

                val success = viewModel.updateBooking(updatedBooking)
                if (success) {
                    Toast.makeText(context, "Booking Updated!", Toast.LENGTH_SHORT).show()
                    onUpdateComplete()
                } else {
                    Toast.makeText(context, "Barber not available at that time.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Please complete all fields", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Update Booking")
        }

        if (selectedDateTime != null && selectedHairstyle.value != null) {
            val endTime = selectedDateTime.plusMinutes(selectedHairstyle.value!!.durationMinutes.toLong())
            Text("Ends at: ${endTime.toLocalTime()}", Modifier.padding(top = 8.dp))
        }
    }
}
