package com.example.periodcycle

import android.os.Build.VERSION.SDK_INT
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import kotlinx.coroutines.delay

@Composable
fun HomePageUi() {
    SunAnimation()
}

@Composable
fun SunAnimation() {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) { //android 9
                add(ImageDecoderDecoder.Factory())
            } else { //older version
                add(GifDecoder.Factory())
            }
        }
        .build()
    // State to track the sun's position
    var sunPosition by remember { mutableStateOf(Offset.Zero) }
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp.value

    // Animation to move the sun from left to right
    LaunchedEffect(Unit) {
        val animationDuration = 5000L  // 5 seconds
        val amplitude = 200f  // Height of the "U" curve
        val endX = screenWidth - 100f  // Ending X position

        while (true) {
            sunPosition = Offset(0f, 0f) // Start from the left
            animate(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = tween(durationMillis = animationDuration.toInt(), easing = LinearEasing)
            ) { progress, _ ->
                val x = progress * endX
                val y = amplitude * (4 * progress * (1 - progress))
                sunPosition = Offset(x, y)
            }.also{delay(1000)}
        }
    }
    // UI layout
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(context).data(data = R.drawable.untitled_design).apply(block = {
                    size(coil.size.Size.ORIGINAL)
                }).build(), imageLoader = imageLoader
            ),
            contentDescription = null,
            modifier = Modifier
                .offset(sunPosition.x.dp, sunPosition.y.dp)
        )
    }
}


