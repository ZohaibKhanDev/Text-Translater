package com.example.texttranslater.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.texttranslater.domain.repository.Repository
import com.example.texttranslater.domain.usecase.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {
    private val _translation = MutableStateFlow<ResultState<String>>(ResultState.Loading)
    val translation: StateFlow<ResultState<String>> = _translation.asStateFlow()


    private val _languageIdentification = MutableStateFlow<ResultState<String>>(ResultState.Loading)
    val languageIdentification: StateFlow<ResultState<String>> = _languageIdentification.asStateFlow()

    private val _availableLanguages = MutableStateFlow<ResultState<String>>(ResultState.Loading)
    val availableLanguages: StateFlow<ResultState<String>> = _availableLanguages.asStateFlow()


    fun translateText(input: String, sourceLang: String, targetLang: String) {
        viewModelScope.launch {
            _translation.value=ResultState.Loading
            try {
                val translatedText = repository.translateText(input, sourceLang, targetLang)
                _translation.emit(ResultState.Success(translatedText))
            } catch (e: Exception) {
                _translation.emit(ResultState.Error(e))
            }
        }
    }


    fun identifyLanguage(text: String) {
        viewModelScope.launch {
            _languageIdentification.value = ResultState.Loading
            try {
                val response = repository.identifyLanguage(text)
                _languageIdentification.value = ResultState.Success(response)
            } catch (e: Exception) {
                _languageIdentification.value = ResultState.Error(e)
            }
        }
    }

    fun fetchAvailableLanguages() {
        viewModelScope.launch {
            _availableLanguages.value = ResultState.Loading
            try {
                val response = repository.getAvailableLanguages()
                _availableLanguages.value = ResultState.Success(response)
            } catch (e: Exception) {
                _availableLanguages.value = ResultState.Error(e)
            }
        }
    }
}
