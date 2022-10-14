package com.android.template.ui.utilities.caruselimageview

import android.annotation.SuppressLint
import android.view.Gravity
import android.widget.LinearLayout
import androidx.annotation.AnimatorRes
import androidx.annotation.DrawableRes
import com.android.template.R

class Config() {
    internal var width = -1
    internal var height = -1
    internal var margin = -1

    @SuppressLint("ResourceType")
    @AnimatorRes
    internal var animatorResId = R.anim.scale_with_alpha

    @AnimatorRes
    internal var animatorReverseResId = 0
    @DrawableRes
    internal var backgroundResId = R.drawable.white_radius
    @DrawableRes
    internal var unselectedBackgroundId: Int = 0
    internal var orientation = LinearLayout.HORIZONTAL
    internal var gravity = Gravity.CENTER

    class Builder {

        private val mConfig: Config = Config()

        fun width(width: Int): Builder {
            mConfig.width = width
            return this
        }

        fun height(height: Int): Builder {
            mConfig.height = height
            return this
        }

        fun margin(margin: Int): Builder {
            mConfig.margin = margin
            return this
        }

        fun animator(@AnimatorRes animatorResId: Int): Builder {
            mConfig.animatorResId = animatorResId
            return this
        }

        fun animatorReverse(@AnimatorRes animatorReverseResId: Int): Builder {
            mConfig.animatorReverseResId = animatorReverseResId
            return this
        }

        fun drawable(@DrawableRes backgroundResId: Int): Builder {
            mConfig.backgroundResId = backgroundResId
            return this
        }

        fun drawableUnselected(@DrawableRes unselectedBackgroundId: Int): Builder {
            mConfig.unselectedBackgroundId = unselectedBackgroundId
            return this
        }

        fun orientation(orientation: Int): Builder {
            mConfig.orientation = orientation
            return this
        }

        fun gravity(gravity: Int): Builder {
            mConfig.gravity = gravity
            return this
        }

        fun build(): Config {
            return mConfig
        }
    }
}