package com.example.periodcycle

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay

@Composable
@Preview
fun AccountUI() {
    var water by remember { mutableStateOf(0) }
    LazyColumn(
    ) {
        item {
                Box( //Darker skin color background
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.TopCenter
                )
                {
                    Column (modifier = Modifier
                        .padding(30.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Box(
                            modifier = Modifier
                                .padding(top = 30.dp)
                                .size(130.dp)
                                .background(
                                    color = Color.White, // Light blue color for the circle
                                    shape = CircleShape // Make the Box a circle
                                )
                        )
                        Text(
                            text = "Stevanie",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.ExtraBold, // Make it stand out
                                color = Color.White // A bold reddish-pink color
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 30.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row (horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ){ //Weight and Height
                            Box( //Weight
                                modifier = Modifier
                                    .height(100.dp)
                                    .weight(1f)
                                    .background(
                                        color = Color(0xFFFFFAD7),
                                        shape = RoundedCornerShape(30.dp)
                                    )
                            ) {
                                Row (modifier = Modifier
                                    .padding(10.dp)
                                    .align(Alignment.CenterStart)
                                    .fillMaxWidth(),
                                ){
                                    Image(
                                        painter = painterResource(id = R.drawable.scale),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .weight(0.4f)
                                            .align(Alignment.CenterVertically)
                                    )
                                    Column (modifier = Modifier.weight(0.6f)){
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
                                            text = "-- Kg",
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
                            ){
                                Row (modifier = Modifier
                                    .padding(10.dp)
                                    .align(Alignment.CenterStart)
                                    .fillMaxWidth()
                                ){
                                    Image(
                                        painter = painterResource(id = R.drawable.moods),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .align(Alignment.CenterVertically)
                                            .weight(0.4f)
                                    )
                                    Column (modifier = Modifier.weight(0.6f)){
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
                                            text = "Happy",
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
                            Column (modifier = Modifier
                                .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ){
                                Row (verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.Start),
                                ){
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
                                Row (horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxWidth()){
                                    Box(//average period
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(80.dp)
                                            .background(
                                                color = Color(0xFFff9f9f), // Light blue color for the circle
                                                shape = RoundedCornerShape(30.dp) // Make the Box a circle
                                            )
                                    ){
                                        Column (modifier = Modifier.padding(8.dp)){
                                            Text(
                                                text = "-- Days",
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
                                    ){
                                        Column (modifier = Modifier.padding(8.dp)){
                                            Text(
                                                text = "-- Days",
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
                                Row (modifier = Modifier.padding(8.dp)){
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
                        ){
                            Row (modifier = Modifier
                                .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Image(
                                    painter = painterResource(id = R.drawable.water),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .height(70.dp)
                                        .weight(0.2f)
                                        .align(Alignment.CenterVertically)
                                        .padding(start = 10.dp)
                                )
                                Column (modifier = Modifier.weight(0.8f)){
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
                detectDragGestures { change, dragAmount ->
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