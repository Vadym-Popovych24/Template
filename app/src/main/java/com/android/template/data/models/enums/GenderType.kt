package com.android.template.data.models.enums

import androidx.annotation.StringRes
import com.android.template.R

enum class GenderType(val code: Int, @StringRes val localizedTitle: Int) {
    UNSPECIFIED(-1, R.string.unspecified),
    MALE(0, R.string.male),
    FEMALE(1, R.string.female);

    companion object {

        fun getGenderByCode(code: Int) = values().firstOrNull { it.code == code } ?: UNSPECIFIED

    }
}