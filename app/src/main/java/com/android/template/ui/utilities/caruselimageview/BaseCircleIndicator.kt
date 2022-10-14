package com.android.template.ui.utilities.caruselimageview

import android.animation.Animator
import android.animation.AnimatorInflater
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.animation.Interpolator
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import com.android.template.R
import kotlin.math.abs

open class BaseCircleIndicator: LinearLayout {
    private val DEFAULT_INDICATOR_WIDTH = 5

    protected var mIndicatorMargin = -1
    protected var mIndicatorWidth = -1
    protected var mIndicatorHeight = -1

    protected var mIndicatorBackgroundResId: Int = 0
    protected var mIndicatorUnselectedBackgroundResId: Int = 0

    protected lateinit var mAnimatorOut: Animator
    protected lateinit var mAnimatorIn: Animator
    protected lateinit var mImmediateAnimatorOut: Animator
    protected lateinit var mImmediateAnimatorIn: Animator

    protected var mLastPosition = -1

    constructor(context: Context): super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val config = handleTypedArray(attrs)
        initialize(config)
    }

    private fun handleTypedArray(attrs: AttributeSet?): Config {
        val config = Config()
        return if (attrs == null) {
            config
        } else config

    }

    private fun initialize(config: Config) {
        val miniSize = (TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            DEFAULT_INDICATOR_WIDTH.toFloat(), resources.displayMetrics
        ) + 0.5f).toInt()
        mIndicatorWidth = if (config.width < 0) miniSize else config.width
        mIndicatorHeight = if (config.height < 0) miniSize else config.height
        mIndicatorMargin = if (config.margin < 0) miniSize else config.margin

        mAnimatorOut = createAnimatorOut(config)
        mImmediateAnimatorOut = createAnimatorOut(config)
        mImmediateAnimatorOut.duration = 0

        mAnimatorIn = createAnimatorIn(config)
        mImmediateAnimatorIn = createAnimatorIn(config)
        mImmediateAnimatorIn.duration = 0

        mIndicatorBackgroundResId =
            if (config.backgroundResId == 0) R.drawable.white_radius else config.backgroundResId
        mIndicatorUnselectedBackgroundResId = if (config.unselectedBackgroundId == 0)
            config.backgroundResId
        else
            config.unselectedBackgroundId

        orientation =
            if (config.orientation == VERTICAL) VERTICAL else HORIZONTAL
        gravity = if (config.gravity >= 0) config.gravity else Gravity.CENTER
    }

    protected fun createAnimatorOut(config: Config): Animator {
        return AnimatorInflater.loadAnimator(context, config.animatorResId)
    }

    protected fun createAnimatorIn(config: Config): Animator {
        val animatorIn: Animator
        if (config.animatorReverseResId == 0) {
            animatorIn = AnimatorInflater.loadAnimator(context, config.animatorResId)
            animatorIn.interpolator = ReverseInterpolator()
        } else {
            animatorIn = AnimatorInflater.loadAnimator(context, config.animatorReverseResId)
        }
        return animatorIn
    }

    protected fun createIndicators(count: Int, currentPosition: Int) {
        val orientation = orientation
        for (i in 0 until count) {
            if (currentPosition == i) {
                addIndicator(orientation, mIndicatorBackgroundResId, mImmediateAnimatorOut)
            } else {
                addIndicator(
                    orientation, mIndicatorUnselectedBackgroundResId,
                    mImmediateAnimatorIn
                )
            }
        }
    }

    protected fun addIndicator(
        orientation: Int, @DrawableRes backgroundDrawableId: Int,
        animator: Animator
    ) {
        if (animator.isRunning) {
            animator.end()
            animator.cancel()
        }
        val indicator = View(context)
        indicator.setBackgroundResource(backgroundDrawableId)
        addView(indicator, mIndicatorWidth, mIndicatorHeight)
        val lp = indicator.layoutParams as LayoutParams

        if (orientation == HORIZONTAL) {
            lp.leftMargin = mIndicatorMargin
            lp.rightMargin = mIndicatorMargin
        } else {
            lp.topMargin = mIndicatorMargin
            lp.bottomMargin = mIndicatorMargin
        }

        indicator.layoutParams = lp
        animator.setTarget(indicator)
        animator.start()
    }

    protected fun internalPageSelected(position: Int) {
        if (mAnimatorIn.isRunning) {
            mAnimatorIn.end()
            mAnimatorIn.cancel()
        }

        if (mAnimatorOut.isRunning) {
            mAnimatorOut.end()
            mAnimatorOut.cancel()
        }

        val currentIndicator = getChildAt(mLastPosition)
        if (mLastPosition >= 0 && currentIndicator != null) {
            currentIndicator.setBackgroundResource(mIndicatorUnselectedBackgroundResId)
            mAnimatorIn.setTarget(currentIndicator)
            mAnimatorIn.start()
        }

        val selectedIndicator = getChildAt(position)
        if (selectedIndicator != null) {
            selectedIndicator.setBackgroundResource(mIndicatorBackgroundResId)
            mAnimatorOut.setTarget(selectedIndicator)
            mAnimatorOut.start()
        }
    }

    protected inner class ReverseInterpolator : Interpolator {
        override fun getInterpolation(value: Float): Float {
            return abs(1.0f - value)
        }
    }
}