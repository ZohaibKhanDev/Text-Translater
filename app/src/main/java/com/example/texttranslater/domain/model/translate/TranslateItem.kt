package com.example.texttranslater.domain.model.translate


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TranslateItem(
    @SerialName("translations")
    val translations: List<Translation>
)