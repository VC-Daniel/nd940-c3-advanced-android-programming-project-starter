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
    private var downloadProgressDegrees: Int = 0

    /**
     * Set the current progress in degrees and then display this change. An explicit setter is used to
     * facilitate redrawing the view based on the documentation at
     * https://developer.android.com/training/custom-views/create-view
     */
    fun setDownloadProgressDegrees(value: Int) {
        downloadProgressDegrees = value
        invalidate()
    }

    /** The color used to draw the progress circle */
    private var circleProgressDefaultColor: Int = 0

    /** Set the color used to draw the progress circle and redraw the progress circle*/
    fun setCircleProgressDefaultColor(value: Int) {
        circleProgressDefaultColor = value
        invalidate()
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    init {
        // Get the specified color to draw the progress circle with
        context.withStyledAttributes(attrs, R.styleable.LoadingCircle) {
            circleProgressDefaultColor = getColor(
                R.styleable.LoadingCircle_circleProgressDefaultColor,
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
        paint.color = circleProgressDefaultColor
        canvas.drawArc(boundingOval, 0F, downloadProgressDegrees.toFloat(), true, paint)
    }
}