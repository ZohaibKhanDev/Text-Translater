package com.example.backgroundremover_changebg.presentation.ui.screens.bgdetail

import android.content.Context
import android.graphics.RenderEffect
import android.graphics.Shader
import android.graphics.Typeface
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavController
import com.example.backgroundremover_changebg.R
import com.example.backgroundremover_changebg.presentation.viewmodel.MainViewModel
import ja.burhanrashid52.photoeditor.OnPhotoEditorListener
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.PhotoEditorView
import ja.burhanrashid52.photoeditor.ViewType
import ja.burhanrashid52.photoeditor.shape.ShapeBuilder
import ja.burhanrashid52.photoeditor.shape.ShapeType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun BlurScreen(navController: NavController) {
    val viewModel: MainViewModel = koinInject()
    val bitmap by viewModel.originalBitmap.collectAsState()

    var isBlurred by remember { mutableStateOf(false) }
    val scanAnimationOffset = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        launch {
            while (!isBlurred) {
                scanAnimationOffset.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
                )
                scanAnimationOffset.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
                )
            }
        }

        delay(7000)
        isBlurred = true
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
                    .height(770.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .then(
                        if (isBlurred) Modifier.blur(10.dp)
                        else Modifier
                    )
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .offset(y = with(LocalDensity.current) {
                        (scanAnimationOffset.value * 890).dp
                    })
                    .background(Color.Blue)
                    .align(Alignment.TopCenter)
            )
        }
    }
}




