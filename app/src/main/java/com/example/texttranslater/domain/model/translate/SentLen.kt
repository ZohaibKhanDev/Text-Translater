package com.example.texttranslater.domain.model.translate


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SentLen(
    @SerialName("srcSentLen")
    val srcSentLen: List<Int>,
    @SerialName("transSentLen")
    val transSentLen: List<Int>
)