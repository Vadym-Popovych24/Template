package com.android.template.utils.custom.speedometer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import com.android.template.R

open class SpeedView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : Speedometer(context, attrs, defStyleAttr) {

    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val speedometerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val speedometerRect = RectF()

    /**
     * change the width of the center circle.
     */
    private var centerCircleRadius = dpTOpx(12f)
        set(centerCircleRadius) {
            field = centerCircleRadius
            if (isAttachedToWindow)
                invalidate()
        }

    init {
        init()
        initAttributeSet(context, attrs)
    }

    override fun defaultSpeedometerValues() {
        indicator = NormalIndicator(context)
        super.marksNumber = 4
    }

    private fun init() {
        speedometerPaint.style = Paint.Style.STROKE
        circlePaint.color = 0xff020200.toInt()
    }

    private fun initAttributeSet(context: Context, attrs: AttributeSet?) {
        if (attrs == null) {
            return
        }
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.SpeedView, 0, 0)

        circlePaint.color =
            a.getColor(R.styleable.SpeedView_sv_centerCircleColor, circlePaint.color)
        centerCircleRadius =
            a.getDimension(R.styleable.SpeedView_sv_centerCircleRadius, centerCircleRadius)
        maxSpeed = 100f
        a.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldW: Int, oldH: Int) {
        super.onSizeChanged(w, h, oldW, oldH)

        updateBackgroundBitmap()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawIndicator(canvas)
        canvas.drawCircle(size * .5f, size * .5f, centerCircleRadius, circlePaint)
    }

    override fun updateBackgroundBitmap() {
        val c = createBackgroundBitmapCanvas()

        sections.forEach {
            val risk = it.width * .5f + padding + it.padding
            speedometerRect.set(risk, risk, size - risk, size - risk)
            speedometerPaint.strokeWidth = it.width
            speedometerPaint.color = it.color
            val startAngle = (getEndDegree() - getStartDegree()) * it.startOffset + getStartDegree()
            val sweepAngle =
                (getEndDegree() - getStartDegree()) * it.endOffset - (startAngle - getStartDegree())

            speedometerPaint.strokeCap = Paint.Cap.BUTT
            c.drawArc(speedometerRect, startAngle, sweepAngle, false, speedometerPaint)
        }

        drawMarks(c)

        drawDefMinMaxSpeedPosition(c)
    }
}