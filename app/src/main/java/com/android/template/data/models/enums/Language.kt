package com.android.template.data.models.enums

import androidx.annotation.StringRes
import com.android.template.R

enum class Language(val code: Int, @StringRes val localizedTitle: Int, val tag: String) {
    EN(0, R.string.language_key_en, "en"),
    UK(1, R.string.language_key_uk, "uk");

    companion object {

        fun getLanguageByCode(code: Int) = entries.firstOrNull { it.code == code } ?: EN

    }
}