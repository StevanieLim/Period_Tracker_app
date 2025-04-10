package com.example.periodcycle
import PeriodTrackerApp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.periodcycle.database.HistoryDateDatabase
import com.example.periodcycle.database.UserData
import com.example.periodcycle.database.UserDatabase
import com.example.periodcycle.ui.theme.PeriodCycleTheme

class MainActivity : ComponentActivity() {
//    private lateinit var userData: UserDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        userData = UserDatabase.getDatabase(applicationContext)
//        val User1 = UserData(1,"Stevanie",7,30)
//        userData.userDao().addAUser(User1)
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
