package com.example.backgroundremover_changebg.presentation.ui.screens.mixcolors

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
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Splitscreen
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.navigation.NavController
import com.example.backgroundremover_changebg.R
import com.example.backgroundremover_changebg.presentation.ui.screens.bgdetail.saveImage
import com.example.backgroundremover_changebg.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import org.koin.compose.koinInject
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Pic6Screen(navController: NavController) {
    val viewModel: MainViewModel = koinInject()
    val bgRemovedBitmap by viewModel.bgRemovedBitmap.collectAsState()
    val originalBitmap by viewModel.originalBitmap.collectAsState()
    var showOriginalImage by remember { mutableStateOf(true) }
    val scanAnimationOffset = remember { Animatable(0f) }
    val boxHeight = 700.dp
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
            Icon(imageVector = Icons.Outlined.Splitscreen, contentDescription = "")
            Icon(imageVector = Icons.Outlined.Share, contentDescription = "")
        }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White))
    }, bottomBar = {
        BottomAppBar(containerColor = Color.White) {
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp)
                    .height(54.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row {
                    Icon(imageVector = Icons.Outlined.Share, contentDescription = "")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Export", fontWeight = FontWeight.Medium)
                }
            }
        }
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
                        .width(380.dp)
                        .height(600.dp)
                        .border(
                            BorderStroke(
                                1.dp,
                                Color.LightGray
                            ), shape = RoundedCornerShape(13.dp)
                        ), contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .width(370.dp)
                            .height(boxHeight)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.bg6),
                            contentDescription = "",
                            contentScale = ContentScale.Crop
                        )

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
}
