package com.android.template.utils.custom

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.ObservableLong
import com.android.template.utils.helpers.SECOND
import java.util.*


class ClockView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private val timerPeriod = 200L

    private val duration = ObservableLong()
    private val startPoint = ObservableLong()

    var updateTextCallback: ((Int) -> String)? = null

    var completeCallback: (() -> Unit)? = null

    var timer: Timer? = null

    fun init(duration: Long) {
        this.duration.set(duration)
        this.startPoint.set(System.currentTimeMillis())
        start()
    }

    private fun start() {
        if (timer == null) {
            timer = Timer()
            val statusBarTask = object : TimerTask() {
                override fun run() {
                    post {
                        updateText()
                    }
                    if (System.currentTimeMillis() - startPoint.get() > duration.get()) {
                        stop()
                        post {
                            completeCallback?.invoke()
                        }
                    }
                }
            }
            timer?.scheduleAtFixedRate(statusBarTask, 0, timerPeriod)
        } else {
            stop()
            start()
        }
    }

    private fun updateText() =
        updateTextCallback?.invoke(((duration.get() - (System.currentTimeMillis() - startPoint.get())) / SECOND).toInt())
            ?.let {
                text = it
            }

    private fun stop() {
        timer?.cancel()
        timer = null
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stop()
    }

}