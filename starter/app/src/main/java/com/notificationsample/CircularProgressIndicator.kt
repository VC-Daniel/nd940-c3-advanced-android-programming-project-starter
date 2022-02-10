package com.notificationsample

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes

/**
 * Indicate progress with a circle that is filled in with a sweeping motion and concurrent
 * changing of the color the circle is drawn with. Define the color to draw via the
 * style attribute LoadingCircle_circleProgressDefaultColor
 */
class CircularProgressIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    /** The bounds of the progress circle */
    private lateinit var boundingOval: RectF

    /** The current progress to display in degrees */
    var downloadProgressDegrees: Int = 0
        set(value) {
            field = value

            // Redraw the circle progress indicator
            invalidate()
        }

    /**
     * Get the name of the downloadProgressDegrees property
     */
    fun getProgressDegreesPropertyName(): String? {
        return ::downloadProgressDegrees.name
    }

    var circleProgressDefaultColor: Int = 0
    var circleProgressAlertColor: Int = 0

    /** The color used to draw the progress circle */
    var circleProgressDrawColor: Int = 0
        set(value) {
            field = value

            // Redraw the circle progress indicator
            invalidate()
        }

    /**
     * Get the name of the circleProgressDrawColor property
     */
    fun getDrawColorPropertyName(): String? {
        return ::circleProgressDrawColor.name
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    init {
        // Get the specified colors to draw the progress circle with
        context.withStyledAttributes(attrs, R.styleable.LoadingCircle) {
            circleProgressDefaultColor = getColor(
                R.styleable.LoadingCircle_circleProgressDefaultColor,
                context.getColor(R.color.cardview_dark_background)
            )
            circleProgressAlertColor = getColor(
                R.styleable.LoadingCircle_circleProgressAlertColor,
                context.getColor(R.color.cardview_dark_background)
            )
        }
    }

    override fun onSizeChanged(width: Int, height: Int, oldw: Int, oldh: Int) {
        boundingOval = RectF(0f, 0f, height.toFloat(), width.toFloat())
    }

    /**
     * Draw the progress circle at the current progress
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = circleProgressDrawColor
        canvas.drawArc(boundingOval, 0F, downloadProgressDegrees.toFloat(), true, paint)
    }
}