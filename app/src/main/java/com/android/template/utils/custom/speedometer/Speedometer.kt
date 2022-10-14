package com.android.template.utils.custom.speedometer

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.android.template.R
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.text.Typography.degree

abstract class Speedometer @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : Gauge(context, attrs, defStyleAttr) {
    /**
     * needle point to [currentSpeed], cannot be null
     *
     * add custom [indicator](https://github.com/anastr/SpeedView/wiki/Indicators).
     */
    var indicator: Indicator<*> = NormalIndicator(context)
        set(indicator) {
            field.deleteObservers()
            indicator.setTargetSpeedometer(this)
            field = indicator
            if (isAttachedToWindow) {
                this.indicator.setTargetSpeedometer(this)
                invalidate()
            }
        }

    /**
     * light effect behind the [indicator].
     */
    private var isWithIndicatorLight = false

    /**
     * indicator light's color.
     * this indicator we can see when indicator(arrow) speed up or down
     * it is look like shadow
     * @see isWithIndicatorLight
     */
    private var indicatorLightColor = 0xBBFF5722.toInt()

    private val circleBackPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0x00FFFFFF //background color in view indicator and sections
    }

    private val indicatorLightPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val markPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val markPath = Path()

    var marksNumber = 0
        set(marksNumber) {
            field = marksNumber
            invalidateGauge()
        }
    /**
     * change the color of all marks (if exist),
     * **this option is not available for all Speedometers**.
     */
    private var markColor
        get() = markPaint.color
        set(markColor) {
            markPaint.color = markColor
        }

    private var marksPadding = 0f
        set(marksPadding) {
            field = marksPadding
            invalidateGauge()
        }

    private var markHeight = dpTOpx(9f)
        set(markHeight) {
            field = markHeight
            invalidateGauge()
        }
    private var markWidth
        get() = markPaint.strokeWidth
        set(markWidth) {
            markPaint.strokeWidth = markWidth
            invalidateGauge()
        }

    private var startDegree = 180 // when i change this i change rotation
    private var endDegree = 90 + 270

    /**
     * to rotate indicator
     * @return current degree where indicator must be.
     */
    private var degree = startDegree.toFloat()

    /** padding to fix speedometer cut when change [.speedometerMode]  */
    private var cutPadding = 0

    private var lastPercentSpeed = 0f

    /**
     * @return size of speedometer.
     */
    val size: Int
        get() = width

    /**
     * @return size of speedometer without padding.
     */
    private val sizePa: Int
        get() = size - padding * 2


    init {
        init()
        initAttributeSet(context, attrs)
    }

    private fun init() {
        indicatorLightPaint.style = Paint.Style.STROKE
        markPaint.style = Paint.Style.STROKE
        markColor = 0xFFFFFFFF.toInt()
        markWidth = dpTOpx(3f)
        defaultSpeedometerValues()
    }

    private fun initAttributeSet(context: Context, attrs: AttributeSet?) {
        if (attrs == null)
            return
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.Speedometer, 0, 0)

        marksNumber = a.getInt(R.styleable.Speedometer_sv_marksNumber, marksNumber)
        marksPadding = a.getDimension(R.styleable.Speedometer_sv_marksPadding, marksPadding)
        markHeight = a.getDimension(R.styleable.Speedometer_sv_markHeight, markHeight)
        markWidth = a.getDimension(R.styleable.Speedometer_sv_markWidth, markWidth)
        markColor = a.getColor(R.styleable.Speedometer_sv_markColor, markColor)

        startDegree = a.getInt(R.styleable.Speedometer_sv_startDegree, startDegree)
        endDegree = a.getInt(R.styleable.Speedometer_sv_endDegree, endDegree)
        cutPadding = a.getDimension(R.styleable.Speedometer_sv_cutPadding, cutPadding.toFloat()).toInt()
        indicator.width = a.getDimension(R.styleable.Speedometer_sv_indicatorWidth, indicator.width)
        indicator.color = a.getColor(R.styleable.Speedometer_sv_indicatorColor, indicator.color)
        isWithIndicatorLight = a.getBoolean(R.styleable.Speedometer_sv_withIndicatorLight, isWithIndicatorLight)
        indicatorLightColor = a.getColor(R.styleable.Speedometer_sv_indicatorLightColor, indicatorLightColor)
        degree = startDegree.toFloat()
        a.recycle()
        checkStartAndEndDegree()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val defaultSize = dpTOpx(250f).toInt()

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val w = MeasureSpec.getSize(widthMeasureSpec)
        val h = MeasureSpec.getSize(heightMeasureSpec)

        var size = if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY)
            min(w, h)
        else if (widthMode == MeasureSpec.EXACTLY)
            w
        else if (heightMode == MeasureSpec.EXACTLY)
            h
        else if ((widthMode == MeasureSpec.UNSPECIFIED && heightMode == MeasureSpec.UNSPECIFIED)
            || (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST))
            min(defaultSize, min(w, h))
        else {
            if (widthMode == MeasureSpec.AT_MOST)
                min(defaultSize, w)
            else
                min(defaultSize, h)
        }

        size = max(size, max(suggestedMinimumWidth, suggestedMinimumHeight))

        setMeasuredDimension(size / 1, (size / 1.7).toInt())
    }

    override fun onSizeChanged(w: Int, h: Int, oldW: Int, oldH: Int) {
        super.onSizeChanged(w, h, oldW, oldH)
        indicator.updateIndicator()
    }

    private fun checkStartAndEndDegree() {
        require(startDegree >= 0) { "StartDegree can\'t be Negative" }
        require(endDegree >= 0) { "EndDegree can\'t be Negative" }
        require(startDegree < endDegree) { "EndDegree must be bigger than StartDegree !" }
        require(endDegree - startDegree <= 360) { "(EndDegree - StartDegree) must be smaller than 360 !" }
        require(startDegree >= 0) {
            "StartDegree must be bigger than 0 !" }
        require(endDegree <= (360 * 2)) {
            "EndDegree must be smaller than ${360 * 2} !" }
    }

    /**
     * add default values for Speedometer inside this method,
     * call super setting method to set default value,
     * Ex :
     *
     * `super.setBackgroundCircleColor(Color.TRANSPARENT);`
     */
    protected abstract fun defaultSpeedometerValues()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        degree = getDegreeAtSpeed(currentSpeed)
    }

    /**
     * draw marks depending on [marksNumber],
     * this method must be called in subSpeedometer's [updateBackgroundBitmap] method.
     * @param canvas marks should be drawn on [backgroundBitmap].
     */
    protected fun drawMarks(canvas: Canvas) {
        markPath.reset()
        markPath.moveTo(size * .5f, marksPadding + padding)
        markPath.lineTo(size * .5f, marksPadding + markHeight + padding)

        canvas.save()
        canvas.rotate(90f + getStartDegree(), size * .5f, size * .5f)
        val everyDegree = (getEndDegree() - getStartDegree()) / (marksNumber + 1f)
        for (i in 1..marksNumber) {
            canvas.rotate(everyDegree, size * .5f, size * .5f)
            canvas.drawPath(markPath, markPaint)
        }
        canvas.restore()
    }

    /**
     * draw indicator at current [degree],
     * this method must be called in subSpeedometer's `onDraw` method.
     * @param canvas view canvas to draw.
     */
    protected fun drawIndicator(canvas: Canvas) {
        if (isWithIndicatorLight)
            drawIndicatorLight(canvas)
        indicator.draw(canvas, degree)
    }

    private fun drawIndicatorLight(canvas: Canvas) {
        val MAX_LIGHT_SWEEP = 30f
        var sweep = abs(getPercentSpeed() - lastPercentSpeed) * MAX_LIGHT_SWEEP
        lastPercentSpeed = getPercentSpeed()
        if (sweep > MAX_LIGHT_SWEEP)
            sweep = MAX_LIGHT_SWEEP
        val colors = intArrayOf(indicatorLightColor, 0x00FFFFFF)
        val lightSweep = SweepGradient(size * .5f, size * .5f, colors, floatArrayOf(0f, sweep / 360f))
        indicatorLightPaint.shader = lightSweep
        indicatorLightPaint.strokeWidth = indicator.getLightBottom() - indicator.getTop()

        val risk = indicator.getTop() + indicatorLightPaint.strokeWidth * .5f
        val speedometerRect = RectF(risk, risk, size - risk, size - risk)
        canvas.save()
        canvas.rotate(degree, size * .5f, size * .5f)
        if (isSpeedIncrease)
            canvas.scale(1f, -1f, size * .5f, size * .5f)
        canvas.drawArc(speedometerRect, 0f, sweep, false, indicatorLightPaint)
        canvas.restore()
    }

    /**
     * create canvas to draw [backgroundBitmap].
     * @return [backgroundBitmap]'s canvas.
     */
    override fun createBackgroundBitmapCanvas(): Canvas {
        if (size == 0)
            return Canvas()
        backgroundBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(backgroundBitmap)
        canvas.drawCircle(size * .5f, size * .5f, size * .5f - padding, circleBackPaint)

        // to fix preview mode issue
        canvas.clipRect(0, 0, size, size)

        return canvas
    }

    /**
     * @param speed to know the degree at it.
     * @return current Degree at that speed.
     */
    private fun getDegreeAtSpeed(speed: Float): Float {
        return (speed - minSpeed) * (endDegree - startDegree) / (maxSpeed - minSpeed) + startDegree
    }

    protected fun getStartDegree(): Int {
        return startDegree
    }

    protected fun getEndDegree(): Int {
        return endDegree
    }

    /**
     * draw minSpeedText and maxSpeedText at default Position.
     * @param c canvas to draw.
     */
    protected fun drawDefMinMaxSpeedPosition(c: Canvas) {
        textPaint.textAlign = when {
            startDegree % 360 <= 90 -> Paint.Align.RIGHT
            startDegree % 360 <= 180 -> Paint.Align.LEFT
            startDegree % 360 <= 270 -> Paint.Align.CENTER
            else -> Paint.Align.RIGHT
        }
        var tickStart: CharSequence? = null

        if (tickStart == null)
            tickStart = minSpeed.toInt().toString()
        c.save()
        c.rotate(startDegree + 90f, size * .5f, size * .5f)
        c.rotate(-(startDegree + 90f), sizePa * .5f - textPaint.textSize + padding, textPaint.textSize + padding)
//        c.drawText(tickStart.toString(), sizePa * .5f - textPaint.textSize + padding, textPaint.textSize + padding, textPaint)
        c.restore()
        textPaint.textAlign = when {
            endDegree % 360 <= 90 -> Paint.Align.RIGHT
            endDegree % 360 <= 180 -> Paint.Align.LEFT
            endDegree % 360 <= 270 -> Paint.Align.CENTER
            else -> Paint.Align.RIGHT
        }
        var tickEnd: CharSequence? = null

        if (tickEnd == null)
            tickEnd = maxSpeed.toInt().toString()
        c.save()
        c.rotate(endDegree + 90f, size * .5f, size * .5f)
        c.rotate(-(endDegree + 90f), sizePa * .5f + textPaint.textSize + padding.toFloat(), textPaint.textSize + padding)
//        c.drawText(tickEnd.toString(), sizePa * .5f + textPaint.textSize + padding.toFloat(), textPaint.textSize + padding, textPaint)
        c.restore()
    }

}