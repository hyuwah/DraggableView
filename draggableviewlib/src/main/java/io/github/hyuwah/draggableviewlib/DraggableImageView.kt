package io.github.hyuwah.draggableviewlib

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import io.github.hyuwah.draggableviewlib.Constants.DRAG_TOLERANCE
import kotlin.math.abs

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

    // Coordinates
    private var widgetXFirst: Float = 0F
    private var widgetDX: Float = 0F
    private var widgetYFirst: Float = 0F
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
            val viewParent: View = (v.parent as View)
            val PARENT_HEIGHT = viewParent.height
            val PARENT_WIDTH = viewParent.width

            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    this.widgetDX = v.x - event.rawX
                    this.widgetDY = v.y - event.rawY
                    this.widgetXFirst = v.x
                    this.widgetYFirst = v.y
                }
                MotionEvent.ACTION_MOVE -> {
                    // Screen border Collision
                    var newX = event.rawX + this.widgetDX
                    newX = Math.max(0F, newX)
                    newX = Math.min((PARENT_WIDTH - v.width).toFloat(), newX)
                    v.x = newX

                    var newY = event.rawY + this.widgetDY
                    newY = Math.max(0F, newY)
                    newY = Math.min((PARENT_HEIGHT - v.height).toFloat(), newY)
                    v.y = newY
                }
                MotionEvent.ACTION_UP -> {
                    // If Sticky
                    when (this.stickyAxis) {
                        STICKY_AXIS_X -> {
                            if (event.rawX >= PARENT_WIDTH / 2) {
                                if (this.mAnimate)
                                    v.animate().x((PARENT_WIDTH) - (v.width).toFloat()).setDuration(250).start()
                                else
                                    v.x = (PARENT_WIDTH) - (v.width).toFloat()
                            } else {
                                if (this.mAnimate)
                                    v.animate().x(0F).setDuration(250).start()
                                else
                                    v.x = 0F
                            }
                        }
                        STICKY_AXIS_Y -> {
                            if (event.rawY >= PARENT_HEIGHT / 2) {
                                if (this.mAnimate)
                                    v.animate().y((PARENT_HEIGHT) - (v.height).toFloat()).setDuration(250).start()
                                else
                                    v.y = (PARENT_HEIGHT) - (v.height).toFloat()
                            } else {
                                if (this.mAnimate)
                                    v.animate().y(0F).setDuration(250).start()
                                else {
                                    if (this.mAnimate)
                                        v.animate().y(0F).setDuration(250).start()
                                    else
                                        v.y = 0F
                                }
                            }
                        }
                        STICKY_AXIS_XY -> {
                            if (event.rawX >= PARENT_WIDTH / 2) {
                                if (this.mAnimate)
                                    v.animate().x((PARENT_WIDTH) - (v.width).toFloat()).setDuration(250).start()
                                else
                                    v.x = (PARENT_WIDTH) - (v.width).toFloat()
                            } else {
                                if (this.mAnimate)
                                    v.animate().x(0F).setDuration(250).start()
                                v.x = 0F
                            }

                            if (event.rawY >= PARENT_HEIGHT / 2) {
                                if (this.mAnimate)
                                    v.animate().y((PARENT_HEIGHT) - (v.height).toFloat()).setDuration(250).start()
                                else
                                    v.y = (PARENT_HEIGHT) - (v.height).toFloat()
                            } else {
                                if (this.mAnimate)
                                    v.animate().y(0F).setDuration(250).start()
                                else
                                    v.y = 0F
                            }
                        }
                    }

                    // Will register as clicked if not moved for 16px in both X & Y
                    if (abs(v.x - widgetXFirst) <= DRAG_TOLERANCE && abs(v.y - widgetYFirst) <= DRAG_TOLERANCE) {
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
}