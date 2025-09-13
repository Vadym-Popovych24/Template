package com.android.template.utils.helpers

import androidx.annotation.StringRes
import com.android.template.utils.getStringFromResource

class ResourceProviderImpl() : ResourceProvider {
    override fun getString(@StringRes resId: Int): String {
        return resId.getStringFromResource
    }
}