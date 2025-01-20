package com.android.template.utils.custom

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.databinding.ViewDataBinding
import com.stfalcon.imageviewer.StfalconImageViewer
import com.android.template.R
import com.android.template.data.models.api.model.FullImage
import com.android.template.ui.base.BaseFragment
import com.android.template.ui.base.BaseViewModel
import com.android.template.utils.BindingUtils
import com.android.template.utils.getDrawableCompat

fun <T : ViewDataBinding, V : BaseViewModel> BaseFragment<T, V>.openViewer(
    images: List<FullImage>,
    view: View,
    startPosition: Int = 0
) {
    setStatusBarColor(R.color.black)
    val overlayView: FullImageOverlayView =
        setupOverlayView(images = images.toMutableList(), startPosition, this.requireContext())
    StfalconImageViewer.Builder(requireContext(), images, ::loadImage)
        .withStartPosition(startPosition)
        .withOverlayView(overlayView)
        .withTransitionFrom(if (view is ImageView) view else null)
        .withImageChangeListener { position ->
            overlayView.update(images[position])
        }
        .withDismissListener {
            setStatusBarColor(R.color.white)
        }
        .show()
}

private fun loadImage(imageView: ImageView, poster: FullImage?) {
    imageView.apply {
        background = context.getDrawableCompat(R.color.black)
        BindingUtils.setLoadImageUrl(this, poster?.url)
    }
}

private fun setupOverlayView(images: MutableList<FullImage>, startPosition: Int, context: Context) =
    FullImageOverlayView(context).apply {
        update(images[startPosition])
    }
