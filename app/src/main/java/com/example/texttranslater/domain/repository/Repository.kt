package com.example.texttranslater.domain.repository

import com.example.texttranslater.data.remote.TranslaterApiClient
import com.example.texttranslater.data.repository.ApiClient

class Repository:ApiClient {
    override suspend fun translateText(
        text: String,
        sourceLanguage: String,
        targetLanguage: String
    ): String {
       return TranslaterApiClient.translateText(text, sourceLanguage, targetLanguage)
    }

    override suspend fun identifyLanguage(text: String): String {
        return TranslaterApiClient.identifyLanguage(text)
    }

    override suspend fun getAvailableLanguages(): String {
        return TranslaterApiClient.getAvailableLanguages()
    }
}