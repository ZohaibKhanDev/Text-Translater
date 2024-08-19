package com.example.texttranslater.data.repository

interface ApiClient {
    suspend fun translateText( text: String, sourceLanguage: String, targetLanguage: String): String
    suspend fun identifyLanguage(text: String): String
    suspend fun getAvailableLanguages(): String
}
