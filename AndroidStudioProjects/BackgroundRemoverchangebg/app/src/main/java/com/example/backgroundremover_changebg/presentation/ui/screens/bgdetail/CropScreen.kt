package com.example.backgroundremover_changebg.presentation.ui.screens.bgdetail

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraEnhance
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.example.backgroundremover_changebg.R
import com.example.backgroundremover_changebg.presentation.viewmodel.MainViewModel
import com.mr0xf00.easycrop.rememberImageCropper
import com.mr0xf00.easycrop.ui.ImageCropperDialog
import kotlinx.coroutines.launch
import java.io.OutputStream
import java.util.Date
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CropScreen(navController: NavController) {
    val imageCropper = rememberImageCropper()
    var bitmap: Bitmap? by remember { mutableStateOf(null) }
    val context = LocalContext.current
    val activity = context as? Activity
    val coroutineScope = rememberCoroutineScope()


    val imageCropLauncher =
        rememberLauncherForActivityResult(contract = CropImageContract()) { result ->
            if (result.isSuccessful) {
                result.uriContent?.let {
                    bitmap = if (Build.VERSION.SDK_INT < 28) {
                        MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                    } else {
                        val source = ImageDecoder.createSource(context.contentResolver, it)
                        ImageDecoder.decodeBitmap(source)
                    }
                }
            } else {
                Toast.makeText(context, "Image Cropping Error: ${result.error}", Toast.LENGTH_LONG).show()
            }
        }

    fun saveImage(bitmap: Bitmap) {
        coroutineScope.launch {
            val contentResolver = context.contentResolver
            val resolver = context.contentResolver
            val name = "CroppedImage_${SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())}.jpg"
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, name)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/${context.getString(R.string.app_name)}/")
            }
            val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            uri?.let {
                contentResolver.openOutputStream(it).use { outputStream: OutputStream? ->
                    if (outputStream != null) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    }
                    Toast.makeText(context, "Image saved successfully!", Toast.LENGTH_LONG).show()
                }
            } ?: run {
                Toast.makeText(context, "Failed to save image", Toast.LENGTH_LONG).show()
            }
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crop Image") },
                actions = {
                    IconButton(
                        onClick = {
                            val cropOptions = CropImageContractOptions(
                                null,
                                CropImageOptions(imageSourceIncludeCamera = false)
                            )
                            imageCropLauncher.launch(cropOptions)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Image,
                            contentDescription = "Select Image"
                        )
                    }

                    IconButton(
                        onClick = {
                            bitmap?.let { saveImage(it) }
                        }
                    ) {
                        Icon(
                            Icons.Filled.SaveAlt,
                            contentDescription = "Save Image"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}



