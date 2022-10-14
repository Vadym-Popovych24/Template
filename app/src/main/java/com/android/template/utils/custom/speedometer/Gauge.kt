package  com.android.template.utils.custom.speedometer

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import  com.android.template.R
import java.util.*
import kotlin.math.max

typealias SpeedTextListener = (speed: Float) -> CharSequence


/**
 * do an action on all [Gauge.sections], with
 * only one redraw (after complete) to avoid redrawing
 * the speedometer on every change.
 * @param [action] an action to invoke for every section.
 */
fun Gauge.doOnSections(action: (section: Section) -> Unit) {
    val sections = ArrayList(this.sections)
    // this will also clear observers.
    this.clearSections()
    sections.forEach { action.invoke(it) }
    this.addSections(sections)
}

/**
 * A callback that notifies clients when the speed has been
 * changed (just when speed change in integer).
 *
 * Notification that the speed has changed.
 *
 * gauge the gauge who change.
 * sSpeedUp if speed increase.
 */
typealias OnSpeedChangeListener = (gauge: Gauge, isSpeedUp: Boolean) -> Unit

abstract class Gauge constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr),
    Observer {
    protected var textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private val speedTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)

    private var _minSpeed = 0f
    private var _maxSpeed = 100f

    /**
     * the min range in speedometer, `default = 0`.
     *
     * any change will move [currentSpeed] to its new position
     * immediately without animation.
     *
     * @throws IllegalArgumentException if `minSpeed >= maxSpeed`
     */
    var minSpeed: Float
        get() = _minSpeed
        set(value) = setMinMaxSpeed(value, maxSpeed)

    /**
     * the max range in speedometer, `default = 100`.
     *
     * any change will move [currentSpeed] to its new position
     * immediately without animation.
     *
     * @throws IllegalArgumentException if `minSpeed >= maxSpeed`
     */
    var maxSpeed: Float
        get() = _maxSpeed
        set(value) = setMinMaxSpeed(minSpeed, value)

    /**
     * @return the last speed which you set by [speedTo]
     * or if you stop speedometer By [stop] method.
     *
     * @see currentSpeed
     */
    private var speed = minSpeed

    /**
     * what is speed now in **integer**.
     * safe method to handle all speed values in [onSpeedChangeListener].
     *
     * @return current speed in Integer
     * @see currentSpeed
     */
    private var currentIntSpeed = 0

    /**
     * what is speed now in **float**.
     * Need for check if speed increase
     *
     * @return current speed now.
     * @see speed
     */
    var currentSpeed = minSpeed
        private set

    /**
     * given a state of the speed change if it's increase or decrease.
     * @return is speed increase in the last change or not.
     */
    var isSpeedIncrease = false
        private set

    private lateinit var speedAnimator: ValueAnimator
    private var canceled = false

    /**
     * Register a callback to be invoked when speed value changed (in integer).
     * maybe null.
     */
    private var onSpeedChangeListener: OnSpeedChangeListener? = null

    /** to contain all drawing that don't change  */
    protected var backgroundBitmap: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    private val backgroundBitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    var padding = 0
        private set

    /**
     * view width without padding
     * @return View width without padding.
     */
    private var widthPa = 0

    /**
     * View height without padding
     * @return View height without padding.
     */
    private var heightPa = 0

    /** All sections -_Read Only_- */
    val sections = ArrayList<Section>()

    var currentSection: Section? = null
        private set

    /**
     * the width of speedometer's bar in pixel.
     */
    open var speedometerWidth = dpTOpx(20f)
        set(speedometerWidth) {
            field = speedometerWidth
            doOnSections { it.width = speedometerWidth }
            if (isAttachedToWindow)
                invalidateGauge()
        }

    private var attachedToWindow = false

    /**
     * @return canvas translate dx.
     */
    private var translatedDx = 0f

    /**
     * @return canvas translate dy.
     */
    private var translatedDy = 0f

    /**
     * Number expresses the Acceleration, between (0, 1]
     *
     * must be between `(0, 1]`, default value 0.1f.
     * @throws IllegalArgumentException if `accelerate` out of range.
     */
    private var accelerate = .1f
        set(accelerate) {
            field = accelerate
            checkAccelerate()
        }

    /**
     * Number expresses the Deceleration, between (0, 1]
     *
     * must be between `(0, 1]`, default value 0.1f.
     * @throws IllegalArgumentException if `decelerate` out of range.
     */
    private var decelerate = .1f
        set(decelerate) {
            field = decelerate
            checkDecelerate()
        }

    /**
     * Speed-Unit Text padding in pixel,
     * this value will be ignored if `{ #speedTextPosition} == Position.CENTER`.
     */
    private var speedTextPadding = dpTOpx(24f)
        set(speedTextPadding) {
            field = speedTextPadding
            if (attachedToWindow)
                invalidate()
        }

    /**
     * number of decimal places
     *
     * change speed text's format by custom text.
     */
    private var speedTextListener: SpeedTextListener = { speed -> speed.toString() }
        set(speedTextFormat) {
            field = speedTextFormat
            invalidateGauge()
        }

    init {
        init()
        initAttributeSet(context, attrs)
    }

    private fun init() {
        textPaint.color = 0xFF000000.toInt()
        textPaint.textSize = dpTOpx(10f)
        textPaint.textAlign = Paint.Align.CENTER

        speedTextPaint.color = 0xFF000000.toInt()
        speedTextPaint.textSize = dpTOpx(18f)

        sections.add(Section(.0f, .2f, 0xFFFF0000.toInt(), speedometerWidth).inGauge(this))
        sections.add(Section(.2f, .4f, Color.parseColor("#FB6A45"), speedometerWidth).inGauge(this))
        sections.add(Section(.4f, .6f, Color.parseColor("#FD9640"), speedometerWidth).inGauge(this))
        sections.add(Section(.6f, .8f, Color.parseColor("#ADFF2F"), speedometerWidth).inGauge(this))
        sections.add(Section(.8f, 1f, Color.parseColor("#7FFF00"), speedometerWidth).inGauge(this))

        speedAnimator = ValueAnimator.ofFloat(0f, 1f)
    }

    private fun initAttributeSet(context: Context, attrs: AttributeSet?) {
        if (attrs == null)
            return
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.Gauge, 0, 0)

        maxSpeed = a.getFloat(R.styleable.Gauge_sv_maxSpeed, maxSpeed)
        minSpeed = a.getFloat(R.styleable.Gauge_sv_minSpeed, minSpeed)

        speed = minSpeed
        currentSpeed = minSpeed

        speedometerWidth = a.getDimension(R.styleable.Gauge_sv_speedometerWidth, speedometerWidth)
        sections.forEach { it.width = speedometerWidth }

        textPaint.color = a.getColor(R.styleable.Gauge_sv_textColor, textPaint.color)
        textPaint.textSize = a.getDimension(R.styleable.Gauge_sv_textSize, textPaint.textSize)

        speedTextPaint.color = a.getColor(R.styleable.Gauge_sv_speedTextColor, speedTextPaint.color)
        speedTextPaint.textSize =
            a.getDimension(R.styleable.Gauge_sv_speedTextSize, speedTextPaint.textSize)

        accelerate = a.getFloat(R.styleable.Gauge_sv_accelerate, accelerate)
        decelerate = a.getFloat(R.styleable.Gauge_sv_decelerate, decelerate)

        speedTextPadding = a.getDimension(R.styleable.Gauge_sv_speedTextPadding, speedTextPadding)

        val speedTypefacePath = a.getString(R.styleable.Gauge_sv_speedTextTypeface)
        if (speedTypefacePath != null && !isInEditMode)
            speedTextTypeface = Typeface.createFromAsset(context.assets, speedTypefacePath)

        val typefacePath = a.getString(R.styleable.Gauge_sv_textTypeface)
        if (typefacePath != null && !isInEditMode)
            textTypeface = Typeface.createFromAsset(context.assets, typefacePath)

        a.recycle()
        checkAccelerate()
        checkDecelerate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldW: Int, oldH: Int) {
        super.onSizeChanged(w, h, oldW, oldH)
        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
    }

    private fun checkAccelerate() {
        require(!(accelerate > 1f || accelerate <= 0)) { "accelerate must be between (0, 1]" }
    }

    private fun checkDecelerate() {
        require(!(decelerate > 1f || decelerate <= 0)) { "decelerate must be between (0, 1]" }
    }

    internal fun checkSection(section: Section) {
        val i = sections.indexOf(section)
        require(section.startOffset < section.endOffset) { "endOffset must be bigger than startOffset" }
        sections.getOrNull(i - 1)?.let {
            require(
                it.endOffset <= section.startOffset
                        && it.endOffset < section.endOffset
            ) { "Section at index ($i) is conflicted with previous section" }
        }
        sections.getOrNull(i + 1)?.let {
            require(
                it.startOffset >= section.endOffset
                        && it.startOffset > section.startOffset
            ) { "Section at index ($i) is conflicted with next section" }
        }
    }

    /**
     * convert dp to **pixel**.
     * @param dp to convert.
     * @return Dimension in pixel.
     */
    fun dpTOpx(dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }

    /**
     * convert pixel to **dp**.
     * @param px to convert.
     * @return Dimension in dp.
     */
    fun pxTOdp(px: Float): Float {
        return px / context.resources.displayMetrics.density
    }

    /**
     * notice that [backgroundBitmap] must recreate.
     */
    protected abstract fun updateBackgroundBitmap()

    /**
     * notice that padding or size have changed.
     */
    private fun updatePadding(left: Int, top: Int, right: Int, bottom: Int) {
        padding = max(max(left, right), max(top, bottom))
        widthPa = width - padding * 2
        heightPa = height - padding * 2
    }

    /**
     * get current speed as string to **Draw**.
     */
    protected fun getSpeedText() = speedTextListener.invoke(currentSpeed)

    /**
     * get current speed as **percent**.
     * @return percent speed, between [0,100].
     */
    fun getPercentSpeed(): Float = (currentSpeed - minSpeed) * 100f / (maxSpeed - minSpeed)

    /**
     * @return offset speed, between [0,1].
     */
    fun getOffsetSpeed(): Float = (currentSpeed - minSpeed) / (maxSpeed - minSpeed)

    /**
     * change all text color without **speed, unit text**.
     *
     * @see speedTextColor
     */
    var textColor: Int
        get() = textPaint.color
        set(textColor) {
            textPaint.color = textColor
            invalidateGauge()
        }

    /**
     * change just speed text color.
     *
     * @see textColor
     */
    private var speedTextColor: Int
        get() = speedTextPaint.color
        set(speedTextColor) {
            speedTextPaint.color = speedTextColor
            if (attachedToWindow)
                invalidate()
        }

    /**
     * change all text size without **speed and unit text**.
     *
     * @see dpTOpx
     * @see speedTextSize
     */
    var textSize: Float
        get() = textPaint.textSize
        set(textSize) {
            textPaint.textSize = textSize
            if (attachedToWindow)
                invalidate()
        }

    /**
     * change just speed text size.
     *
     * @see dpTOpx
     * @see textSize
     */
    private var speedTextSize: Float
        get() = speedTextPaint.textSize
        set(speedTextSize) {
            speedTextPaint.textSize = speedTextSize
            if (attachedToWindow)
                invalidate()
        }

    val viewSize: Int
        get() = max(width, height)

    val viewSizePa: Int
        get() = max(widthPa, heightPa)

    /**
     * Maybe null, change typeface for **speed and unit** text.
     */
    private var speedTextTypeface: Typeface?
        get() = speedTextPaint.typeface
        set(typeface) {
            speedTextPaint.typeface = typeface
            invalidateGauge()
        }

    /**
     * Maybe null, change typeface for all texts without speed and unit text.
     */
    private var textTypeface: Typeface?
        get() = textPaint.typeface
        set(typeface) {
            textPaint.typeface = typeface
            invalidateGauge()
        }

    override fun onDraw(canvas: Canvas) {
        canvas.translate(translatedDx, translatedDy)

        canvas.drawBitmap(backgroundBitmap, 0f, 0f, backgroundBitmapPaint)

        // check onSpeedChangeEvent.
        val newSpeed = currentSpeed.toInt()
        if (newSpeed != currentIntSpeed && onSpeedChangeListener != null) {
            val isSpeedUp = newSpeed > currentIntSpeed
            val update = if (isSpeedUp) 1 else -1
            // this loop to pass on all speed values,
            // to safe handle by call gauge.getCorrectIntSpeed().
            while (currentIntSpeed != newSpeed) {
                currentIntSpeed += update
                onSpeedChangeListener!!.invoke(this, isSpeedUp)
            }
        }
        currentIntSpeed = newSpeed
    }

    /**
     * create canvas to draw [backgroundBitmap].
     * @return [backgroundBitmap]'s canvas.
     */
    protected open fun createBackgroundBitmapCanvas(): Canvas {
        if (width == 0 || height == 0)
            return Canvas()
        backgroundBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        return Canvas(backgroundBitmap)
    }

    /**
     * stop speedometer
     * use this method just when you wont to stop [speedTo].
     */
    private fun stop() {
        if (!speedAnimator.isRunning)
            return
        speed = currentSpeed
        cancelSpeedAnimator()
    }

    /**
     * cancel all animators
     */
    private fun cancelSpeedAnimator() {
        cancelSpeedMove()
    }

    private fun cancelSpeedMove() {
        canceled = true
        speedAnimator.cancel()
        canceled = false
    }

    /**
     * move speed value to new speed without animation.
     * @param speed current speed to move.
     */
    private fun setSpeedAt(speed: Float) {
        var newSpeed = speed
        newSpeed =
            if (newSpeed > maxSpeed) maxSpeed else if (newSpeed < minSpeed) minSpeed else newSpeed
        isSpeedIncrease = newSpeed > currentSpeed
        this.speed = newSpeed
        this.currentSpeed = newSpeed
        cancelSpeedAnimator()
        invalidate()
    }

    /**
     * move speed to current value smoothly with animation duration,
     * it should be between [[minSpeed], [maxSpeed]].
     *
     * if `speed > maxSpeed` speed value will move to [maxSpeed].
     *
     * if `speed < minSpeed` speed value will move to [minSpeed].
     *
     * @param speed current speed to move.
     * @param moveDuration The length of animation, in milliseconds.
     * This value cannot be negative (2000 by default).
     *
     * @see speedTo
     */
    @JvmOverloads
    fun speedTo(speed: Float, moveDuration: Long = 2000) {
        var newSpeed = speed
        newSpeed =
            if (newSpeed > maxSpeed) maxSpeed else if (newSpeed < minSpeed) minSpeed else newSpeed
        if (newSpeed == this.speed)
            return
        this.speed = newSpeed

        isSpeedIncrease = newSpeed > currentSpeed

        cancelSpeedAnimator()
        speedAnimator = ValueAnimator.ofFloat(currentSpeed, newSpeed)
        speedAnimator.interpolator = DecelerateInterpolator()
        speedAnimator.duration = moveDuration
        speedAnimator.addUpdateListener {
            currentSpeed = speedAnimator.animatedValue as Float
            postInvalidate()
        }
        speedAnimator.start()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        attachedToWindow = true
        if (!isInEditMode) {
            updateBackgroundBitmap()
            invalidate()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        cancelSpeedAnimator()
        attachedToWindow = false
    }

    /**
     * change Min and Max speed.
     *
     * this method will move [currentSpeed] to its new position
     * immediately without animation.
     *
     * @param minSpeed new MIN Speed.
     * @param maxSpeed new MAX Speed.
     *
     * @throws IllegalArgumentException if `minSpeed >= maxSpeed`
     */
    private fun setMinMaxSpeed(minSpeed: Float, maxSpeed: Float) {
        require(minSpeed < maxSpeed) { "minSpeed must be smaller than maxSpeed !!" }
        cancelSpeedAnimator()
        _minSpeed = minSpeed
        _maxSpeed = maxSpeed
        invalidateGauge()
        if (attachedToWindow)
            setSpeedAt(speed)
    }

    /**
     * add list of sections to the gauge.
     * @throws IllegalArgumentException if [Section.startOffset] or [Section.endOffset] are invalid.
     */
    fun addSections(vararg sections: Section) {
        addSections(sections.asList())
    }

    /**
     * add list of sections to the gauge.
     * @throws IllegalArgumentException if [Section.startOffset] or [Section.endOffset] are invalid.
     */
    fun addSections(sections: List<Section>) {
        sections.forEach {
            this.sections.add(it.inGauge(this))
            checkSection(it)
        }
        invalidateGauge()
    }

    /**
     * clear old [sections],
     * and add [numberOfSections] equal to each others.
     */
    fun makeSections(numberOfSections: Int, color: Int) {
        sections.forEach { it.clearGauge() }
        sections.clear()
        var prevPart = 0f
        var part = 1f / numberOfSections
        for (i in 0 until numberOfSections) {
            sections.add(Section(prevPart, part, color, speedometerWidth).inGauge(this))
            prevPart = part
            part += (1f / numberOfSections)
        }
        invalidateGauge()
    }

    /**
     * remove section from this gauge.
     */
    fun removeSection(section: Section?) {
        section?.clearGauge()
        sections.remove(section)
        invalidateGauge()
    }

    /**
     * remove all sections.
     */
    fun clearSections() {
        sections.forEach { it.clearGauge() }
        sections.clear()
        invalidateGauge()
    }

    /**
     * notification that an section has changed.
     */
    override fun update(section: Observable?, isPercentChanged: Any?) {
        invalidateGauge()
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        updatePadding(left, top, right, bottom)
        super.setPadding(padding, padding, padding, padding)
    }

    override fun setPaddingRelative(start: Int, top: Int, end: Int, bottom: Int) {
        updatePadding(start, top, end, bottom)
        super.setPaddingRelative(padding, padding, padding, padding)
    }

    /**
     * @return calculate current section.
     */
    private fun findSection(): Section? {
        sections.forEach {
            if ((maxSpeed - minSpeed) * it.startOffset + minSpeed <= currentSpeed
                && (maxSpeed - minSpeed) * it.endOffset + minSpeed >= currentSpeed
            )
                return it
        }
        return null
    }

    /**
     * @return whether this view attached to Layout or not.
     */
    override fun isAttachedToWindow(): Boolean {
        return attachedToWindow
    }

    /**
     * redraw the Gauge.
     */
    fun invalidateGauge() {
        if (attachedToWindow) {
            updateBackgroundBitmap()
            invalidate()
        }
    }

}