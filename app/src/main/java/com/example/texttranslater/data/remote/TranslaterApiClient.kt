package com.example.texttranslater.data.remote

import com.example.texttranslater.domain.model.translate.Translate
import com.example.texttranslater.utils.Constant.API_KEY
import com.example.texttranslater.utils.Constant.TIMEOUT
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.utils.EmptyContent.headers
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.Parameters
import io.ktor.http.formUrlEncode
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import okio.ByteString.Companion.encode
import java.util.logging.Level
import java.util.logging.Logger

object TranslaterApiClient {
    @OptIn(ExperimentalSerializationApi::class)
    val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(
                Json {
                    isLenient = true
                    ignoreUnknownKeys = true
                    explicitNulls = false
                }
            )
        }

        install(Logging) {
            level = LogLevel.ALL
            logger = object : io.ktor.client.plugins.logging.Logger {
                override fun log(message: String) {
                    println(message)
                }
            }
        }

        install(HttpTimeout) {
            socketTimeoutMillis = TIMEOUT
            connectTimeoutMillis = TIMEOUT
            requestTimeoutMillis = TIMEOUT
        }
    }



    suspend fun translateText(
        text: String,
        sourceLanguage: String,
        targetLanguage: String
    ): String {
        val client = HttpClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        val response: Translate = client.post("https://api.apilayer.com/language_translation/translate") {
            header("apikey", API_KEY)
            url {
                parameters.append("source", sourceLanguage)
                parameters.append("target", targetLanguage)
            }
            setBody(text)
        }.body()


        return response.translations.firstOrNull()?.translation ?: "Translation not found"
    }



    suspend fun identifyLanguage(text: String): String {
        val response: HttpResponse = client.post("https://api.apilayer.com/language_translation/identify") {
            headers {
                append("apikey", API_KEY)
            }
            setBody("text=${text.encode()}")
        }

        if (response.status.isSuccess()) {
            return response.bodyAsText()
        } else {
            throw Exception("Failed to identify language: ${response.status}")
        }
    }

    suspend fun getAvailableLanguages(): String {
        val response: HttpResponse = client.get("https://api.apilayer.com/language_translation/languages") {
            headers {
                append("apikey", API_KEY)
            }
        }

        if (response.status.isSuccess()) {
            return response.bodyAsText()
        } else {
            throw Exception("Failed to fetch languages: ${response.status}")
        }
    }
}
