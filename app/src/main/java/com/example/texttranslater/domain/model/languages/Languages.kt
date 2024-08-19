package com.example.texttranslater.domain.model.languages


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Languages(
    @SerialName("languages")
    val languages: List<Language>
)