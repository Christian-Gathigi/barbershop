package com.example.mybarbershop.ui.theme.screens.barberbooking

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mybarbershop.R
import com.example.mybarbershop.data.Barber
import com.example.mybarbershop.data.Booking
import com.example.mybarbershop.data.Hairstyle
import com.example.mybarbershop.models.BarberViewModel

import java.time.LocalDateTime
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddBarberBookingScreen(viewModel: BarberViewModel) {
    val context = LocalContext.current

    val name = remember { mutableStateOf("") }
    val timeInput = remember { mutableStateOf("") } // User input for time, e.g. "14:00"
    val selectedHairstyle = remember { mutableStateOf<Hairstyle?>(null) }
    val selectedBarber = remember { mutableStateOf<Barber?>(null) }

    val selectedDateTime = remember(timeInput.value) {
        try {
            val parsedTime = LocalTime.parse(timeInput.value)
            LocalDateTime.of(LocalDateTime.now().toLocalDate(), parsedTime)
        } catch (e: Exception) {
            null
        }
    }

    val availableBarbers = remember(selectedDateTime, selectedHairstyle.value) {
        selectedDateTime?.let { time ->
            selectedHairstyle.value?.let { hairstyle ->
                viewModel.getAvailableBarbers(time, hairstyle)
            }
        } ?: emptyList()
    }


    Column(Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = name.value,
            onValueChange = { name.value = it },
            label = { Text("Your Name") },
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = timeInput.value,
            onValueChange = { timeInput.value = it },
            label = { Text("Time (HH:mm)") },
            placeholder = { Text("e.g. 14:30") },
            modifier = Modifier.padding(bottom = 8.dp)
        )
        DropdownSelector(
            label = "Select Hairstyle",
            options = viewModel.hairstyles,
            selectedOption = selectedHairstyle.value,
            onOptionSelected = { selectedHairstyle.value = it }
        )
        Spacer(Modifier.height(8.dp))

        DropdownSelector(
            label = "Select Barber",
            options = availableBarbers,
            selectedOption = selectedBarber.value,
            onOptionSelected = { selectedBarber.value = it }
        )

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            val hairstyle = selectedHairstyle.value
            val barber = selectedBarber.value
            val dateTime = selectedDateTime

            if (name.value.isNotBlank() && hairstyle != null && barber != null && dateTime != null) {
                val booking = Booking(
                    clientName = name.value,
                    dateTime = dateTime,
                    barberId = barber.id,
                    hairstyleId = hairstyle.id,
                    id = TODO()
                )
                val success = viewModel.createBooking(booking)
                if (!success) {
                    Toast.makeText(context, "Barber not available!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Please complete all fields correctly", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Book Now")
        }

        if (selectedDateTime != null && selectedHairstyle.value != null) {
            val endTime = selectedDateTime.plusMinutes(selectedHairstyle.value!!.durationMinutes.toLong())
            Text("Ends at: ${endTime.toLocalTime()}", Modifier.padding(top = 8.dp))
        }
    }
}
@Composable
fun HairstyleDropdownSelector(
    label: String,
    options: List<Hairstyle>,
    selectedOption: Hairstyle?,
    onOptionSelected: (Hairstyle) -> Unit
) {
    val expanded = remember { mutableStateOf(false) }

    Column {
        Button(onClick = { expanded.value = true }) {
            Text(text = selectedOption?.name ?: label)
        }

        DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Row {
                            Image(
                                painter = painterResource(id = option.imageResId),
                                contentDescription = option.name,
                                modifier = Modifier
                                    .height(40.dp)
                                    .padding(end = 8.dp)
                            )
                            Text(option.name)
                        }
                    },
                    onClick = {
                        onOptionSelected(option)
                        expanded.value = false
                    }
                )
            }
        }
    }
}
@Composable
fun <T> DropdownSelector(
    label: String,
    options: List<T>,
    selectedOption: T?,
    onOptionSelected: (T) -> Unit
) {
    val expanded = remember { mutableStateOf(false) }

    Column {
        Button(onClick = { expanded.value = true }) {
            Text(text = selectedOption?.toString() ?: label)
        }

        DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.toString()) },
                    onClick = {
                        onOptionSelected(option)
                        expanded.value = false
                    }
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun AddBarberBookingScreenPreview() {
    val fakeViewModel = object : BarberViewModel() {

        val fakeHairstyles= listOf(
            Hairstyle(id = "1", name = "Buzz Cut", durationMinutes = 30, imageResId = R.drawable.buzzcut),
            Hairstyle(id = "2", name = "Afro Mohawk", durationMinutes = 45, imageResId = R.drawable.afromohawk),
            Hairstyle(id = "3", name = "Braids", durationMinutes = 50, imageResId = R.drawable.braids),
            Hairstyle(id = "4", name = "Mid Taper", durationMinutes = 40, imageResId = R.drawable.midtaper),
            Hairstyle(id = "5", name = "Afro Taper", durationMinutes = 25, imageResId = R.drawable.afrotaper),
            Hairstyle(id = "5", name = "Haircut", durationMinutes = 25, imageResId = R.drawable.haircut),
            Hairstyle(id = "5", name = "Beard trim", durationMinutes = 25, imageResId = R.drawable.afrotaper)
        )

        override fun getAvailableBarbers(time: LocalDateTime, hairstyle: Hairstyle): List<Barber> {
            return listOf(
                Barber(id = "1", name = "Joe"),
                Barber(id = "2", name = "Mike")
            )
        }

        override fun createBooking(booking: Booking): Boolean {
            return true
        }
    }

    AddBarberBookingScreen(viewModel = fakeViewModel)
}
