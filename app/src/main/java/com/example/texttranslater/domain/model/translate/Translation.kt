package com.example.texttranslater.domain.model.translate


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Translation(
    @SerialName("translation")
    val translation: String
)