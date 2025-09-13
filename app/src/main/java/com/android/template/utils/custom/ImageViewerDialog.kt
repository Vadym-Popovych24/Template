package com.android.template.utils.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import coil.load
import com.android.template.R
import com.github.chrisbanes.photoview.PhotoView

class ImageViewerDialog : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.dialog_image_viewer, container, false)
        val photoView = root.findViewById<PhotoView>(R.id.photo_view)
        val descriptionText = root.findViewById<TextView>(R.id.tv_description)

        val description = arguments?.getString(IMAGE_DESCRIPTION)

        val imageUrl = arguments?.getString(IMAGE_URL)
        photoView.load(imageUrl) {
            crossfade(true)
            placeholder(android.R.color.darker_gray)
            error(android.R.color.holo_red_light)
        }

        // Set text
        descriptionText.text = description

        // Close on tap
        photoView.setOnPhotoTapListener { _, _, _ -> dismiss() }

        return root
    }

    companion object {
        private const val IMAGE_URL = "IMAGE_URL"
        private const val IMAGE_DESCRIPTION = "IMAGE_DESCRIPTION"

        fun newInstance(url: String, description: String): ImageViewerDialog {
            val fragment = ImageViewerDialog()
            fragment.arguments = Bundle().apply {
                putString(IMAGE_URL, url)
                putString(IMAGE_DESCRIPTION, description)
            }
            return fragment
        }
    }
}
