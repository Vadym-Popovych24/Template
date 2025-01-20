package com.android.template.utils.custom

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.template.R
import com.android.template.data.models.api.model.FullImage

class FullImageOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var tv: TextView? = null

    init {
        inflate(context, R.layout.view_full_image_overlay, this)
        tv = findViewById(R.id.posterOverlayDescriptionText)
        setBackgroundColor(Color.TRANSPARENT)
    }

    fun update(fullImage: FullImage) {
        tv?.text = fullImage.description
    }
}