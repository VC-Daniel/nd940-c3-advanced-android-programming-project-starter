package com.notificationsample

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

/** A button that is animated when a download is ongoing */
class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    /** The current progress to display from 0.0 to 1.0 */
    var downloadProgress: Float = 0f
        set(value) {
            field = value

            // Redraw the loading bar
            invalidate()
        }

    /**
     * Get the name of the downloadProgress property
     */
    fun getProgressPropertyName(): String {
        return ::downloadProgress.name
    }


    /** The color of the background of the button which is displayed when no download is ongoing. */
    private var defaultColor: Int = 0

    /** The color to draw a loading indicator on top of the backgrounf when a download is ongoing */
    private var loadingColor: Int = 0
    private var textColor: Int = 0

    /** The text to display to start a download */
    private var startText: String = context.getString(R.string.start_downloading)

    /** The text to display when a download is in progress */
    private var inProgressText: String = context.getString(R.string.downloading)

    /** the currently displayed text */
    private var buttonText: String = startText

    private var widthSize = 0
    private var heightSize = 0
    private var xPos = 0f
    private var yPos = 0f

    private val paint = Paint().apply {
        isAntiAlias = true
        strokeWidth = resources.getDimension(R.dimen.stroke_width)
        textSize = resources.getDimension(R.dimen.text_size)
    }

    /** The current state of the download */
    var downloadState: DownloadState by Delegates.observable(DownloadState.Completed) { _, _, new ->
        if (downloadState != DownloadState.Loading) {

            // Indicate the current state of the download by setting the appropriate text
            if (new == DownloadState.Starting) {
                buttonText = inProgressText
                downloadState = DownloadState.Loading
            } else {
                buttonText = startText
            }
            this.invalidate()
        }
    }

    init {
        // Retrieve the custom attributes to style the button as specified
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            textColor =
                getColor(R.styleable.LoadingButton_textColor, context.getColor(R.color.white))
            startText = getString(R.styleable.LoadingButton_startText).toString()
            inProgressText = getString(R.styleable.LoadingButton_inProgressText).toString()
            defaultColor = getColor(
                R.styleable.LoadingButton_defaultColor,
                Color.GREEN
            )
            loadingColor = getColor(
                R.styleable.LoadingButton_loadingColor,
                Color.CYAN
            )
            downloadProgress = getFloat(R.styleable.LoadingButton_downloadProgress, 0f)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        /** Draw the button background and, text and if a download is in progress the
         * loading indicator
         */
        drawBackground(canvas)
        drawLoadingRect(canvas)
        drawText(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)

        xPos = widthSize / 2f
        yPos = (heightSize / 2 - (paint.descent() + paint.ascent()) / 2)
    }

    /**
     * Draw the appropriate text that corresponds to the current [downloadState]
     * The text centering logic is based on the logic at
     * https://stackoverflow.com/questions/11120392/android-center-text-on-canvas
     */
    private fun drawText(canvas: Canvas) {
        canvas.save()
        paint.color = textColor
        paint.textAlign = Paint.Align.CENTER

        canvas.drawText(
            buttonText,
            xPos, yPos, paint
        )
        canvas.restore()
    }

    /** Draw the background of the button which is partially drawn over when a download
     * is in progress
     */
    private fun drawBackground(canvas: Canvas) {
        canvas.save()
        canvas.drawColor(defaultColor)
        canvas.restore()
    }

    /**
     * Draw an indicator to denote the current [downloadProgress]
     */
    private fun drawLoadingRect(canvas: Canvas) {
        canvas.save()
        paint.color = loadingColor
        canvas.drawRect(0f, 0f, (widthSize * downloadProgress), widthSize.toFloat(), paint)
        canvas.restore()
    }
}