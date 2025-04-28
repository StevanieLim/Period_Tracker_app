package com.example.periodcycle

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.periodcycle.database.HistoryDate
import com.example.periodcycle.database.HistoryDateViewModel
import com.example.periodcycle.database.UserData
import com.example.periodcycle.database.UserHistoryViewModel
import com.example.periodcycle.database.UserViewModel
import io.github.boguszpawlowski.composecalendar.Calendar
import io.github.boguszpawlowski.composecalendar.CalendarState
import io.github.boguszpawlowski.composecalendar.day.DayState
import io.github.boguszpawlowski.composecalendar.rememberCalendarState
import io.github.boguszpawlowski.composecalendar.selection.EmptySelectionState
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalenderUI(
    viewModel: HistoryDateViewModel,
    viewModelUser: UserViewModel,
    currentUser: Int,
    viewModelUserHistory: UserHistoryViewModel
) {
    val dates by viewModel.allDates.collectAsState(initial = emptyList())
    val users by viewModelUser.allUser.collectAsState(initial = emptyList())
    var user by remember { mutableStateOf<UserData?>(null) }
    users.firstOrNull()?.let {
        user = users[currentUser]
    }
    val calendarState = rememberCalendarState()

    Box(
    ) {

        LazyColumn(
            modifier = Modifier.background(Color(0xFFFFFAD7)).fillMaxHeight()
        ) {
            item {
//             Calendar Section
                if (user != null) {
                    CustomCalendarView(
                        viewModelUserHistory = viewModelUserHistory,
                        viewModel = viewModel,
                        user = user!!,
                        dates = dates,
                        calendarState
                    )
                }

                // Add Cycle Button
                Button(
                    onClick = {
                        user?.averagePeriod?.let { LocalDate.now().plusDays(it.toLong()) }?.let {
                            viewModel.saveDate(LocalDate.now(), it)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE97777)
                    )
                ) {
                    Text("Start Period Today!")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // History Section
                Text(
                    text = "History",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                HistoryList(viewModel = viewModel, dates = dates)
            }
        }

        FloatingActionButton(
            onClick = {
                calendarState.monthState.currentMonth = YearMonth.now()
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = Color(0xFFE97777),
            contentColor = Color.White
        ) {
            Icon(Icons.Default.DateRange, contentDescription = "Go to Today")
        }
    }
}



@Composable
fun CustomCalendarView(
    viewModelUserHistory: UserHistoryViewModel,
    viewModel: HistoryDateViewModel,
    user: UserData,
    dates: List<HistoryDate>,
    calendarState: CalendarState<EmptySelectionState>,
    ) {
    var showDialog by remember { mutableStateOf(0) }
    var seledtedDay by remember { mutableStateOf<DayState<EmptySelectionState>?>(null) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(365.dp)
    ) {
        Calendar(
            calendarState = calendarState,
            monthHeader = { monthState ->
                // Customize the month header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Absolute.Center
                ) {
                    Text(
                        text = monthState.currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE97777),
                        modifier = Modifier.padding(end = 10.dp)
                    )
                    Text(
                        text = "${monthState.currentMonth.year}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                        color = Color.Gray
                    )
                }
            },
            daysOfWeekHeader = { daysOfWeek ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    daysOfWeek.forEach { dayOfWeek ->
                        Text(
                            text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                            color = Color(0xFFFF9F9F),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.weight(1f), // Equal weight for each day
                            textAlign = TextAlign.Center // Center the text within its space
                        )
                    }
                }
            },
            dayContent = { day ->
                val isPast = day.date.isBefore(LocalDate.now())

                val isPeriodDay by remember(dates) {
                    derivedStateOf {
                        dates.any { day.date in it.startDate..it.endDate }
                    }
                }

                val isFuturePeriodDay by remember(dates) {
                    derivedStateOf {
                        dates.lastOrNull()?.startDate?.let { lastStartDate ->
                            generateSequence(lastStartDate) { it.plusMonths(1) }
                                .takeWhile { it <= day.date }
                                .any { start ->
                                    val startDay = start.plusDays(user.averageCycle.toLong())
                                    val endDay = start.plusDays((user.averageCycle + user.averagePeriod).toLong())
                                    day.date in startDay..endDay
                                }
                        } ?: false
                    }
                }




                Box(
                    modifier = Modifier
                        .padding(1.dp)
                        .size(48.dp)
                        //Blood Flow state belum handle
                        .background(
                            if (isPeriodDay)
                                if(isPast) Color(0xFFE97777).copy(alpha = 0.5f) else Color(0xFFE97777)

                            else if (isFuturePeriodDay) Color(0xFFE97777).copy(alpha = 0.8f)

                            else if (day.isCurrentDay) Color(0xFFFFBE5E).copy(alpha = 0.6f)

                            else if (day.isFromCurrentMonth)
                                if (isPast) Color(0xFFFFDDA9).copy(alpha = 0.5f) else Color(0xFFFFDDA9)

                            else Color(0xFFFFDDA9).copy(alpha = 0.2f),

                            if (day.isCurrentDay) RoundedCornerShape(50) else RoundedCornerShape(20)
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(bounded = true), // Adds a ripple effect
                        ) {
                            seledtedDay = day
                            if (isPeriodDay) showDialog = 2
                            else showDialog = 1
                          },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (day.isCurrentDay) "Today" else day.date.dayOfMonth.toString(),
                        fontSize = if (day.isCurrentDay) 12.sp else 18.sp,
                        fontWeight = if (day.isFromCurrentMonth && !isPast) FontWeight.Bold else FontWeight.Normal,
                        color = if (isPeriodDay) Color.White
                                else if (isPast) Color(0xFFD86666).copy(alpha = 0.5f)
                                else Color(0xFFD86666)
                    )
                }
            }
        )


        DialogBloodBox(
            show = showDialog,
            onDismiss = { showDialog = 0 },
            day = seledtedDay,
            viewModel = viewModelUserHistory ,
        )
        DialogDateBox(
            show = showDialog,
            onDismiss = { showDialog = 0 },
            day = seledtedDay,
            viewModel = viewModel,
            period = user.averagePeriod
        )
    }
}

@Composable
fun DialogBloodBox(viewModel: UserHistoryViewModel, show : Int, onDismiss : () -> Unit,
                   day : DayState<EmptySelectionState>?){
    var rating by remember { mutableStateOf(0) }
    if (show == 2) {
        Dialog(onDismissRequest = {onDismiss()}) {
            Surface (
                modifier = Modifier.wrapContentSize(),
                shape = MaterialTheme.shapes.small,
                color = Color(0xFFFFD495)
            ){
                Column (
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){

                    Text(text = day?.date.toString(), style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "How is Your Blood Flow?", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(16.dp))
                    StarRating(
                        rating = rating,
                        onRatingChange = { newRating -> rating = newRating }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Track Your Blood Flow to record your body!")
                    Button(
                        onClick = {
                            viewModel.UpdataBloodFlow(rating)
                            onDismiss() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE97777))
                    ) {
                        Text("Done!")
                    }
                }
            }
        }
    }
}

@Composable
fun DialogDateBox(viewModel: HistoryDateViewModel, show : Int, onDismiss : () -> Unit,
                   day : DayState<EmptySelectionState>?, period : Int){
    if (show == 1 ) {
        Dialog(onDismissRequest = {onDismiss()}) {
            Surface (
                modifier = Modifier.wrapContentSize(),
                shape = MaterialTheme.shapes.small,
                color = Color(0xFFFFD495)
            ){
                Column (
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){

                    Text(text = day?.date.toString(), style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Today You Start Your Period?")
                    Button(
                        onClick = {
                            day?.date?.let { viewModel.saveDate(it,it.plusDays(period.toLong())) }
                            onDismiss() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE97777))
                    ) {
                        Text("Yes!")
                    }
                }
            }
        }
    }
}

@Composable
fun StarRating(
    rating: Int, // Current rating (e.g., 3 out of 5)
    onRatingChange: (Int) -> Unit // Callback to update the rating
) {
    val maxRating = 5 // Maximum number of stars
    Box(
        modifier = Modifier
            .width(200.dp)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    val newRating = (change.position.x / size.width) * maxRating
                    onRatingChange(
                        newRating
                            .coerceIn(0f, maxRating.toFloat())
                            .toInt()
                    )
                }
            }
    ) {

        Row {
            for (i in 1..maxRating) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_water_drop_24),
                    contentDescription = "Blood $i",
                    tint = if (i <= rating) Color(0xFFE97777) else Color.White, // Color stars based on rating
                    modifier = Modifier
                        .clickable { onRatingChange(i) } // Update rating when a star is clicked
                        .padding(4.dp)
                        .size(32.dp)
                )
            }
        }
    }
}


@Composable
fun HistoryList(viewModel: HistoryDateViewModel, dates: List<HistoryDate>) {
    Column {
        if (dates.isEmpty()) {
            Text("No cycles recorded yet.")
        } else {
            dates.forEach { date ->
                HistoryItem(viewModel=viewModel,date = date)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun HistoryItem(viewModel: HistoryDateViewModel, date: HistoryDate) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { viewModel.deleteDate(date.id) },
        colors = CardDefaults.cardColors(Color(0xFFFFBE5E).copy(alpha = 0.7f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Start: ${date.startDate}",
                style = MaterialTheme.typography.bodyMedium ,
                fontWeight = FontWeight.ExtraBold, // Make it stand out
                color = Color.White
            )
            Text(
                text = "End: ${date.endDate}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.ExtraBold, // Make it stand out
                color = Color.White
            )
        }
    }
}


