package com.example.texttranslater.domain.model.identify


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Language(
    @SerialName("confidence")
    val confidence: Double,
    @SerialName("language")
    val language: String
)