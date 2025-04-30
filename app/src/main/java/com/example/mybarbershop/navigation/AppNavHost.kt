package com.example.mybarbershop.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mybarbershop.ui.theme.screens.SplashScreen
import com.example.mybarbershop.ui.theme.screens.dashboard.DashboardScreen
import com.example.mybarbershop.ui.theme.screens.menssection.mensSectionScreen
import com.example.mybarbershop.ui.theme.screens.womenssection.womensSectionScreen


@Composable
fun AppNavHost(navController:NavHostController= rememberNavController(),startDestination:String= ROUTE_SPLASH){
    NavHost(navController=navController,startDestination=startDestination){
        composable(ROUTE_SPLASH){ SplashScreen{
            navController.navigate(ROUTE_DASHBOARD){
                popUpTo(ROUTE_SPLASH){inclusive=true}} } }
        composable(ROUTE_MENSSECTION) { MENSSECTION(navController) }
        composable(ROUTE_WOMENSSECTION) { LoginScreen(navController) }
        composable(ROUTE_DASHBOARD) { DashboardScreen(navController) }

    }
}