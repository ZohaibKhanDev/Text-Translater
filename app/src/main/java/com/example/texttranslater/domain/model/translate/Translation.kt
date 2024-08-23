package com.example.texttranslater.domain.model.translate


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Translation(
    @SerialName("alignment")
    val alignment: Alignment?=null,
    @SerialName("sentLen")
    val sentLen: SentLen?=null,
    @SerialName("text")
    val text: String,
    @SerialName("to")
    val to: String
)