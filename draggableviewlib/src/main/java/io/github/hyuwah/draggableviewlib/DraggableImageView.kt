package io.github.hyuwah.draggableviewlib

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ImageView
import kotlin.math.abs

/**
 * 29/01/2019
 * muhammad.whydn@gmail.com
 */
class DraggableImageView(context: Context, attrs: AttributeSet) : ImageView(context, attrs) {

    private val STICKY_AXIS_X = 1
    private val STICKY_AXIS_Y = 2
    private val STICKY_AXIS_XY = 3

    private var DEVICE_WIDTH = (context as Activity).windowManager.defaultDisplay.width
    private var DEVICE_HEIGHT = (context as Activity).windowManager.defaultDisplay.height

    private var stickyAxis: Int
    private var mAnimate: Boolean
    private var mMarginTop: Int

    private var widgetXFirst: Float = 0F
    private var widgetDX: Float = 0F
    private var widgetYFirst: Float = 0F
    private var widgetDY: Float = 0F
    private var widgetLastAction: Int = 0

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.DraggableImageView, 0, 0).apply {
            try {
                stickyAxis = getInteger(R.styleable.DraggableImageView_sticky, 0)
                mAnimate = getBoolean(R.styleable.DraggableImageView_animate, false)
                mMarginTop = getDimensionPixelSize(R.styleable.DraggableImageView_marginTop, 0)
            } finally {
                recycle()
            }
        }

        draggableSetup()
    }

    /**
     * Draggable Touch Listener
     */
    fun draggableSetup() {
        this.setOnTouchListener { v, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    this.widgetDX = v.x - event.rawX
                    this.widgetDY = v.y - event.rawY
                    this.widgetXFirst = v.x
                    this.widgetYFirst = v.y
                    this.widgetLastAction = MotionEvent.ACTION_DOWN
                }
                MotionEvent.ACTION_MOVE -> {

                    // Screen border Collision
                    if (event.rawX >= v.width / 2 && event.rawX <= (DEVICE_WIDTH - v.width / 2))
                        v.x = event.rawX + this.widgetDX

                    if (event.rawY >= (mMarginTop.toFloat()) && event.rawY <= (DEVICE_HEIGHT - v.height / 2))
                        v.y = event.rawY + this.widgetDY

                    this.widgetLastAction = MotionEvent.ACTION_MOVE
                }
                MotionEvent.ACTION_UP -> {

                    when (this.stickyAxis) {
                        STICKY_AXIS_X -> {
                            if (event.rawX >= DEVICE_WIDTH / 2) {
                                if (this.mAnimate)
                                    v.animate().x((DEVICE_WIDTH) - (v.width).toFloat()).setDuration(250).start()
                                else
                                    v.x = (DEVICE_WIDTH) - (v.width).toFloat()
                            } else {
                                if (this.mAnimate)
                                    v.animate().x(0F).setDuration(250).start()
                                else
                                    v.x = 0F
                            }
                        }
                        STICKY_AXIS_Y -> {
                            if (event.rawY >= DEVICE_HEIGHT / 2) {
                                if (this.mAnimate)
                                    v.animate().y((DEVICE_HEIGHT) - (v.height * 2).toFloat()).setDuration(250).start()
                                else
                                    v.y = (DEVICE_HEIGHT) - (v.height * 2).toFloat()
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
                            if (event.rawX >= DEVICE_WIDTH / 2) {
                                if (this.mAnimate)
                                    v.animate().x((DEVICE_WIDTH) - (v.width).toFloat()).setDuration(250).start()
                                else
                                    v.x = (DEVICE_WIDTH) - (v.width).toFloat()
                            } else
                                v.x = 0F
                            if (event.rawY >= DEVICE_HEIGHT / 2) {
                                if (this.mAnimate)
                                    v.animate().y((DEVICE_HEIGHT) - (v.height * 2).toFloat()).setDuration(250).start()
                                else
                                    v.y = (DEVICE_HEIGHT) - (v.height * 2).toFloat()
                            } else {
                                if (this.mAnimate)
                                    v.animate().y(0F).setDuration(250).start()
                                else
                                    v.y = 0F
                            }
                        }
                    }

                    // Will register as clicked if not moved for 16px in both X & Y
                    if (abs(v.x - widgetXFirst) <= 16 && abs(v.y - widgetYFirst) <= 16) {
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


    fun setStickyAxis(axis: Int) {
        when (axis) {
            STICKY_AXIS_X, STICKY_AXIS_Y, STICKY_AXIS_XY -> {
                this.stickyAxis = axis
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