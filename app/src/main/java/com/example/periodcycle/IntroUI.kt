package com.example.periodcycle
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun IntroScreen(navController: NavController, onDismiss: () -> Unit) {
    var isVisible by remember { mutableStateOf(false)}
    val alpha: Float by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f, // Target alpha value
        animationSpec = tween(durationMillis =  2000, easing = LinearEasing) // 1-second animation
    )
    val offsetY by animateFloatAsState(
        targetValue = if (isVisible) 0f else -100f, // Target offset value (move up by 100dp)
        animationSpec = tween(durationMillis = 2000, easing = LinearEasing) // 2-second animation
    )
    LaunchedEffect(Unit) {
        delay(150) // Wait for 500ms before starting the animation
        isVisible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onDismiss() }, // Dismiss when the user clicks anywhere
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFFAD7))
                .verticalScroll(rememberScrollState())
                .padding(48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.intropage),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().padding(top = 48.dp)
                    .offset(y = offsetY.dp),
                contentScale = ContentScale.Fit,
                alignment = Alignment.Center,
                alpha = alpha
            )
            Text(
                text = stringResource(id = R.string.Title),
                modifier = Modifier.padding(top = 16.dp),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray.copy(alpha = alpha),
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(id = R.string.subTitle),
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = Color.DarkGray.copy(alpha = alpha)
            )
            Spacer(modifier = Modifier.height(64.dp))
            Text(
                text = stringResource(id = R.string.subTitle2),
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier,
                color = Color.Gray
            )
        }
    }
    LaunchedEffect(Unit) {
        delay(3000) // Delay for 3 seconds
        navController.navigate("main") {
            popUpTo("main") { inclusive = true }
        }
    }
}