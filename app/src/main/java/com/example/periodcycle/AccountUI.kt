package com.example.periodcycle

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.periodcycle.database.UserData


//prepopulate and is if is empty is weird


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountUI(viewModel: UserViewModel) {
    var water by remember { mutableIntStateOf(0) }
    var showDialogAcc by remember { mutableStateOf(false) }
    var showDialogMoods by remember { mutableStateOf(false) }
    var showDialogCycle by remember { mutableStateOf(0) }
    var selectedWeight by remember { mutableIntStateOf(60) }
    var selectedUnit by remember { mutableStateOf("kg") }
    var selectedMood by remember { mutableStateOf("Happy") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val users by viewModel.allUser.collectAsState(initial = emptyList())
    var currentUser by remember { mutableIntStateOf(0) }
//    if (users.isEmpty()) viewModel.saveUser(1,"user",7,30)

    LazyColumn {
        item {
            Box( //Darker skin color background
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            )
            {
                Column(
                    modifier = Modifier
                        .padding(30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .padding(top = 30.dp)
                            .size(130.dp)
                            .background(
                                color = Color.White, // Light blue color for the circle
                                shape = CircleShape // Make the Box a circle
                            )
                    )
                    if(users.isEmpty()) { Text(text = "asdasdddddddddddddddd")}
                    else {
                        var username by remember { mutableStateOf(users[currentUser].name) }

                        TextField(
                            value = username,
                            onValueChange = {
                                username = it
                            },
                            textStyle = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.ExtraBold, // Make it stand out
                                color = Color.White, // Text color
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .align(Alignment.CenterHorizontally),
                            singleLine = true, // If you want to ensure the text stays on one line,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedLabelColor = Color.Transparent,
                                unfocusedLabelColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text, // Set the keyboard type
                                imeAction = ImeAction.Done // Set the action on the 'Enter' key
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                    viewModel.UpdateName(users[currentUser].UserId, username + "")
                                }
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) { //Weight and Height
                        Box( //Weight
                            modifier = Modifier
                                .height(100.dp)
                                .weight(1f)
                                .background(
                                    color = Color(0xFFFFFAD7),
                                    shape = RoundedCornerShape(30.dp)
                                )
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = rememberRipple(bounded = true)
                                ) {
                                    showDialogAcc = true
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .align(Alignment.CenterStart)
                                    .fillMaxWidth(),
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.scale),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .weight(0.4f)
                                        .align(Alignment.CenterVertically)
                                )
                                Column(modifier = Modifier.weight(0.6f)) {
                                    Text(
                                        text = "Weight",
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontWeight = FontWeight.Bold, // Make it stand out
                                            color = Color(0xFFE97777) // A bold reddish-pink color
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    )
                                    Text(
                                        text = "$selectedWeight $selectedUnit",
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 23.sp,// Make it stand out
                                        color = Color(0xFFE97777), // A bold reddish-pink color ,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Box(//Moods
                            modifier = Modifier
                                .height(100.dp)
                                .weight(1f)
                                .background(
                                    color = Color(0xFFFFFAD7), // Light blue color for the circle
                                    shape = RoundedCornerShape(30.dp) // Make the Box a circle
                                )
                                .clickable { showDialogMoods = true }
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .align(Alignment.CenterStart)
                                    .fillMaxWidth()
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.moods),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .weight(0.4f)
                                )
                                Column(modifier = Modifier.weight(0.6f)) {
                                    Text(
                                        text = "Moods",
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontWeight = FontWeight.Bold, // Make it stand out
                                            color = Color(0xFFE97777) // A bold reddish-pink color
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    )
                                    Text(
                                        text = selectedMood,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 20.sp,// Make it stand out
                                        color = Color(0xFFE97777), // A bold reddish-pink color ,
                                        textAlign = TextAlign.Left,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(//Cycle
                        modifier = Modifier
                            .height(250.dp)
                            .fillMaxWidth()
                            .background(
                                color = Color(0xFFFFFAD7), // Light blue color for the circle
                                shape = RoundedCornerShape(40.dp) // Make the Box a circle
                            )
                    )
                    {// Cycle content
                        Column(
                            modifier = Modifier
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentWidth(Alignment.Start),
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.cycle),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .weight(0.2f)
                                )
                                Text(
                                    text = "Cycle",
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.ExtraBold, // Make it stand out
                                        color = Color(0xFFD86666) // A bold reddish-pink color
                                    ),
                                    textAlign = TextAlign.Left,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 8.dp)
                                        .weight(0.8f)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Box(//average period
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(80.dp)
                                        .background(
                                            color = Color(0xFFff9f9f), // Light blue color for the circle
                                            shape = RoundedCornerShape(30.dp) // Make the Box a circle
                                        )
                                        .clickable { showDialogCycle = 1 }
                                ) {
                                    var _period by remember { mutableStateOf(" ")}
                                    if (users.isEmpty()) { Text(text = "asdasdddddddddddddddd")}
                                    else {_period = users[currentUser].averagePeriod.toString() }
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Text(
                                            text = "$_period Days",
                                            style = MaterialTheme.typography.headlineSmall.copy(
                                                fontWeight = FontWeight.ExtraBold, // Make it stand out
                                                color = Color(0xFFFFFAD7)// A bold reddish-pink color
                                            ),
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                        )
                                        Text(
                                            text = "Average Period",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.ExtraBold, // Make it stand out
                                                color = Color.White// A bold reddish-pink color
                                            ),
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 4.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Box(//average Cycle
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(80.dp)
                                        .background(
                                            color = Color(0xFFFFBE5E),
                                            shape = RoundedCornerShape(30.dp)
                                        )
                                        .clickable { showDialogCycle = 2 }
                                ) {
                                    var _cycle by remember { mutableStateOf(" ")}
                                    if (users.isEmpty()) { Text(text = "asdasdddddddddddddddd")}
                                    else {_cycle = users[currentUser].averageCycle.toString() }
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Text(
                                            text = "$_cycle Days",
                                            style = MaterialTheme.typography.headlineSmall.copy(
                                                fontWeight = FontWeight.ExtraBold, // Make it stand out
                                                color = Color(0xFFFFFAD7)// A bold reddish-pink color
                                            ),
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                        )
                                        Text(
                                            text = "Average Cycle",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.ExtraBold, // Make it stand out
                                                color = Color.White// A bold reddish-pink color
                                            ),
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 4.dp)
                                        )
                                    }
                                }
                            }
                            Row(modifier = Modifier.padding(8.dp)) {
                                Text(
                                    text = "Blood Flow",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold, // Make it stand out
                                        color = Color(0xFFD86666)// A bold reddish-pink color
                                    ),
                                    textAlign = TextAlign.Left,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(//water reminder
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp)
                            .background(
                                color = Color(0xFFFFFAD7), // Light blue color for the circle
                                shape = RoundedCornerShape(40.dp) // Make the Box a circle
                            )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.water),
                                contentDescription = null,
                                modifier = Modifier
                                    .height(70.dp)
                                    .weight(0.2f)
                                    .align(Alignment.CenterVertically)
                                    .padding(start = 10.dp)
                            )
                            Column(modifier = Modifier.weight(0.8f)) {
                                Text(
                                    text = "Water Reminder",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color(0xFFD86666)
                                    ),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )
                                WaterRating(
                                    water = water,
                                    onWaterChange = { newRating -> water = newRating }
                                )
                            }
                        }
                    }
                }
                DialogWeightBox(
                    show = showDialogAcc,
                    onDismiss = { showDialogAcc = false },
                    selectedWeight = selectedWeight,
                    selectedUnit = selectedUnit,
                    onWeightChange = { selectedWeight = it },
                    onUnitChange = { selectedUnit = it },
                )
                DialogMoodBox(
                    show = showDialogMoods,
                    onDismiss = { showDialogMoods = false },
                    selectedMood = selectedMood,
                    onMoodChange = { selectedMood = it }
                )
                DialogCycleBox(
                    viewModel = viewModel,
                    currentUser = currentUser,
                    show = showDialogCycle,
                    onDismiss = { showDialogCycle = 0 },
                    user = users
//                    selectedDays = selectedCyclePeriod,
//                    onDaysChange = { newDays -> onPeriodChange(newDays) },
                )
                DialogCycleBox(
                    viewModel = viewModel,
                    currentUser = currentUser,
                    show = showDialogCycle,
                    onDismiss = { showDialogCycle = 0 },
                    user = users
//                    selectedDays = selectedCycleRemain,
//                    onDaysChange = { newDays -> onRemainChange(newDays) },
                )
            }
        }

    }
}

@Composable
fun WaterRating(
    water: Int,
    onWaterChange: (Int) -> Unit // Callback to update the rating
) {
    val maxRating = 8 // Maximum number of stars
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    val newWater = (change.position.x / size.width) * maxRating
                    onWaterChange(
                        newWater
                            .coerceIn(0f, maxRating.toFloat())
                            .toInt()
                    )
                }
            }
    ) {
        Row {
            for (i in 1..maxRating) {
                Image(
                    painter = painterResource(id = R.drawable.glass),
                    contentDescription = "Water $i",
                    colorFilter = if (i > water) ColorFilter.tint(Color.Gray) else null,
                    modifier = Modifier
                        .weight(0.1f)
                        .padding(2.dp)
                        .clickable { onWaterChange(i) }
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun DialogWeightBox(
    show: Boolean,
    onDismiss: () -> Unit,
    selectedWeight: Int,
    selectedUnit: String,
    onWeightChange: (Int) -> Unit,
    onUnitChange: (String) -> Unit,
) {
    if (show) {
        Dialog(onDismissRequest = { onDismiss() }) {

            val listState =
                rememberLazyListState(initialFirstVisibleItemIndex = selectedWeight - 31)
            val visibleWeight by remember {
                derivedStateOf { listState.firstVisibleItemIndex + 31 }
            }

            LaunchedEffect(visibleWeight) {
                onWeightChange(visibleWeight)
            }

            Surface(
                modifier = Modifier.wrapContentSize(),
                shape = MaterialTheme.shapes.small,
                color = Color(0xFFFFD495)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    androidx.compose.material3.Text(
                        text = "How Much Weight?",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        //weight number
                        Box(
                            modifier = Modifier.height(120.dp)
                        ) {
                            LazyColumn(
                                state = listState,
                                modifier = Modifier.height(120.dp)
                            ) {
                                items((30..200).toList()) { weight ->
                                    Text(
                                        text = weight.toString(),
                                        fontSize = if (weight == selectedWeight) 24.sp else 18.sp,
                                        fontWeight = if (weight == selectedWeight) FontWeight.Bold else FontWeight.Normal,
                                        modifier = Modifier.padding(4.dp),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                        // Weight Unit Selector (kg/lb)
                        Column(
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            listOf("kg", "lb").forEach { unit ->
                                Button(
                                    onClick = { onUnitChange(unit) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (selectedUnit == unit) Color(0xFFE97777) else Color(
                                            0xFFFF9F9F
                                        )
                                    ),
                                    modifier = Modifier.padding(4.dp)
                                ) {
                                    Text(unit, color = Color.White)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    androidx.compose.material3.Text("Track weight to record your body!")
                    Button(
                        onClick = { onDismiss() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE97777))
                    ) {
                        androidx.compose.material3.Text("Done!")
                    }

                }
            }
        }
    }
}

@Composable
fun DialogMoodBox(
    show: Boolean,
    onDismiss: () -> Unit,
    selectedMood: String,
    onMoodChange: (String) -> Unit,
) {
    if (show) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Surface(
                modifier = Modifier.wrapContentSize(),
                shape = MaterialTheme.shapes.small,
                color = Color(0xFFFFD495)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    androidx.compose.material3.Text(
                        text = "How Your Mood?",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier.height(400.dp)
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(120.dp)
                        ) {
                            items(
                                listOf(
                                    "Happy", "Sad", "Exited", "Confuse", "Lazy", "Craving",
                                    "Ice Cream"
                                )
                            ) { unit ->
                                Button(
                                    onClick = { onMoodChange(unit) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (selectedMood == unit) Color(
                                            0xFFE97777
                                        ) else Color(0xFFFF9F9F)
                                    ),
                                    modifier = Modifier.padding(4.dp)
                                ) {
                                    Text(unit, color = Color.White)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    androidx.compose.material3.Text("Track moods to record your body!")
                    Button(
                        onClick = { onDismiss() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE97777))
                    ) {
                        androidx.compose.material3.Text("Done!")
                    }

                }
            }
        }
    }
}

@Composable
fun DialogCycleBox(
    viewModel: UserViewModel,
    currentUser : Int,
    show: Int,
    onDismiss: () -> Unit,
    user: List<UserData>
//    selectedDays: Int,
//    onDaysChange: (Int) -> Unit,
) {
    if (show != 0) {
        Dialog(onDismissRequest = { onDismiss() }) {

            var nowHandleWhat = 0
            if (show.equals(1)) nowHandleWhat = user[currentUser].averagePeriod!!
            else nowHandleWhat = user[currentUser].averageCycle!!

            val listState = rememberLazyListState(initialFirstVisibleItemIndex = nowHandleWhat - 1)

            val visibleDays by remember {
                derivedStateOf { listState.firstVisibleItemIndex + 1 }
            }

//            LaunchedEffect(visibleDays) {
//                onDaysChange(visibleDays)
//            }

            Surface(
                modifier = Modifier.wrapContentSize(),
                shape = MaterialTheme.shapes.small,
                color = Color(0xFFFFD495)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    androidx.compose.material3.Text(
                        text = "How Many Days?",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier.height(120.dp)
                    ) {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier.height(120.dp)
                        ) {
                            items((0..100).toList()) { weight ->
                                Text(
                                    text = weight.toString(),
                                    fontSize = if (weight == nowHandleWhat) 24.sp else 18.sp,
                                    fontWeight = if (weight == nowHandleWhat) FontWeight.Bold else FontWeight.Normal,
                                    modifier = Modifier.padding(4.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    androidx.compose.material3.Text("Track Cycle to record your body!")
                    Button(
                        onClick = {
                            onDismiss()
                            if (show.equals(1)) viewModel.UpdataPeriod(user[currentUser].UserId,visibleDays)
                            else viewModel.UpdataCycle(user[currentUser].UserId,visibleDays)
                                  },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE97777))
                    ) {
                        androidx.compose.material3.Text("Done!")
                    }

                }
            }
        }
    }
}