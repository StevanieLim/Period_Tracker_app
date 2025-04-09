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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import io.github.boguszpawlowski.composecalendar.Calendar
import io.github.boguszpawlowski.composecalendar.day.DayState
import io.github.boguszpawlowski.composecalendar.rememberCalendarState
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.EmptySelectionState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalenderUI(viewModel: HistoryDateViewModel , cyclePeriod: Int){
    val dates by viewModel.allDates.collectAsState(initial = emptyList())
    var isPeriodDay by remember { mutableStateOf(false) }
    LazyColumn(
    modifier = Modifier.background(Color(0xFFFFFAD7))
    ){
        item {
//             Calendar Section
            CustomCalendarView(viewModel = viewModel, isPeriodDay = isPeriodDay, cyclePeriod = cyclePeriod)

            // Add Cycle Button
            Button(
                onClick = {
//                    viewModel.saveDate("2024-05-20","2024-05-27")
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE97777)
                )
            ) {
                Text("Add Cycle")
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
}


@Composable
fun CustomCalendarView(viewModel: HistoryDateViewModel ,isPeriodDay : Boolean, cyclePeriod:Int) {
    var showDialog by remember { mutableStateOf(false) }
    val calendarState = rememberCalendarState()
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
                Box(
                    modifier = Modifier
                        .padding(1.dp)
                        .size(48.dp) // Set a fixed size for uniform circles
                        .background(
                            if (isPeriodDay) Color(0xFFE97777)
                            else if (day.isCurrentDay) Color(0xFFFFBE5E).copy(alpha = 0.8f)
                            else if (day.isFromCurrentMonth) Color(0xFFFCDDB0)
                            else Color(0xFFFCDDB0).copy(alpha = 0.3f),
                            if (day.isCurrentDay) RoundedCornerShape(50) else RoundedCornerShape(20)
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(bounded = true), // Adds a ripple effect
                        ) {
                            seledtedDay = day
                            showDialog = true
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (day.isCurrentDay) "Today" else day.date.dayOfMonth.toString(),
                        fontSize = if (day.isCurrentDay) 12.sp else 18.sp,
                        fontWeight = if (day.isFromCurrentMonth) FontWeight.Bold else FontWeight.Normal,
                        color = Color(0xFFD86666)
                    )
                }
            }
        )
        DialogBloodBox(
            show = showDialog,
            onDismiss = { showDialog = false },
            day = seledtedDay,
            viewModel = viewModel,
            cycle = cyclePeriod
        )
    }
}

@Composable
fun DialogBloodBox(viewModel: HistoryDateViewModel ,show : Boolean, onDismiss : () -> Unit,
                   day : DayState<EmptySelectionState>?, cycle : Int){
    var rating by remember { mutableStateOf(0) }
    if (show) {
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
                            day?.date?.let { viewModel.saveDate(it,it.plusDays(cycle.toLong())) }
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
        colors = CardDefaults.cardColors(Color(0xFFF5C57E))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Start: ${date.startDate}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "End: ${date.endDate}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}


