package com.android.template.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.databinding.ObservableField
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.recyclerview.widget.RecyclerView
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import com.android.template.R
import com.android.template.TemplateApp
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputEditText
import com.rule.validator.formvalidator.FormValidator
import java.io.File
import java.io.InputStream
import java.util.regex.Pattern


/** START Glide **/
/* Glide extension circle image with border */
/**
 * Load model into ImageView as a circle image with borderSize (optional) using Glide
 *
 * @param model - Any object supported by Glide (Uri, File, Bitmap, String, resource id as Int, ByteArray, and Drawable)
 * @param borderSize - The border size in pixel
 * @param borderColor - The border color
 */

fun <T> ImageView.loadCircularImage(
    model: T,
    borderSize: Float = 0F,
    borderColor: Int = Color.WHITE
) {
    Glide.with(context)
        .asBitmap()
        .load(model)
        .placeholder(R.drawable.avatar_placeholder)
        .apply(RequestOptions.circleCropTransform())
        .into(object : BitmapImageViewTarget(this) {
            override fun setResource(resource: Bitmap?) {
                setImageDrawable(
                    resource?.run {
                        RoundedBitmapDrawableFactory.create(
                            resources,
                            if (borderSize > 0) {
                                createBitmapWithBorder(borderSize, borderColor)
                            } else {
                                this
                            }
                        ).apply {
                            isCircular = true
                        }
                    }
                )
            }
        })
}

/**
 * Create a new bordered bitmap with the specified borderSize and borderColor
 *
 * @param borderSize - The border size in pixel
 * @param borderColor - The border color
 * @return A new bordered bitmap with the specified borderSize and borderColor
 */
fun Bitmap.createBitmapWithBorder(borderSize: Float, borderColor: Int = Color.WHITE): Bitmap {
    val borderOffset = (borderSize * 2).toInt()
    val halfWidth = width / 2
    val halfHeight = height / 2
    val circleRadius = Math.min(halfWidth, halfHeight).toFloat()
    val newBitmap = Bitmap.createBitmap(
        width + borderOffset,
        height + borderOffset,
        Bitmap.Config.ARGB_8888
    )

    // Center coordinates of the image
    val centerX = halfWidth + borderSize
    val centerY = halfHeight + borderSize

    val paint = Paint()
    val canvas = Canvas(newBitmap).apply {
        // Set transparent initial area
        drawARGB(0, 0, 0, 0)
    }

    // Draw the transparent initial area
    paint.isAntiAlias = true
    paint.style = Paint.Style.FILL
    canvas.drawCircle(centerX, centerY, circleRadius, paint)

    // Draw the image
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(this, borderSize, borderSize, paint)

    // Draw the createBitmapWithBorder
    paint.xfermode = null
    paint.style = Paint.Style.STROKE
    paint.color = borderColor
    paint.strokeWidth = borderSize
    canvas.drawCircle(centerX, centerY, circleRadius, paint)
    return newBitmap
    /* END Glide extension circle image with border */

    /** END Glide  **/
}

fun Int.getPluralStringFromResource(quantity: Int) =
    TemplateApp.appContext.resources.getQuantityString(this, quantity)

fun Int.getStringArrayFromResource() =
    TemplateApp.appContext.resources.getStringArray(this)

val Int.getStringFromResource: String
    get() = TemplateApp.appContext.applicationContext.getString(this)

fun Int.getColorFromResource() = ResourcesCompat.getColor(TemplateApp.appContext.resources, this, null)

val Int.toDp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

fun TextView.setBold(isBold: Boolean) {
    typeface = if (isBold) {
        Typeface.DEFAULT_BOLD
    } else {
        Typeface.DEFAULT
    }
}

fun EditText.setOnActionDoneCallback(callback: () -> Unit) =
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            callback()
        }
        false
    }

fun ObservableField<String>.getValueOrEmpty() = get() ?: ""

fun TextView.setTextWithClickableLink(
    text: String,
    clickablePart: String,
    onClickCallback: () -> Unit
) {
    val spannableString = SpannableString(text)
    val clickableSpan = object : ClickableSpan() {

        override fun updateDrawState(textPaint: TextPaint) {
            textPaint.color = R.color.blue.getColorFromResource()
            textPaint.isUnderlineText = true
        }

        override fun onClick(view: View) {
            Selection.setSelection((view as TextView).text as Spannable, 0)
            view.invalidate()
            onClickCallback()
        }
    }
    val startIndexOfLink = this.text.toString().indexOf(clickablePart)
    spannableString.setSpan(
        clickableSpan, startIndexOfLink, startIndexOfLink + clickablePart.length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    setText(spannableString)
    movementMethod = LinkMovementMethod.getInstance()
}

fun EditText.showKeyboard() =
    postDelayed({
        val imm = context?.getSystemService(
            Context.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        setSelection(text.length)
        isActivated = true
        isCursorVisible = true
        performClick()
        requestFocus()
    }, 500)

fun NavController.slide(target: Int, arguments: Bundle? = null) {
    val navBuilder = NavOptions.Builder()
    val enter = R.anim.left_to_right
    val exit = R.anim.right_to_left
    val popEnter = R.anim.pop_right_to_left
    val popExit = R.anim.pop_left_to_right
    navBuilder.setEnterAnim(enter).setExitAnim(exit)
        .setPopEnterAnim(popEnter).setPopExitAnim(popExit)


    navigate(target, arguments, navBuilder.build())
}

fun <T> Array<T>.findBy(block: (T) -> Boolean): T? {
    forEach {
        if (block(it)) {
            return it
        }
    }
    return null
}

fun IntArray.toQueryArray(query: String): String {
    val builder = StringBuilder()
    builder.append("?$query=")
    forEach {
        builder.append(it)
        if (indexOf(it) < size - 1)
            builder.append("&$query=")
    }
    return builder.toString()
}

fun Int.toHex(): String = "#" + Integer.toHexString(this)

fun String.isUrl(): Boolean = Patterns.WEB_URL.matcher(this).matches()

fun String.isEmail(): Boolean = Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isPhoneNumber(): Boolean = Patterns.PHONE.matcher(this).matches()

fun String.isValidPassword(lengthMin: Int = 6, lengthMax: Int = 64): Boolean =
    Pattern.compile(
        "^" +
                "(?=.*[0-9])" +
                "(?=.*[a-z])" +
                "(?=.*[A-Z])" +
                "(?=.*[a-zA-Z])" +
                ".{$lengthMin,$lengthMax}" +
                "$"
    ).matcher(this).matches()

fun String.isValidDateFormat(): Boolean {
    val regex = Regex("""\d{2}/\d{2}/\d{4}""")
    return regex.matches(this)
}

fun String.isValidName(): Boolean {

    val allowedSign = arrayListOf('-', '\'')

    return when {
        !this.toCharArray().all { char ->
            char.isLetter()
                    || char.isWhitespace()
                    || allowedSign.any { sing -> sing == char }
        } -> {
            false
        }
        else -> {
            true
        }
    }
}

fun String.removeHtml() = "<[^>]*>".toRegex().replace(this, "")

fun RecyclerView.scrollToEnd() {
    val lastPosition = adapter?.itemCount?.minus(1)
    lastPosition?.let { scrollToPosition(it) }
    smoothScrollToPosition(0)
}

fun Toolbar.initNavigationIcon(@DrawableRes iconId: Int, callback: () -> Unit) {
    setNavigationIcon(iconId)
    setNavigationOnClickListener {
        callback()
    }
}

fun TabLayout.setOnTabSelectedCallback(callback: (Int) -> Unit) =
    addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {
            // Implementation
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
            // Implementation
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            tab?.position?.let { callback(it) }
        }
    })


fun <T, V : ViewGroup> V.inflateList(
    list: List<T>,
    inflateViewCallback: V.(T, LayoutInflater) -> View?
) = postApply {
    removeAllViews()
    val layoutInflater = LayoutInflater.from(context)
    list.forEach {
        inflateViewCallback(it, layoutInflater)?.let { view -> addView(view) }
    }
}

fun <T : ViewGroup> T.postApply(block: T.() -> Unit) = post { apply(block) }

inline fun <reified T> Any.takeAs(): T? = if (this is T) {
    this
} else {
    null
}

fun NavController.setupNavigationButton(view: View?, navigationId: Int, bundle: Bundle?) =
    view?.setOnClickListener { navigate(navigationId, bundle) }

fun NavController.setupNavigationButton(
    view: View?,
    navigationId: Int,
    bundle: ((() -> Bundle?)?) = null
) = view?.setOnClickListener { navigate(navigationId, bundle?.invoke()) }

fun TextInputEditText.afterTextChanged(error : ObservableField<String>){
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            error.set(null)
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            // Implementation
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            // Implementation
        }
    })
}

fun File.encryptedFile(): EncryptedFile {
    val cryptoFileKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    return EncryptedFile.Builder(
        this,
        TemplateApp.appContext,
        cryptoFileKey,
        EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
    ).build()
}

fun Button.setOnClickListenerWithPreValidation(formValidator: FormValidator, onClick: () -> Unit) {
    setOnClickListener {
        if (formValidator.preValidateAllFields()) {
            onClick()
        }
    }
}

fun EditText.setOnActionDoneCallbackWithPreValidation(formValidator: FormValidator, callback: () -> Unit) =
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE && formValidator.preValidateAllFields()) {
            callback()
        }
        false
    }

fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

fun Uri.writeFileContent(context: Context): String? {
    val fileBrowserCacheDir = "CertCache"
    val certCacheDir = File(context.getExternalFilesDir(null), fileBrowserCacheDir)

    if (!certCacheDir.exists() && !certCacheDir.mkdirs()) {
        return null
    }

    val filePath = this.getFileDisplayName(context)?.let { File(certCacheDir, it).absolutePath }

    val selectedFileInputStream: InputStream? = try {
        context.contentResolver?.openInputStream(this)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
    val selectedFileOutPutStream = filePath?.let { File(it).outputStream() }

    selectedFileInputStream?.use { input ->
        selectedFileOutPutStream.use { output ->
            output?.let { input.copyTo(it, bufferSize = 1024) }
        }
    }

    return filePath
}

fun Uri.getFileDisplayName(context: Context): String? {
    var displayName: String? = null
    context.contentResolver
        .query(this, null, null, null, null, null).use { cursor ->
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                displayName = cursor.getString(columnIndex)
            }
        }
    if (displayName.isNullOrEmpty()) {
        val path = this.toString()
        val cut: Int = path.lastIndexOf('/')
        if (cut != -1) {
            displayName = path.substring(cut + 1)
        }
    }
    return displayName
}

fun Context.getDrawableCompat(@DrawableRes drawableRes: Int): Drawable? {
    return AppCompatResources.getDrawable(this, drawableRes)
}