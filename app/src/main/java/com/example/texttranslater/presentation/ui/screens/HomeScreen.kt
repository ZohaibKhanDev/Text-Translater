package com.example.texttranslater.presentation.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.texttranslater.R
import com.example.texttranslater.domain.usecase.ResultState
import com.example.texttranslater.presentation.viewmodel.MainViewModel
import org.koin.compose.koinInject


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val viewModel: MainViewModel = koinInject()
    val translationState by viewModel.translation.collectAsState()
    var inputText by remember { mutableStateOf("") }
    var translateData by remember { mutableStateOf<String?>(null) }

    var sourceLanguage by remember { mutableStateOf("English") }
    var targetLanguage by remember { mutableStateOf("Spanish") }


    val languageMap = mapOf(
        "English" to "en",
        "Spanish" to "es",
        "Chinese" to "zh"
    )

    val availableLanguages = listOf(
        "English" to R.drawable.translate_icon,
        "Spanish" to R.drawable.translate_icon,
        "Chinese" to R.drawable.translate_icon
    )

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

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = "Language Translator",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }, navigationIcon = {
            Icon(imageVector = Icons.Default.Menu, contentDescription = "", tint = Color.White)
        }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0XFF003366)))
    }) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding()),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                LanguageSelector(
                    selectedLanguage = sourceLanguage,
                    onLanguageSelected = { selected ->
                        sourceLanguage = selected
                    },
                    availableLanguages = availableLanguages
                )

                Icon(Icons.Default.ArrowForward, contentDescription = "Switch Language")

                LanguageSelector(
                    selectedLanguage = targetLanguage,
                    onLanguageSelected = { selected ->
                        targetLanguage = selected
                    },
                    availableLanguages = availableLanguages
                )
            }

            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                label = { Text("Enter text") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )

            Button(
                onClick = {

                    val sourceCode = languageMap[sourceLanguage] ?: "en"
                    val targetCode = languageMap[targetLanguage] ?: "es"
                    viewModel.translateText(inputText, sourceCode, targetCode)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(text = "Translate")
            }
            Spacer(modifier = Modifier.height(16.dp))
            translateData?.let {
                Text(
                    text = it,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun LanguageSelector(
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit,
    availableLanguages: List<Pair<String, Int>>,
    iconSize: Dp = 24.dp
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.clickable { expanded = true }
        ) {
            Image(
                painter = painterResource(id = availableLanguages.first { it.first == selectedLanguage }.second),
                contentDescription = "$selectedLanguage Flag",
                modifier = Modifier.size(iconSize)
            )
            Text(text = selectedLanguage, fontSize = 18.sp, fontWeight = FontWeight.Medium)
            Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown Icon")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            availableLanguages.forEach { language ->
                DropdownMenuItem(text = {
                    Image(
                        painter = painterResource(id = language.second),
                        contentDescription = "${language.first} Flag",
                        modifier = Modifier.size(iconSize)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = language.first)

                }, onClick = {
                    onLanguageSelected(language.first)
                    expanded = false
                })
            }
        }
    }
}
