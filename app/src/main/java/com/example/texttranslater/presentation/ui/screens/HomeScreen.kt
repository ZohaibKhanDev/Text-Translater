package com.example.texttranslater.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.texttranslater.domain.usecase.ResultState
import com.example.texttranslater.presentation.viewmodel.MainViewModel
import org.koin.compose.koinInject

@Composable
fun HomeScreen() {
    val viewModel: MainViewModel = koinInject()
    val translationState by viewModel.translation.collectAsState()
    var inputText by remember { mutableStateOf("") }
    var translateData by remember { mutableStateOf<String?>(null) }

    when (translationState) {
        is ResultState.Error -> {
            val error = (translationState as ResultState.Error).error
            Text(text = error.toString())
        }

        ResultState.Loading -> {
            CircularProgressIndicator()
        }

        is ResultState.Success -> {
            val success = (translationState as ResultState.Success).success
            translateData = success
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(value = inputText, onValueChange = { inputText = it })
        Spacer(modifier = Modifier.height(50.dp))

        Button(onClick = { viewModel.translateText(inputText, "en", "zh") }) {
            Text(text = "Enter")
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        translateData?.let {
            Text(text = it)
        }
    }

}


