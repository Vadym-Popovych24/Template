package com.android.template.utils.custom

import android.content.Context
import android.text.InputType.TYPE_NULL
import android.util.AttributeSet

class InstantAutoComplete @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatAutoCompleteTextView(context, attrs, defStyleAttr) {

    init {
        inputType = TYPE_NULL
    }

    override fun performFiltering(text: CharSequence?, keyCode: Int) {
        super.performFiltering("", keyCode)
    }
}