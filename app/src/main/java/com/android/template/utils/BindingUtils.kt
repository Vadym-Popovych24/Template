package com.android.template.utils

import android.content.res.ColorStateList
import android.graphics.Paint
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso
import com.android.template.BR
import com.android.template.R
import com.android.template.databinding.LeftHandMenuHeaderBinding
import com.android.template.ui.navigation.viewmodel.NavigationHeaderViewModel
import com.github.leandroborgesferreira.loadingbutton.customViews.CircularProgressButton
import java.io.File
import java.text.DateFormatSymbols
import java.util.*

object BindingUtils {
    /**
     * Provide custom logic for corresponding attribute
     */
    @JvmStatic
    @BindingAdapter("addHeader")
    fun loadHeader(view: NavigationView, viewModel: NavigationHeaderViewModel) {
        val binding = LeftHandMenuHeaderBinding.inflate(LayoutInflater.from(view.context))
        binding.setVariable(BR.viewModel, viewModel)
        binding.executePendingBindings()
        view.addHeaderView(binding.root)
    }


    @JvmStatic
    @BindingAdapter("loadImageUrl")
    fun setLoadImageUrl(imageView: ImageView, path: String?) {
        if (!path.isNullOrEmpty()) {
            val picasso = Picasso.get()
            val requestCreator =
                if (path.isUrl()) {
                    picasso.load(path)
                } else {
                    picasso.load(File(path))
                }
            requestCreator.placeholder(R.drawable.avatar_placeholder).into(imageView)
        } else {
            Picasso.get().load(R.drawable.avatar_placeholder).into(imageView)
        }
    }

    @JvmStatic
    @BindingAdapter("loadNotificationImageUrl")
    fun setLoadNotificationImageUrl(imageView: ImageView, path: String?) {
        if (!path.isNullOrEmpty()) {
            val picasso = Picasso.get()
            val requestCreator =
                if (path.isUrl()) {
                    picasso.load(path)
                } else {
                    picasso.load(File(path))
                }
            requestCreator.placeholder(R.drawable.logo).into(imageView)
        } else {
            Picasso.get().load(R.drawable.logo).into(imageView)
        }
    }

    @JvmStatic
    @BindingAdapter("error")
    fun setError(textInputLayout: TextInputLayout, errorField: String?) {
        textInputLayout.error = errorField
        textInputLayout.isErrorEnabled = errorField != null
    }

    @JvmStatic
    @BindingAdapter("loadImageUrlWithoutPlaceholder")
    fun setLoadImageUrlWithoutPlaceholder(imageView: ImageView, path: String?) {
        if (!path.isNullOrEmpty()) {
            val picasso = Picasso.get()
            val requestCreator =
                if (path.isUrl()) {
                    picasso.load(path)
                } else {
                    picasso.load(File(path))
                }
            requestCreator.into(imageView)
        } else {
            imageView.setImageResource(android.R.color.white)
        }
    }

    @JvmStatic
    @BindingAdapter("circleImageWithBorder")
    fun setCircleImageWithBorder(imageView: ImageView, path: String?) {
        if (!path.isNullOrEmpty()) {
            imageView.loadCircularImage(path, 4.0f)
        } else {
            Glide.with(imageView.context).load(R.drawable.avatar_placeholder).apply(
                RequestOptions().circleCrop()
            ).into(imageView)
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["path", "defaultPath", "isIncognito"], requireAll = false)
    fun setCircleImageWithBorderAndDefault(imageView: ImageView, pathImage: String?, @IdRes defaultPath: Int? = null, isIncognito: Boolean = false) {
        if (isIncognito){
            imageView.setColorFilter(ContextCompat.getColor(imageView.context, R.color.gray))
            imageView.background = ContextCompat.getDrawable(imageView.context, R.drawable.circler_border_with_background)
            imageView.setPadding(40.toDp, 40.toDp, 40.toDp, 40.toDp)
        }else{
            imageView.setColorFilter(ContextCompat.getColor(imageView.context, android.R.color.transparent))
            imageView.setPadding(0.toDp, 0.toDp, 0.toDp, 0.toDp)
        }

        Glide.with(imageView.context)
            .asBitmap()
            .load(pathImage)
            .placeholder(defaultPath ?: R.drawable.avatar_placeholder)
            .apply(RequestOptions().circleCrop())
            .into(imageView)

    }

    @JvmStatic
    @BindingAdapter("circleImage")
    fun setCircleImage(imageView: ImageView, path: String?) {
        if (!path.isNullOrEmpty()) {
            Glide.with(imageView.context).load(path).apply(
                RequestOptions().circleCrop()
            ).into(imageView)
        } else Glide.with(imageView.context).load(R.drawable.default_avatar).apply(
            RequestOptions().circleCrop()
        ).into(imageView)
    }

   /* @JvmStatic
    @BindingAdapter("conversationAvatar")
    fun setConversationImage(
        imageView: ImageView,
        avatar: ConversationItemViewModel.ConversationsAvatar?
    ) {
        val requestManager = Glide.with(imageView.context)
        val requestBuilder = when {
            avatar == null -> {
                requestManager.load(R.drawable.avatar_placeholder)
            }
            avatar.isGroup -> {
                requestManager.load(R.drawable.ic_group_chat)
            }
            avatar.avatarUrl.isNullOrEmpty() && avatar.isGroup.not() -> {
                requestManager.load(R.drawable.avatar_placeholder)
            }
            else -> {
                requestManager.load(avatar.avatarUrl)
            }
        }

        requestBuilder.apply(
            RequestOptions().circleCrop()
        ).into(imageView)
    }*/

    @JvmStatic
    @BindingAdapter("layout_height")
    fun setLayoutHeight(view: View, height: Float?) {
        height?.let { view.layoutParams.height = it.toInt() }
    }

    @JvmStatic
    @BindingAdapter("progressColor")
    fun setProgressColor(progressBar: ProgressBar, color: Int?) {
        color?.let { progressBar.progressTintList = ColorStateList.valueOf(it) }
    }

    @JvmStatic
    @BindingAdapter("android:src")
    fun loadImage(view: ImageView, icon: Int) {
        view.setImageResource(icon)
    }

    @JvmStatic
    @BindingAdapter("underline")
    fun underline(view: TextView, underlineText: String) {
        view.text = underlineText
        view.paintFlags = view.paintFlags or Paint.UNDERLINE_TEXT_FLAG
    }

    @JvmStatic
    @BindingAdapter(value = ["setDate", "isStillWork"], requireAll = false)
    fun setDateToTextInput(view: TextInputEditText, date: Date?, isStillWork: Boolean = false) {
        if(date == null && !isStillWork) {
            view.text = null
            return
        }

        val text = if(isStillWork){
            view.context.resources.getString(R.string.present)
        }else{
            val monthOfYear = DateFormat.format("MM", date).toString().toInt()
            val year = DateFormat.format("yyyy", date)
            val monthName = DateFormatSymbols(Locale.getDefault()).months[monthOfYear - 1]
            "$monthName $year"
        }
        view.setText(text)
    }

    @JvmStatic
    @BindingAdapter("loading")
    fun loadingCircularProgressButton(button: CircularProgressButton, loading: Boolean) {
        if (loading)
            button.startAnimation()
        else
            button.revertAnimation()
    }

}