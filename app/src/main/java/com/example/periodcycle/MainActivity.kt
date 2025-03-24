package com.example.periodcycle
import PeriodTrackerApp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.periodcycle.ui.theme.PeriodCycleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PeriodCycleTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "intro") {
                    composable("intro") {
                        //cannot remove backstack?
                        IntroScreen(navController,onDismiss = { navController.navigate("main")
                        })
                    }
                    composable("main") {
                        PeriodTrackerApp()
                    }
                }
            }
        }
    }
}
