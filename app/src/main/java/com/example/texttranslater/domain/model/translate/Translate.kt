package com.example.texttranslater.domain.model.translate


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Translate(
    @SerialName("character_count")
    val characterCount: Int,
    @SerialName("translations")
    val translations: List<Translation>,
    @SerialName("word_count")
    val wordCount: Int
)