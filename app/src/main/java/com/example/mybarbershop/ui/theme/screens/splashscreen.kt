package com.example.mybarbershop.ui.theme.screens

import android.window.SplashScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mybarbershop.R

@Composable
fun SplashScreen(onNavigateToNext: () -> Unit) {
    val splashScreenDuration = 3000L
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(splashScreenDuration)
        onNavigateToNext()
    }
    Box(modifier = Modifier.fillMaxSize().background(Color.White), contentAlignment = Alignment.Center)
    { Column(horizontalAlignment = Alignment.CenterHorizontally){
        Image(painter = painterResource(id = R.drawable.logo), contentDescription = "App Logo",
            modifier = Modifier.size(300.dp))
        Text(text = "Welcome to Maryland", color = Color.White)
    }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SplashScreenPreview(){
    SplashScreen { }
}