package com.example.periodcycle

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.example.periodcycle.database.UserHistory
import com.example.periodcycle.database.UserHistoryViewModel
import com.example.periodcycle.database.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate


//prepopulate and is if is empty is weird


@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun AccountUI(viewModel: UserViewModel, viewModel2: UserHistoryViewModel) {
    var showDialog by remember { mutableIntStateOf(0) }
    var selectedUnit by remember { mutableStateOf("kg") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val users by viewModel.allUser.collectAsState(initial = emptyList())
    val currentUser by remember { mutableIntStateOf(0) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.saveUserOnce(context)
        viewModel2.getUserHistory()
        viewModel2.saveUserHistoryOnce(context)
    }

    val usersinfo by viewModel2.allUserHistory.collectAsState(initial = emptyList())
    val currentuserInfo by viewModel2.userHistory.collectAsState()

    if (users.isEmpty() && usersinfo.isEmpty()) {
        Text(text = "no user yet")
    } else {
        usersinfo.lastOrNull()?.let {
            if (it.date != LocalDate.now())
                viewModel2.saveInfo(it.weight, it.mood, it.water)
        }
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

//                        if (usersinfo.isEmpty()) {
//                            androidx.compose.material3.Text("No cycles recorded yet.")
//                        } else {
//                            Log.d("DEBUG", "in userinfo there are ${usersinfo.size} ")
//                            usersinfo.forEach { info ->
//                                Text(text = info.id.toString())
//                                Text(text = info.date.toString())
//                                Text(text = info.weight.toString())
//                                Spacer(modifier = Modifier.height(8.dp))
//                            }
//                        }
//
//                        Text(text = currentuserInfo.toString())

                        RequestContentPermission(viewModel = viewModel, user = users[currentUser])

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
                                .align(Alignment.CenterHorizontally),
                            singleLine = true, // If you want to ensure the text stays on one line,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                focusedLabelColor = Color.Transparent,
                                unfocusedLabelColor = Color.Transparent,
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text, // Set the keyboard type
                                imeAction = ImeAction.Done // Set the action on the 'Enter' key
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                    viewModel.UpdateName(
                                        users[currentUser].UserId,
                                        username + ""
                                    )
                                }
                            )
                        )
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) { //Weight and Height
                            Box( //Weight
                                modifier = Modifier
                                    .height(90.dp)
                                    .weight(1f)
                                    .background(
                                        color = Color(0xFFFFFAD7),
                                        shape = RoundedCornerShape(30.dp)
                                    )
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = rememberRipple(bounded = true)
                                    ) {
                                        showDialog = 3
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
//                                            text = "$selectedWeight $selectedUnit",
                                            text = "${currentuserInfo?.weight}  $selectedUnit",
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
                                    .height(90.dp)
                                    .weight(1f)
                                    .background(
                                        color = Color(0xFFFFFAD7), // Light blue color for the circle
                                        shape = RoundedCornerShape(30.dp) // Make the Box a circle
                                    )
                                    .clickable { showDialog = 4 }
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
                                            text = "${currentuserInfo?.mood}",
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
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(//Cycle
                            modifier = Modifier
                                .height(250.dp)
                                .fillMaxWidth()
                                .background(
                                    color = Color(0xFFFFFAD7),
                                    shape = RoundedCornerShape(40.dp)
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
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color(0xFFD86666)
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
                                                color = Color(0xFFff9f9f),
                                                shape = RoundedCornerShape(30.dp)
                                            )
                                            .clickable { showDialog = 1 }
                                    ) {
                                        Column(modifier = Modifier.padding(8.dp)) {
                                            Text(
                                                text = "${users[currentUser].averagePeriod} Days",
                                                style = MaterialTheme.typography.headlineSmall.copy(
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = Color(0xFFFFFAD7)
                                                ),
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                            )
                                            Text(
                                                text = "Average Period",
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = Color.White
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
                                            .clickable { showDialog = 2 }
                                    ) {
                                        Column(modifier = Modifier.padding(8.dp)) {
                                            Text(
                                                text = "${users[currentUser].averageCycle} Days",
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
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = Color.White
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
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color(0xFFD86666)
                                        ),
                                        textAlign = TextAlign.Left,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(//water reminder
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp)
                                .background(
                                    color = Color(0xFFFFFAD7),
                                    shape = RoundedCornerShape(40.dp)
                                )
                                .clickable { showDialog = 5 }
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
                                    Row {
                                        val maxRating = 8
                                        for (i in 1..maxRating) {
                                            Image(
                                                painter = painterResource(id = R.drawable.glass),
                                                contentDescription = "Water $i",
                                                colorFilter = if (i > (currentuserInfo?.water ?: 0)) ColorFilter.tint(Color.Gray) else null,
                                                modifier = Modifier
                                                    .weight(0.1f)
                                                    .padding(2.dp)
                                                    .fillMaxWidth()
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    currentuserInfo?.let {
                        DialogMoodBox(
                            show = showDialog,
                            onDismiss = { showDialog = 0 },
                            viewModel = viewModel2
                        )
                        DialogWaterRating(
                            show = showDialog,
                            onDismiss = { showDialog = 0},
                            currentuserInfo = it,
                            viewModel2 = viewModel2
                        )
                        DialogWeightBox(
                            show = showDialog,
                            onDismiss = { showDialog = 0 },
                            selectedUnit = selectedUnit,
                            onUnitChange = { selectedUnit = it },
                            userInfo = it,
                            viewModel = viewModel2
                        )
                    }
                    DialogCycleBox(
                        viewModel = viewModel,
                        currentUser = currentUser,
                        show = showDialog,
                        onDismiss = { showDialog = 0 },
                        user = users
                    )
                    DialogCycleBox(
                        viewModel = viewModel,
                        currentUser = currentUser,
                        show = showDialog,
                        onDismiss = { showDialog = 0 },
                        user = users
                    )
                }
            }

        }
    }
}

@Composable
fun DialogWaterRating(
    show: Int,
    onDismiss: () -> Unit,
    currentuserInfo : UserHistory,
    viewModel2: UserHistoryViewModel
){
    if (show == 5) {
        var waterNumber by remember { mutableIntStateOf(currentuserInfo.water) }
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
                        text = "Drink Enough Water Today?",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    currentuserInfo?.let {
                        WaterRating(
                            onWaterSelected = { waterNumber = it },
                            water = waterNumber
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            onDismiss()
                            viewModel2.UpdataWater(waterNumber)
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

@Composable
fun WaterRating(
    onWaterSelected: (Int) -> Unit,
    water : Int
) {
    val maxRating = 8 // Maximum number of stars
    Box(
        modifier = Modifier
            .fillMaxWidth()
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
                        .clickable {
                            onWaterSelected(i)
                        }
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun DialogWeightBox(
    show: Int,
    onDismiss: () -> Unit,
    selectedUnit: String,
    onUnitChange: (String) -> Unit,
    userInfo: UserHistory,
    viewModel: UserHistoryViewModel
) {
    if (show == 3) {
        Dialog(onDismissRequest = { onDismiss() }) {

            val listState =
                rememberLazyListState(initialFirstVisibleItemIndex = userInfo.weight - 31)
            val visibleWeight by remember {
                derivedStateOf { listState.firstVisibleItemIndex + 31 }
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
                                        fontSize = if (weight == visibleWeight) 24.sp else 18.sp,
                                        fontWeight = if (weight == visibleWeight) FontWeight.Bold else FontWeight.Normal,
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
                        onClick = {
                            onDismiss()
                            viewModel.UpdataWeight(visibleWeight)
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

@Composable
fun DialogMoodBox(
    show: Int,
    onDismiss: () -> Unit,
    viewModel: UserHistoryViewModel,
) {
    if (show == 4) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Surface(
                modifier = Modifier.wrapContentSize(),
                shape = MaterialTheme.shapes.small,
                color = Color(0xFFFFD495)
            ) {
                var selected by remember { mutableStateOf(" ") }
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
                                    "Sweets"
                                )
                            ) { unit ->
                                Button(
                                    onClick = {
                                        selected = unit
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (selected == unit) Color(
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
                        onClick = {
                            onDismiss()
                            viewModel.UpdataMood(selected)
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

@Composable
fun DialogCycleBox(
    viewModel: UserViewModel,
    currentUser: Int,
    show: Int,
    onDismiss: () -> Unit,
    user: List<UserData>
) {
    if (show == 1 || show == 2) {
        Dialog(onDismissRequest = { onDismiss() }) {

            val nowHandleWhat: Int = if (show == 1) user[currentUser].averagePeriod
            else user[currentUser].averageCycle

            val listState = rememberLazyListState(initialFirstVisibleItemIndex = nowHandleWhat - 1)

            val visibleDays by remember {
                derivedStateOf { listState.firstVisibleItemIndex + 1 }
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
                                    fontSize = if (weight == visibleDays) 24.sp else 18.sp,
                                    fontWeight = if (weight == visibleDays) FontWeight.Bold else FontWeight.Normal,
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
                            if (show == 1) viewModel.UpdataPeriod(
                                user[currentUser].UserId,
                                visibleDays
                            )
                            else viewModel.UpdataCycle(user[currentUser].UserId, visibleDays)
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

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun RequestContentPermission(
    viewModel: UserViewModel,
    user :UserData
) {
    var imageUriString by remember { mutableStateOf(user.profilePicture) }
    val context = LocalContext.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(
                it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            imageUriString = it.toString() // Update final source
            viewModel.UpdataPicture(id = user.UserId, picture = imageUriString!!) // Save to backend
        }
    }
    LaunchedEffect(imageUriString) {
        imageUriString?.let { uriStr ->
            val uri = Uri.parse(uriStr)
            try {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                bitmap.value = ImageDecoder.decodeBitmap(source)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    Box(
        modifier = Modifier
            .padding(top = 10.dp)
            .size(120.dp)
            .clip(CircleShape)
            .background(Color.White)
            .clickable {
                launcher.launch(arrayOf("image/*"))
            },
        contentAlignment = Alignment.Center
    ) {
        if (bitmap.value != null) {
            Image(
                bitmap = bitmap.value!!.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                imageVector = Icons.Default.AddCircle,
                contentDescription = "Add Photo",
                tint = Color.Gray,
                modifier = Modifier.size(40.dp)
            )
        }


    }
}

