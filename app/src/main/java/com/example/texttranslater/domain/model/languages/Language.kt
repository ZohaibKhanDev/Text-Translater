package com.example.texttranslater.domain.model.languages


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Language(
    @SerialName("country_code")
    val countryCode: String,
    @SerialName("direction")
    val direction: String,
    @SerialName("identifiable")
    val identifiable: Boolean,
    @SerialName("language")
    val language: String,
    @SerialName("language_name")
    val languageName: String,
    @SerialName("native_language_name")
    val nativeLanguageName: String,
    @SerialName("supported_as_source")
    val supportedAsSource: Boolean,
    @SerialName("supported_as_target")
    val supportedAsTarget: Boolean,
    @SerialName("words_separated")
    val wordsSeparated: Boolean
)