package com.example.backgroundremover_changebg.presentation.ui.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.backgroundremover_changebg.R
import com.example.backgroundremover_changebg.presentation.ui.navigation.Screens
import com.example.backgroundremover_changebg.presentation.viewmodel.MainViewModel
import dev.eren.removebg.RemoveBg
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CreateScreen(navController: NavController) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    val context = LocalContext.current
    var inputImage by remember { mutableStateOf<Bitmap?>(null) }
    var outputImage by remember { mutableStateOf<Bitmap?>(null) }
    var colorsInputImage by remember { mutableStateOf<Bitmap?>(null) }
    var BlurInputImage by remember { mutableStateOf<Bitmap?>(null) }
    var pic1InputImage by remember { mutableStateOf<Bitmap?>(null) }
    var pic2InputImage by remember { mutableStateOf<Bitmap?>(null) }
    var pic3InputImage by remember { mutableStateOf<Bitmap?>(null) }
    var pic4InputImage by remember { mutableStateOf<Bitmap?>(null) }
    var pic5InputImage by remember { mutableStateOf<Bitmap?>(null) }
    var pic6InputImage by remember { mutableStateOf<Bitmap?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val sharedViewModel: MainViewModel = koinInject()
    val secondRowItems = listOf(
        EditingOption("White", R.drawable.white),
        EditingOption("Black", R.drawable.black),
        EditingOption("Transparent", R.drawable.transparent),
        EditingOption("Original", R.drawable.orignal)
    )
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(it)
            )
            inputImage = bitmap
            sharedViewModel.setOriginalBitmap(inputImage!!)

            coroutineScope.launch {
                val remover = RemoveBg(context)
                remover.clearBackground(inputImage!!).collect { output ->
                    outputImage = output
                    sharedViewModel.setBgRemovedBitmap(outputImage!!)
                    navController.navigate(Screens.BgDetail.route)
                }
            }
        }
    }

    val colorsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(it)
            )
            colorsInputImage = bitmap
            sharedViewModel.setOriginalBitmap(colorsInputImage!!)

            coroutineScope.launch {
                val remover = RemoveBg(context)
                remover.clearBackground(colorsInputImage!!).collect { output ->
                    colorsInputImage = output
                    sharedViewModel.setBgRemovedBitmap(colorsInputImage!!)
                    navController.navigate(Screens.Pic1Screen.route)
                }
            }
        }
    }
    val thirdRowItems = listOf(
        EditingOption("", R.drawable.pic1),
        EditingOption("", R.drawable.pic2),
        EditingOption("", R.drawable.pic3),
        EditingOption("", R.drawable.pic4),
        EditingOption("", R.drawable.pic5),
        EditingOption("", R.drawable.pic6)
    )

    val BlurLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(it)
            )
            BlurInputImage = bitmap
            sharedViewModel.setOriginalBitmap(BlurInputImage!!)

            coroutineScope.launch {
                val remover = RemoveBg(context)
                remover.clearBackground(BlurInputImage!!).collect { output ->
                    BlurInputImage = output
                    sharedViewModel.setBgRemovedBitmap(BlurInputImage!!)
                    navController.navigate(Screens.BlursScreen.route)
                }
            }
        }
    }


    val pic1Launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(it)
            )
            pic1InputImage = bitmap
            sharedViewModel.setOriginalBitmap(pic1InputImage!!)

            coroutineScope.launch {
                val remover = RemoveBg(context)
                remover.clearBackground(pic1InputImage!!).collect { output ->
                    pic1InputImage = output
                    sharedViewModel.setBgRemovedBitmap(pic1InputImage!!)
                    navController.navigate(Screens.Pic1Screen.route)
                }
            }
        }
    }

    val pic2Launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(it)
            )
            pic2InputImage = bitmap
            sharedViewModel.setOriginalBitmap(pic2InputImage!!)

            coroutineScope.launch {
                val remover = RemoveBg(context)
                remover.clearBackground(pic2InputImage!!).collect { output ->
                    pic2InputImage = output
                    sharedViewModel.setBgRemovedBitmap(pic2InputImage!!)
                    navController.navigate(Screens.Pic2Screen.route)
                }
            }
        }
    }

    val pic3Launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(it)
            )
            pic3InputImage = bitmap
            sharedViewModel.setOriginalBitmap(pic3InputImage!!)

            coroutineScope.launch {
                val remover = RemoveBg(context)
                remover.clearBackground(pic3InputImage!!).collect { output ->
                    pic3InputImage = output
                    sharedViewModel.setBgRemovedBitmap(pic3InputImage!!)
                    navController.navigate(Screens.Pic3Screen.route)
                }
            }
        }
    }

    val pic4Launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(it)
            )
            pic4InputImage = bitmap
            sharedViewModel.setOriginalBitmap(pic4InputImage!!)

            coroutineScope.launch {
                val remover = RemoveBg(context)
                remover.clearBackground(pic4InputImage!!).collect { output ->
                    pic4InputImage = output
                    sharedViewModel.setBgRemovedBitmap(pic4InputImage!!)
                    navController.navigate(Screens.Pic4Screen.route)
                }
            }
        }
    }

    val pic5Launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(it)
            )
            pic5InputImage = bitmap
            sharedViewModel.setOriginalBitmap(pic5InputImage!!)

            coroutineScope.launch {
                val remover = RemoveBg(context)
                remover.clearBackground(pic5InputImage!!).collect { output ->
                    pic5InputImage = output
                    sharedViewModel.setBgRemovedBitmap(pic5InputImage!!)
                    navController.navigate(Screens.Pic5Screen.route)
                }
            }
        }
    }

    val pic6Launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(it)
            )
            pic6InputImage = bitmap
            sharedViewModel.setOriginalBitmap(pic6InputImage!!)

            coroutineScope.launch {
                val remover = RemoveBg(context)
                remover.clearBackground(pic6InputImage!!).collect { output ->
                    pic6InputImage = output
                    sharedViewModel.setBgRemovedBitmap(pic6InputImage!!)
                    navController.navigate(Screens.Pic6Screen.route)
                }
            }
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                actions = {
                    Image(
                        painter = painterResource(id = R.drawable.questionmark),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Image(
                        painter = painterResource(id = R.drawable.getpro),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .wrapContentWidth()
                            .height(36.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = paddingValues.calculateTopPadding(), bottom = 90.dp)
                .background(Color.White),
            verticalArrangement = Arrangement.spacedBy(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(start = 10.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Create",
                        color = Color.Black,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    TextField(
                        value = searchText,
                        onValueChange = { newText ->
                            searchText = newText
                        },
                        placeholder = {
                            Text(text = "Search Photoroom Templates", color = Color.Gray)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp)
                            .height(54.dp),
                        shape = RoundedCornerShape(11.dp),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "",
                                tint = Color.Gray
                            )
                        },
                        textStyle = TextStyle(
                            fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color.Gray
                        ),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedContainerColor = Color.LightGray.copy(alpha = 0.40f),
                            unfocusedContainerColor = Color.LightGray.copy(alpha = 0.40f),
                        )
                    )


                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val firstRowItems = listOf(
                            EditingOption("Remove Bg", R.drawable.bgremover),
                            EditingOption("Retouch", R.drawable.retouch),
                            EditingOption("Ai Bg", R.drawable.aibg_remove),
                            EditingOption("Ai Shadows", R.drawable.aishadow),
                            EditingOption("Resize", R.drawable.crop)
                        )

                        items(firstRowItems.filter { option ->
                            option.name.contains(searchText.text, ignoreCase = true)
                        }) { option ->
                            Column {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .width(78.dp)
                                        .clickable {
                                            when (option.name) {
                                                "Remove Bg" -> galleryLauncher.launch("image/*")
                                                "Resize" -> navController.navigate(Screens.CropScreen.route)
                                            }
                                        }
                                        .height(70.dp)
                                        .background(Color(0XFF9eaaf7).copy(alpha = 0.20f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = option.icon),
                                        contentDescription = option.name,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.size(40.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(7.dp))
                                Text(
                                    text = option.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                            }
                        }
                    }


                    LazyRow(
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {


                        items(secondRowItems) { option ->
                            Column(
                                modifier = Modifier.padding(horizontal = 8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .border(
                                            BorderStroke(1.dp, Color.Gray),
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .width(78.dp)
                                        .height(70.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0XFF9eaaf7).copy(alpha = 0.20f))
                                        .clickable {
                                            when (option.name) {
                                                "White" -> colorsLauncher.launch("image/*")

                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = option.icon),
                                        contentDescription = "",
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }

                                Spacer(modifier = Modifier.height(7.dp))

                                Text(
                                    text = option.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                            }
                        }
                    }

                    Text(
                        text = "Photo Editing Classic  >",
                        color = Color.Black,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(start = 10.dp)
                    )

                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(horizontal = 10.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val thirdRowItems = listOf(
                            EditingOption("Blur", R.drawable.blur),
                            EditingOption("Color Splash", R.drawable.colorsplash),
                            EditingOption("Motion", R.drawable.motion),
                            EditingOption("Low Key", R.drawable.lowkey),
                            EditingOption("Heigh Key", R.drawable.heightkey),
                            EditingOption("Sepia", R.drawable.sepia)
                        )

                        items(thirdRowItems.filter { option ->
                            option.name.contains(searchText.text, ignoreCase = true)
                        }) { option ->
                            Column(
                                modifier = Modifier.padding(horizontal = 8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .border(
                                            BorderStroke(1.dp, Color.Gray),
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .width(80.dp)
                                        .height(80.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0XFF9eaaf7).copy(alpha = 0.20f))
                                        .clickable {
                                            when (option.name) {
                                                "Blur" -> BlurLauncher.launch("image/*")

                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = option.icon),
                                        contentDescription = "",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }

                                Spacer(modifier = Modifier.height(7.dp))

                                Text(
                                    text = option.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                            }
                        }
                    }


                    Text(
                        text = "Profile Pics  >",
                        color = Color.Black,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(start = 10.dp)
                    )

                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(horizontal = 10.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(thirdRowItems.filter { option ->
                            option.name.contains(searchText.text, ignoreCase = true)
                        }) { option ->
                            Column(
                                modifier = Modifier.padding(horizontal = 8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .border(
                                            BorderStroke(1.dp, Color.Gray),
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .width(80.dp)
                                        .height(80.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0XFF9eaaf7).copy(alpha = 0.20f))
                                        .clickable {
                                            when (option.icon) {
                                                R.drawable.pic1 -> pic1Launcher.launch("image/*")
                                                R.drawable.pic2 -> pic2Launcher.launch("image/*")
                                                R.drawable.pic3 -> pic3Launcher.launch("image/*")
                                                R.drawable.pic4 -> pic4Launcher.launch("image/*")
                                                R.drawable.pic5 -> pic5Launcher.launch("image/*")
                                                R.drawable.pic6 -> pic6Launcher.launch("image/*")

                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = option.icon),
                                        contentDescription = "",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

data class EditingOption(val name: String, val icon: Int)