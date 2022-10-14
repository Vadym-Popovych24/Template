package com.android.template.data.models.api.model.translations

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
open class Translation: Parcelable {
    @SerializedName("translationText")
    val text: String = ""
    @SerializedName("languageTag")
    val languageTag: String = ""
}

fun List<Translation>.getTranslation(languageTag: String): String? = firstOrNull { it.languageTag == languageTag }?.text