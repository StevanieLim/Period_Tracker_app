import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.periodcycle.AccountUI
import com.example.periodcycle.CalenderUI
import com.example.periodcycle.database.HistoryDateViewModel
import com.example.periodcycle.HomePageUi
import com.example.periodcycle.database.UserHistoryViewModel
import com.example.periodcycle.database.UserViewModel
import kotlinx.coroutines.delay
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun PeriodTrackerApp(
    navController: NavHostController = rememberNavController()
) {
    val viewModelHistory: HistoryDateViewModel = viewModel()
    val viewModelUser: UserViewModel = viewModel()
    val viewModelUserHistory: UserHistoryViewModel = viewModel()
    var selectedItem by remember { mutableStateOf(0) } // State to track the selected item
    val items = listOf("Home", "Calendar", "Me") // List of bottom bar items
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    var progress by remember { mutableStateOf(calculateProgress()) }
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            lerpColor(morningColors[0], nightColors[0], progress),
            lerpColor(morningColors[1], nightColors[1], progress)
        )
    )

    //Current User
    val currentUser by remember { mutableIntStateOf(0) }


    // Update progress every minute
    LaunchedEffect(Unit) {
        while (true) {
            delay(600_000) // Update every 10 minute
            progress = calculateProgress()
        }
    }
        val currentRoute = currentBackStackEntry?.destination?.route
        Scaffold(
            bottomBar = {
                BottomAppBar (
                    modifier  = Modifier.height(55.dp),
                    containerColor = Color(0xFFE97777)
                ){
                    items.forEachIndexed { index, item ->
                        BottomNavigationItem(
                            icon = {
                                when (index) {
                                    0 -> Icon(Icons.Default.Home, tint = Color.White ,contentDescription = "Home")
                                    1 -> Icon(Icons.Default.DateRange, tint = Color.White ,contentDescription = "Calendar")
                                    2 -> Icon(Icons.Default.AccountCircle, tint = Color.White, contentDescription = "Me")
                                }
                            },
                            label = { Text(text = item, color = Color.White) },
                            selected = selectedItem == index,
                            onClick = {
                                when (index) {
                                    0 -> navController.navigate("Home"){
                                        popUpTo("Home")
                                        launchSingleTop = true
                                    }
                                    1 -> navController.navigate("Calendar"){
                                        popUpTo("Home")
                                        launchSingleTop = true
                                    }
                                    2 -> navController.navigate("Me"){
                                        popUpTo("Home")
                                        launchSingleTop = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        )
        {padding->
            Box(
                modifier = Modifier
                    .fillMaxSize().background(backgroundBrush)
            ) {
                // Your content here
            }
            NavHost(
                navController = navController,
                startDestination = "Home",
                modifier = Modifier.padding(padding)
            ) {
                composable("Home") { HomePageUi() }
                composable("Calendar") { CalenderUI(viewModel = viewModelHistory, viewModelUser = viewModelUser, viewModelUserHistory = viewModelUserHistory ,currentUser = currentUser) }
                composable("Me") { AccountUI(viewModel = viewModelUser, viewModel2 = viewModelUserHistory, currentUser = currentUser) }
            }
        }
    }

// Define colors for gradients
val nightColors = listOf(Color(0xFFFFFAD7), Color(0xFFE97777))
val morningColors = listOf(Color(0xFFFCDDB0), Color(0xFFE97777))

fun calculateProgress(): Float {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY) // Get current hour (0-23)

    // Define morning and night hours
    val morningStart = 6 // 6 AM
    val nightStart = 18 // 6 PM

    return when {
        hour < morningStart -> 0f // Before morning, fully night
        hour >= nightStart -> 1f // After night starts, fully night
        else -> (hour - morningStart).toFloat() / (nightStart - morningStart) // Progress between morning and night
    }
}

fun lerpColor(start: Color, end: Color, fraction: Float): Color {
    return Color(
        red = lerp(start.red, end.red, fraction),
        green = lerp(start.green, end.green, fraction),
        blue = lerp(start.blue, end.blue, fraction),
        alpha = lerp(start.alpha, end.alpha, fraction)
    )
}

fun lerp(start: Float, end: Float, fraction: Float): Float {
    return start + (end - start) * fraction
}