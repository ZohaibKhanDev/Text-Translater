package com.example.texttranslater.presentation.ui.screens

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CopyAll
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.CopyAll
import androidx.compose.material.icons.outlined.IosShare
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.texttranslater.R
import com.example.texttranslater.domain.usecase.ResultState
import com.example.texttranslater.presentation.viewmodel.MainViewModel
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import org.koin.compose.koinInject

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val viewModel: MainViewModel = koinInject()
    val translationState by viewModel.translation.collectAsState()
    var inputText by remember { mutableStateOf("") }
    var translateData by remember { mutableStateOf<String?>(null) }

    var sourceLanguage by remember { mutableStateOf("English") }
    var targetLanguage by remember { mutableStateOf("Urdu") }
    var isLoading by remember { mutableStateOf(false) }


    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current


    fun copyTextToClipboard(text: String) {
        val clipData = ClipData.newPlainText("label", text)
        clipboardManager.setText(AnnotatedString(text))
        Toast.makeText(context, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
    }


    fun shareText(text: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }

    val languageMap = mapOf(
        "Afrikaans" to "af",
        "Amharic" to "am",
        "Arabic" to "ar",
        "Bengali" to "bn",
        "Bosnian" to "bs",
        "Bulgarian" to "bg",
        "Catalan" to "ca",
        "Chinese (Simplified)" to "zh-CN",
        "Chinese (Traditional)" to "zh-TW",
        "Croatian" to "hr",
        "Czech" to "cs",
        "Danish" to "da",
        "Dutch" to "nl",
        "English" to "en",
        "Estonian" to "et",
        "Finnish" to "fi",
        "French" to "fr",
        "Galician" to "gl",
        "German" to "de",
        "Greek" to "el",
        "Gujarati" to "gu",
        "Hebrew" to "he",
        "Hindi" to "hi",
        "Hungarian" to "hu",
        "Icelandic" to "is",
        "Indonesian" to "id",
        "Irish" to "ga",
        "Italian" to "it",
        "Japanese" to "ja",
        "Kannada" to "kn",
        "Kazakh" to "kk",
        "Korean" to "ko",
        "Latvian" to "lv",
        "Lithuanian" to "lt",
        "Macedonian" to "mk",
        "Malay" to "ms",
        "Malayalam" to "ml",
        "Marathi" to "mr",
        "Norwegian" to "no",
        "Persian" to "fa",
        "Polish" to "pl",
        "Portuguese" to "pt",
        "Romanian" to "ro",
        "Russian" to "ru",
        "Serbian" to "sr",
        "Slovak" to "sk",
        "Slovenian" to "sl",
        "Spanish" to "es",
        "Swahili" to "sw",
        "Swedish" to "sv",
        "Tamil" to "ta",
        "Telugu" to "te",
        "Thai" to "th",
        "Turkish" to "tr",
        "Ukrainian" to "uk",
        "Urdu" to "ur",
        "Vietnamese" to "vi",
        "Zulu" to "zu"
    )


    val availableLanguages = languageMap.keys.map { it to R.drawable.translate_icon }

    when (translationState) {
        is ResultState.Error -> {
            isLoading = false
            val error = (translationState as ResultState.Error).error
            translateData = error.toString()
        }

        ResultState.Loading -> {
            isLoading = true
        }

        is ResultState.Success -> {
            isLoading = false
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
                .padding(top = 100.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Card(
                modifier = Modifier
                    .padding(start = 13.dp, end = 13.dp, top = 24.dp)
                    .fillMaxWidth()
                    .height(47.dp),
                elevation = CardDefaults.cardElevation(3.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                ), shape = RoundedCornerShape(50.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0XFFf7f2f9)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    LanguageSelector(
                        selectedLanguage = sourceLanguage, onLanguageSelected = { selected ->
                            sourceLanguage = selected
                        }, availableLanguages = availableLanguages
                    )

                    Icon(Icons.Default.ArrowForward, contentDescription = "Switch Language")

                    LanguageSelector(
                        selectedLanguage = targetLanguage, onLanguageSelected = { selected ->
                            targetLanguage = selected
                        }, availableLanguages = availableLanguages
                    )
                }
            }


            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
                    .height(216.dp),
                elevation = CardDefaults.cardElevation(3.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0XFFf7f2f9))
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(11.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = sourceLanguage,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0XFF003366)
                        )
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "",
                            modifier = Modifier.clickable {
                                inputText = ""
                                translateData=null
                            })
                    }

                    TextField(
                        value = inputText,
                        onValueChange = { newText ->
                            inputText = newText
                            val sourceCode = languageMap[sourceLanguage] ?: "en"
                            val targetCode = languageMap[targetLanguage] ?: "ur"
                            if (inputText.isNotBlank()) {
                                viewModel.translateText(inputText, sourceCode, targetCode)
                            } else {
                                translateData = null
                            }
                        },
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        shape = RoundedCornerShape(0.dp),
                        placeholder = {
                            Text(
                                text = "Enter Text",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(25.dp))


            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
                    .height(216.dp),
                elevation = CardDefaults.cardElevation(3.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0XFFf7f2f9))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(11.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = targetLanguage,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0XFF003366)
                        )
                    }


                    if (isLoading) {
                        ShimmerPlaceholder()
                    } else {
                        Text(
                            text = translateData ?: "Translation will appear here",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(bottom = 55.dp, start = 11.dp)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 250.dp),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CopyAll,
                            contentDescription = "Copy Text",
                            tint = Color(0XFF003366),
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    translateData?.let { copyTextToClipboard(it) }
                                }
                        )

                        Icon(
                            imageVector = Icons.Outlined.IosShare,
                            contentDescription = "Share Text",
                            tint = Color(0XFF003366),
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    translateData?.let { shareText(it) }
                                }
                        )
                        Icon(
                            imageVector = Icons.Outlined.Star,
                            contentDescription = "",
                            tint = Color(0XFF003366),
                            modifier = Modifier
                                .size(24.dp)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun ShimmerPlaceholder() {
    val shimmerInstance = rememberShimmer(ShimmerBounds.Custom)
    Box(
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()
            .shimmer(shimmerInstance)
            .background(Color.Gray, shape = RoundedCornerShape(4.dp))
    )
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

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            availableLanguages.forEach { language ->
                DropdownMenuItem(
                    text = {
                        Text(text = language.first)
                    },
                    onClick = {
                        onLanguageSelected(language.first)
                        expanded = false
                    }
                )
            }
        }
    }
}
