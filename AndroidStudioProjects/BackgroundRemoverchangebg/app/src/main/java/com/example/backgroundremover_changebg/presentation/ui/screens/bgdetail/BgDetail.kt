package com.example.backgroundremover_changebg.presentation.ui.screens.bgdetail

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BlendMode
import android.graphics.Canvas
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.PhotoSizeSelectActual
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SavedSearch
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.backgroundremover_changebg.R
import com.example.backgroundremover_changebg.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.compose.koinInject
import java.net.URL

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UseOfNonLambdaOffsetOverload")
@Composable
fun BgDetailScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: MainViewModel = koinInject()
    val originalBitmap by viewModel.originalBitmap.collectAsState()
    val bgRemovedBitmap by viewModel.bgRemovedBitmap.collectAsState()
    var showBgRemovedImage by remember { mutableStateOf(false) }
    var isZoomSelected by remember { mutableStateOf(false) }
    var isEraseSelected by remember { mutableStateOf(false) }
    var isBrushSelected by remember { mutableStateOf(false) }
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    val scaleFactor = remember { Animatable(1f) }
    val scanAnimationOffset = remember { Animatable(0f) }
    val canvasPath = remember { Path() }
    var erasedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val eraserColor = Color.Transparent
    var brushSize by remember { mutableStateOf(30f) }
    var brushOpacity by remember { mutableStateOf(1f) }
    var brushIndicatorPosition by remember { mutableStateOf(Offset.Zero) }
    val revealAnimationOffset = remember { Animatable(-500f) }
    var showResetDialog by remember { mutableStateOf(false) }
    var showColorSheet by remember { mutableStateOf(false) }
    var selectedColor by remember { mutableStateOf<Color?>(null) }

    val colorsList = listOf(
        Color(0xFFFF0000),
        Color(0xFF0000FF),
        Color(0xFF00FF00),
        Color(0xFFFFFF00),
        Color(0xFFFF00FF),
        Color(0xFF00FFFF),
        Color(0xFF000000),
        Color(0xFF808080),
        Color(0xFFFFFFFF),
        Color(0xFFD3D3D3),
        Color(0xFFA9A9A9),
        Color(0xFFFFA500),
        Color(0xFF800080),
        Color(0xFFFFC0CB),
        Color(0xFFA52A2A),
        Color(0xFF008080),
        Color(0xFF90EE90),
        Color(0xFF00FF00),
        Color(0xFF8F00FF),
        Color(0xFFFF7F50),
        Color(0xFFFFD700),
        Color(0xFF4B0082),
        Color(0xFF40E0D0)
    )

    val photoList = listOf(
        "https://w0.peakpx.com/wallpaper/415/344/HD-wallpaper-curves-bg-wp-abstract.jpg",
        "https://c0.wallpaperflare.com/preview/387/101/108/mountains-landscape-wallpaper-retina.jpg",
        "https://himfenalarms.co.uk/wp-content/uploads/2014/04/bokeh-cover-bg.jpg",
        "https://w0.peakpx.com/wallpaper/159/815/HD-wallpaper-mira-bg-black-abstract-dark.jpg",
        "https://www.nycwatershed.org/wp-content/uploads/2012/09/bg-prlx-big-sky1.jpg",
        "https://images.pexels.com/photos/949587/pexels-photo-949587.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://img.freepik.com/free-vector/realistic-neon-lights-background_23-2148907367.jpg",
        "https://png.pngtree.com/thumb_back/fh260/background/20200714/pngtree-modern-double-color-futuristic-neon-background-image_351866.jpg",
        "https://i.pinimg.com/564x/6a/aa/ab/6aaaab354709ef2fa16fbd72299c8f55.jpg",
        "https://st5.depositphotos.com/35914836/63547/i/450/depositphotos_635479512-stock-photo-brown-wooden-wall-texture-background.jpg",
        "https://plus.unsplash.com/premium_photo-1701520913496-503a71946555?fm=jpg&q=60&w=3000&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8cHJlc2VudGF0aW9uJTIwYmFja2dyb3VuZHxlbnwwfHwwfHx8MA%3D%3D",
        "https://img.freepik.com/free-vector/gradient-dynamic-blue-lines-background_23-2148995756.jpg",
        "https://t3.ftcdn.net/jpg/06/15/96/36/360_F_615963650_DeXVvEIB5Cc7WN5Z9Y51ElbjN81HHMYy.jpg",
        "https://wallpapers.com/images/hd/selfie-background-1366-x-768-o8qusz5dcxc061tp.jpg",
        "https://png.pngtree.com/background/20220714/original/pngtree-transparent-shape-blue-background-with-circle-in-corner-picture-image_1606163.jpg"
    )

    var showPhotosSheet by remember {
        mutableStateOf(false)
    }

    var selectedPhoto by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = true) {
        launch {
            while (!showBgRemovedImage) {
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

        delay(9000)
        showBgRemovedImage = true

        revealAnimationOffset.animateTo(
            targetValue = 0f, animationSpec = tween(durationMillis = 2000, easing = LinearEasing)
        )
    }

    Scaffold(topBar = {
        TopAppBar(title = { }, navigationIcon = {
            Icon(imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "",
                modifier = Modifier.clickable { navController.navigateUp() })
        }, actions = {
            Text(text = "Save", color = Color.Magenta, modifier = Modifier.clickable {
                saveImage(
                    context,
                    originalBitmap,
                    bgRemovedBitmap,
                    erasedBitmap,
                    selectedColor,
                    selectedPhoto
                )
            })
        })
    }, bottomBar = {
        BottomAppBar(containerColor = Color.White) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .wrapContentWidth()
                        .clickable {
                            isZoomSelected = !isZoomSelected
                            isEraseSelected = false
                            isBrushSelected = false
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = Icons.Default.SavedSearch,
                        contentDescription = "",
                        tint = if (isZoomSelected) Color.Blue else Color.Black
                    )

                    Text(text = "Zoom", color = if (isZoomSelected) Color.Blue else Color.Black)
                }

                Column(
                    modifier = Modifier
                        .wrapContentWidth()
                        .clickable {
                            isEraseSelected = !isEraseSelected
                            isZoomSelected = false
                            isBrushSelected = false
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {

                    Icon(
                        painter = painterResource(id = R.drawable.eraser),
                        contentDescription = "",
                        tint = if (isEraseSelected) Color.Blue else Color.Black
                    )
                    Text(text = "Erase", color = if (isEraseSelected) Color.Blue else Color.Black)
                }


                Column(modifier = Modifier
                    .wrapContentWidth()
                    .clickable {
                        isZoomSelected = false
                        isEraseSelected = false
                        isBrushSelected = false
                        showColorSheet = true
                    }) {
                    Icon(imageVector = Icons.Default.ColorLens, contentDescription = "")

                    Text(text = "Color")
                }

                Column(modifier = Modifier
                    .wrapContentWidth()
                    .clickable {
                        isZoomSelected = false
                        isEraseSelected = false
                        isBrushSelected = false
                        showPhotosSheet = true
                    }) {
                    Icon(imageVector = Icons.Default.PhotoSizeSelectActual, contentDescription = "")

                    Text(text = "Photo")
                }

                Column(
                    modifier = Modifier
                        .wrapContentWidth()
                        .clickable { showResetDialog = true }) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = "")

                    Text(text = "Reset")
                }
            }
        }
    }) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 30.dp)
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(500.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            if (isZoomSelected) {
                                scale = (scale * zoom).coerceIn(1f, 5f)
                                offsetX += pan.x
                                offsetY += pan.y
                            }
                        }
                    }, contentAlignment = Alignment.Center
            ) {

                if (selectedColor != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(selectedColor!!)
                    )
                } else {

                    if (selectedPhoto != null) {
                        Box(
                            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = selectedPhoto,
                                contentDescription = "",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    } else {

                        Image(
                            painter = painterResource(id = R.drawable.transparentbg),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                AnimatedVisibility(
                    visible = showBgRemovedImage,
                    enter = fadeIn(animationSpec = tween(1000)),
                    exit = fadeOut(animationSpec = tween(1000))
                ) {
                    bgRemovedBitmap?.let { bgBitmap ->
                        if (isEraseSelected) {
                            erasedBitmap = bgBitmap.eraseOnCanvas(
                                canvasPath.asAndroidPath(), eraserColor, brushOpacity
                            )
                        }

                        Image(bitmap = (erasedBitmap ?: bgBitmap).asImageBitmap(),
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer {
                                    clip = true
                                    alpha = if (showBgRemovedImage) 1f else 0f
                                    translationY = revealAnimationOffset.value
                                    scaleX = scale
                                    scaleY = scale
                                    translationX = offsetX
                                    translationY = offsetY
                                }
                                .pointerInput(isEraseSelected) {
                                    if (isEraseSelected) {
                                        detectDragGestures { change, _ ->
                                            change.consume()
                                            val brushPos = change.position
                                            canvasPath.addOval(
                                                Rect(
                                                    brushPos - Offset(brushSize, brushSize),
                                                    brushPos + Offset(brushSize, brushSize)
                                                )
                                            )
                                            brushIndicatorPosition = brushPos
                                        }
                                    }
                                },
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            if (!showBgRemovedImage) {
                originalBitmap?.let { bitmap ->
                    Box(
                        modifier = Modifier
                            .size(500.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .offset(y = with(LocalDensity.current) {
                                    (scanAnimationOffset.value * 500).dp
                                })
                                .background(Color.Blue)
                                .align(Alignment.TopCenter)
                        )
                    }
                }
            }

            if (isEraseSelected) {
                Box(
                    modifier = Modifier
                        .size(brushSize.dp)
                        .offset(x = with(LocalDensity.current) {
                            (brushIndicatorPosition.x - brushSize / 2).toDp()
                        }, y = with(LocalDensity.current) {
                            (brushIndicatorPosition.y - brushSize / 2).toDp()
                        })
                        .background(Color.Red.copy(alpha = 0.5f), shape = RoundedCornerShape(50))
                )
            }
        }

        if (isEraseSelected) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Brush Size")
                Slider(
                    value = brushSize, onValueChange = { brushSize = it }, valueRange = 30f..200f
                )

                Text(text = brushSize.toInt().toString())
            }
        }

        if (showResetDialog) {
            AlertDialog(onDismissRequest = { showResetDialog = false },
                title = { Text("Reset") },
                text = { Text("Do you want to reset the editing?") },
                confirmButton = {
                    TextButton(onClick = {
                        showResetDialog = false
                        canvasPath.reset()
                        erasedBitmap = null
                        selectedColor = null
                        selectedPhoto = null
                        scale = 1f
                        offsetX = 0f
                        offsetY = 0f
                    }) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showResetDialog = false }) {
                        Text("No")
                    }
                })
        }
    }

    if (showColorSheet) {
        ModalBottomSheet(onDismissRequest = { showColorSheet = false }) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4), contentPadding = PaddingValues(16.dp)
            ) {
                items(colorsList) { color ->
                    Box(modifier = Modifier
                        .padding(4.dp)
                        .width(12.dp)
                        .height(54.dp)
                        .background(color, shape = RoundedCornerShape(12.dp))
                        .clickable {
                            selectedPhoto = null
                            selectedColor = color
                            showColorSheet = false
                        })
                }
            }
        }
    }

    if (showPhotosSheet) {
        ModalBottomSheet(onDismissRequest = { showPhotosSheet = false }) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(photoList) { photoRes ->
                    Box(modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .padding(4.dp)
                        .clickable {
                            selectedColor = null
                            selectedPhoto = photoRes
                            showPhotosSheet = false
                        }) {
                        AsyncImage(
                            model = photoRes,
                            contentDescription = "Photo",
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(12.dp)),
                            placeholder = painterResource(R.drawable.aibg_remove),
                            error = painterResource(R.drawable.aishadow),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("NewApi")
fun Bitmap.eraseOnCanvas(path: android.graphics.Path, eraserColor: Color, opacity: Float): Bitmap {
    val newBitmap = copy(config, true)
    val canvas = android.graphics.Canvas(newBitmap)
    val paint = android.graphics.Paint().apply {
        color = android.graphics.Color.TRANSPARENT
        alpha = (opacity * 255).toInt()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            blendMode = BlendMode.CLEAR
        }
        isAntiAlias = true
    }
    canvas.drawPath(path, paint)
    return newBitmap
}


fun saveImage(
    context: Context,
    originalBitmap: Bitmap?,
    bgRemovedBitmap: Bitmap?,
    erasedBitmap: Bitmap?,
    selectedColor: Color?,
    selectedPhoto: String?
) {
    createNotificationChannel(context)
    showNotification(context, 0, true)

    CoroutineScope(Dispatchers.IO).launch {
        val bitmap = withContext(Dispatchers.Default) {
            val width = 500
            val height = 500
            val resultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(resultBitmap)

            selectedPhoto?.let {
                try {
                    val photoBitmap = BitmapFactory.decodeStream(URL(it).openStream())
                    canvas.drawBitmap(photoBitmap, 0f, 0f, null)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } ?: run {
                selectedColor?.let {
                    canvas.drawColor(it.toArgb())
                }
            }

            bgRemovedBitmap?.let {
                canvas.drawBitmap(it, 0f, 0f, null)
            }

            erasedBitmap?.let {
                canvas.drawBitmap(it, 1f, 1f, null)
            }

            resultBitmap
        }

        saveBitmapToStorage(context, bitmap)

        withContext(Dispatchers.Main) {
            updateNotificationProgress(context, 100)
            dismissNotification(context)
        }
    }
}


private fun saveBitmapToStorage(context: Context, bitmap: Bitmap) {
    val resolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(
            MediaStore.Images.Media.DISPLAY_NAME,
            "bg_removed_image_${System.currentTimeMillis()}.png"
        )
        put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
    }
    val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

    uri?.let {
        resolver.openOutputStream(it)?.use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        }
    }
}


private fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channelId = "image_save_channel"
        val channelName = "Image Save Notifications"
        val channelDescription = "Notifications for image saving progress"

        val channel = NotificationChannel(
            channelId, channelName, NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = channelDescription
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}


private fun showNotification(context: Context, progress: Int, isIndeterminate: Boolean) {
    val channelId = "image_save_channel"
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val notificationBuilder =
        NotificationCompat.Builder(context, channelId).setContentTitle("Saving Image")
            .setContentText("Saving image to gallery...")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_LOW).setOngoing(true)
    if (isIndeterminate) {
        notificationBuilder.setProgress(0, 0, true)
    } else {
        notificationBuilder.setProgress(100, progress, false)
    }
    notificationManager.notify(1, notificationBuilder.build())
}

private fun updateNotificationProgress(context: Context, progress: Int) {
    showNotification(context, progress, false)
}


private fun dismissNotification(context: Context) {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.cancel(1)
}
