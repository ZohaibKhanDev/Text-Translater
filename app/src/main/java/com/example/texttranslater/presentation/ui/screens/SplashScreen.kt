package com.example.texttranslater.presentation.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.material3.Text
import com.example.texttranslater.R

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {

        CustomBackground(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter)
                .padding(20.dp)
        )


        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.translate_icon),
                contentDescription = "Logo",
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "TRANSLATE ON THE GO",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = Color.Black
            )
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Dot(color = Color(0xFF6B8EAE))
            Spacer(modifier = Modifier.width(8.dp))
            Dot(color = Color(0xFFC0E8E2))
        }
    }
}

@Composable
fun CustomBackground(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        val path = Path().apply {
            moveTo(0f, height * 0.4f)
            cubicTo(
                width * 0.3f, height * 0.25f,
                width * 0.7f, height * 0.6f,
                width, height * 0.4f
            )
            lineTo(width, 0f)
            lineTo(0f, 0f)
            close()
        }

        drawPath(
            path = path,
            color = Color(0xFFB8A99A)
        )

        val path2 = Path().apply {
            moveTo(0f, height * 0.4f)
            cubicTo(
                width * 0.3f, height * 0.55f,
                width * 0.7f, height * 0.2f,
                width, height * 0.4f
            )
            lineTo(width, 0f)
            lineTo(0f, 0f)
            close()
        }

        drawPath(
            path = path2,
            color = Color(0xFFFFB08F)
        )
    }
}

@Composable
fun Dot(color: Color) {
    Box(
        modifier = Modifier
            .size(12.dp)
            .background(color = color, shape = CircleShape)
    )
}
