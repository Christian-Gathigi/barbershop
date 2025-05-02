package com.example.mybarbershop.ui.theme.screens.massage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController

@Composable
fun AddMassageScreen() {
    Column(Modifier.padding(16.dp)) {
        Text("Booking Page")
        // Add date pickers, time slots, etc.
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddMassageScreenPreview(){
    AddMassageScreen()
}