package com.example.backgroundremover_changebg.presentation.ui.screens.bgdetail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.navigation.NavController
import com.example.backgroundremover_changebg.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import org.koin.compose.koinInject
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ColorsBgDetail(navController: NavController) {
    val viewModel: MainViewModel = koinInject()
    val bgRemovedBitmap by viewModel.bgRemovedBitmap.collectAsState()
    val originalBitmap by viewModel.originalBitmap.collectAsState()
    var showOriginalImage by remember { mutableStateOf(true) }
    val scanAnimationOffset = remember { Animatable(0f) }
    val boxHeight = 460.dp
    val scannerHeight = 6.dp
    val context = LocalContext.current
    val revealAnimation = remember { Animatable(0f) }

    LaunchedEffect(showOriginalImage) {
        if (!showOriginalImage) {
            revealAnimation.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1500, easing = LinearEasing)
            )
        }
    }

    LaunchedEffect(Unit) {
        while (showOriginalImage) {
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
    var erasedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(Unit) {
        delay(5000)
        showOriginalImage = false
    }


    Scaffold(topBar = {
        TopAppBar(title = { /*TODO*/ }, navigationIcon = {
            Text(
                text = "Cancel",
                color = Color.Blue,
                modifier = Modifier.clickable { navController.navigateUp() })
        }, actions = {

            Text(text = "Save", color = Color.Magenta, modifier = Modifier
                .blur(0.dp)
                .clickable {
                    saveImage(
                        context,
                        originalBitmap,
                        bgRemovedBitmap,
                        erasedBitmap,
                        Color.White,
                        ""
                    )
                })
        }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White))
    }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            if (showOriginalImage) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .width(370.dp)
                            .height(boxHeight)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        originalBitmap?.let {
                            Image(
                                bitmap = it.asImageBitmap(),
                                contentDescription = "",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.White)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(scannerHeight)
                                .offset(y = with(LocalDensity.current) {
                                    (scanAnimationOffset.value * (boxHeight - scannerHeight)).toPx().dp
                                })
                                .background(Color.Blue)
                                .align(Alignment.TopCenter)
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .width(370.dp)
                        .height(boxHeight)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    bgRemovedBitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(12.dp))
                                .alpha(revealAnimation.value)
                        )
                    }
                }
            }
        }
    }
}


fun addBackgroundColorToImage(bitmap: Bitmap, color: Int): Bitmap {
    val width = bitmap.width
    val height = bitmap.height

    val newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(newBitmap)
    val paint = Paint()

    paint.color = color
    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

    canvas.drawBitmap(bitmap, 0f, 0f, null)

    return newBitmap
}

private fun saveBitmapToFile(bitmap: Bitmap, outputPath: String) {
    FileOutputStream(File(outputPath)).use { out ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
    }
}

private fun notifyMediaScanner(context: Context, file: File) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val contentUri = Uri.fromFile(file)
        mediaScanIntent.data = contentUri
        context.sendBroadcast(mediaScanIntent)
    } else {
        context.contentResolver.notifyChange(Uri.fromFile(file), null)
    }
}

private fun downloadImageWithBackground(context: Context, bitmap: Bitmap, backgroundColor: Color) {
    try {
        val outputPath = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "bg_removed_image.jpg"
        ).absolutePath

        val colorInt = backgroundColor.toArgb()

        val newBitmap = addBackgroundColorToImage(bitmap, colorInt)

        saveBitmapToFile(newBitmap, outputPath)

        val file = File(outputPath)
        notifyMediaScanner(context, file)

        Toast.makeText(context, "Image saved and updated in gallery.", Toast.LENGTH_SHORT).show()

    } catch (e: Exception) {
        Toast.makeText(context, "Save failed: ${e.message}", Toast.LENGTH_LONG).show()
    }
}