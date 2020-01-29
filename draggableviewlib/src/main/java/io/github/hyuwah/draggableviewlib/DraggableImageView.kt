package io.github.hyuwah.draggableviewlib

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import io.github.hyuwah.draggableviewlib.Draggable.DRAG_TOLERANCE
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * 29/01/2019
 * muhammad.whydn@gmail.com
 */
class DraggableImageView(context: Context, attrs: AttributeSet) : ImageView(context, attrs) {

    companion object {
        const val NON_STICKY = 0
        const val STICKY_AXIS_X = 1
        const val STICKY_AXIS_Y = 2
        const val STICKY_AXIS_XY = 3
    }

    // Attributes
    private var stickyAxis: Int
    private var mAnimate: Boolean

    private var draggableListener: DraggableListener? = null

    // Coordinates
    private var widgetInitialX: Float = 0F
    private var widgetDX: Float = 0F
    private var widgetInitialY: Float = 0F
    private var widgetDY: Float = 0F

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.DraggableImageView, 0, 0).apply {
            try {
                stickyAxis = getInteger(R.styleable.DraggableImageView_sticky, 0)
                mAnimate = getBoolean(R.styleable.DraggableImageView_animate, false)
            } finally {
                recycle()
            }
        }

        draggableSetup()
    }

    /**
     * Draggable Touch Setup
     */
    private fun draggableSetup() {
        this.setOnTouchListener { v, event ->
            val viewParent = v.parent as View
            val parentHeight = viewParent.height
            val parentWidth = viewParent.width
            val xMax = parentWidth - v.width
            val xMiddle = parentWidth / 2
            val yMax = parentHeight - v.height
            val yMiddle = parentHeight / 2

            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    widgetDX = v.x - event.rawX
                    widgetDY = v.y - event.rawY
                    widgetInitialX = v.x
                    widgetInitialY = v.y
                }
                MotionEvent.ACTION_MOVE -> {
                    var newX = event.rawX + widgetDX
                    newX = max(0F, newX)
                    newX = min(xMax.toFloat(), newX)
                    v.x = newX

                    var newY = event.rawY + widgetDY
                    newY = max(0F, newY)
                    newY = min(yMax.toFloat(), newY)
                    v.y = newY

                    draggableListener?.onPositionChanged(v)
                }
                MotionEvent.ACTION_UP -> {
                    when (stickyAxis) {
                        STICKY_AXIS_X -> {
                            if (event.rawX >= xMiddle) {
                                if (mAnimate)
                                    v.animate().x(xMax.toFloat())
                                        .setDuration(Draggable.DURATION_MILLIS)
                                        .setUpdateListener { draggableListener?.onPositionChanged(v) }
                                        .start()
                                else
                                    v.x = xMax.toFloat()
                            } else {
                                if (mAnimate)
                                    v.animate().x(0F).setDuration(Draggable.DURATION_MILLIS)
                                        .setUpdateListener { draggableListener?.onPositionChanged(v) }
                                        .start()
                                else
                                    v.x = 0F
                            }
                        }
                        STICKY_AXIS_Y -> {
                            if (event.rawY >= yMiddle) {
                                if (mAnimate)
                                    v.animate().y(yMax.toFloat())
                                        .setDuration(Draggable.DURATION_MILLIS)
                                        .setUpdateListener { draggableListener?.onPositionChanged(v) }
                                        .start()
                                else
                                    v.y = yMax.toFloat()
                            } else {
                                if (mAnimate)
                                    v.animate().y(0F)
                                        .setDuration(Draggable.DURATION_MILLIS)
                                        .setUpdateListener { draggableListener?.onPositionChanged(v) }
                                        .start()
                                else {
                                    if (mAnimate)
                                        v.animate().y(0F).setDuration(Draggable.DURATION_MILLIS)
                                            .setUpdateListener {
                                                draggableListener?.onPositionChanged(
                                                    v
                                                )
                                            }
                                            .start()
                                    else
                                        v.y = 0F
                                }
                            }
                        }
                        STICKY_AXIS_XY -> {
                            if (event.rawX >= xMiddle) {
                                if (mAnimate)
                                    v.animate().x(xMax.toFloat())
                                        .setDuration(Draggable.DURATION_MILLIS)
                                        .setUpdateListener { draggableListener?.onPositionChanged(v) }
                                        .start()
                                else
                                    v.x = xMax.toFloat()
                            } else {
                                if (mAnimate)
                                    v.animate().x(0F).setDuration(Draggable.DURATION_MILLIS)
                                        .setUpdateListener { draggableListener?.onPositionChanged(v) }
                                        .start()
                                v.x = 0F
                            }

                            if (event.rawY >= yMiddle) {
                                if (mAnimate)
                                    v.animate().y(yMax.toFloat())
                                        .setDuration(Draggable.DURATION_MILLIS)
                                        .setUpdateListener { draggableListener?.onPositionChanged(v) }
                                        .start()
                                else
                                    v.y = yMax.toFloat()
                            } else {
                                if (mAnimate)
                                    v.animate().y(0F).setDuration(Draggable.DURATION_MILLIS)
                                        .setUpdateListener { draggableListener?.onPositionChanged(v) }
                                        .start()
                                else
                                    v.y = 0F
                            }
                        }
                    }

                    if (abs(v.x - widgetInitialX) <= DRAG_TOLERANCE && abs(v.y - widgetInitialY) <= DRAG_TOLERANCE) {
                        performClick()
                    }
                }
                else -> return@setOnTouchListener false
            }
            true
        }
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    /***
     * ATTRIBUTES setter / getter
     */

    fun setStickyAxis(axis: Int) {
        when (axis) {
            NON_STICKY, STICKY_AXIS_X, STICKY_AXIS_Y, STICKY_AXIS_XY -> {
                this.stickyAxis = axis
                invalidate()
                requestLayout()
            }
        }
    }

    fun isAnimate(): Boolean {
        return mAnimate
    }

    fun setAnimate(animate: Boolean) {
        mAnimate = animate
        invalidate()
        requestLayout()
    }

    fun setListener(draggableListener: DraggableListener?) {
        this.draggableListener = draggableListener
    }
}