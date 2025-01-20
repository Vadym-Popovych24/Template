package com.android.template.data.models.enums

import com.android.template.R
import com.android.template.utils.getStringFromResource

enum class ChangeImageType(val code: Int, val title: String) {
    UNSPECIFIED(-1, R.string.unspecified.getStringFromResource),
    AVATAR(0, "avatar"),
    COVER(1, "cover");

    companion object {

        fun getChangeImageType(code: Int) = entries.firstOrNull { it.code == code } ?: UNSPECIFIED

    }
}