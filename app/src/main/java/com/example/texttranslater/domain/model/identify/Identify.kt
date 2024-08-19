package com.example.texttranslater.domain.model.identify


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Identify(
    @SerialName("languages")
    val languages: List<Language>
)