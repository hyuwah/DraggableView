package io.github.hyuwah.draggableviewlib

import android.view.MotionEvent
import android.view.View
import io.github.hyuwah.draggableviewlib.Constants.DRAG_TOLERANCE
import java.lang.Math.max
import kotlin.math.abs
import kotlin.math.min

fun View.makeDraggable(
    stickyAxis: Constants.STICKY = Constants.STICKY.NONE,
    animated: Boolean = true
) {
    var widgetInitialX = 0f
    var widgetDX = 0f
    var widgetInitialY = 0f
    var widgetDY = 0f
    setOnTouchListener { v, event ->
        val viewParent = v.parent as View
        val PARENT_HEIGHT = viewParent.height
        val PARENT_WIDTH = viewParent.width

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
                newX = min((PARENT_WIDTH - v.width).toFloat(), newX)
                v.x = newX

                var newY = event.rawY + widgetDY
                newY = max(0F, newY)
                newY = min((PARENT_HEIGHT - v.height).toFloat(), newY)
                v.y = newY
            }
            MotionEvent.ACTION_UP -> {
                when (stickyAxis) {
                    Constants.STICKY.AXIS_X -> {
                        if (event.rawX >= PARENT_WIDTH / 2) {
                            if (animated)
                                v.animate().x((PARENT_WIDTH) - (v.width).toFloat()).setDuration(250).start()
                            else
                                v.x = (PARENT_WIDTH) - (v.width).toFloat()
                        } else {
                            if (animated)
                                v.animate().x(0F).setDuration(250).start()
                            else
                                v.x = 0F
                        }
                    }
                    Constants.STICKY.AXIS_Y -> {
                        if (event.rawY >= PARENT_HEIGHT / 2) {
                            if (animated)
                                v.animate().y((PARENT_HEIGHT) - (v.height).toFloat()).setDuration(
                                    250
                                ).start()
                            else
                                v.y = (PARENT_HEIGHT) - (v.height).toFloat()
                        } else {
                            if (animated)
                                v.animate().y(0F).setDuration(250).start()
                            else {
                                if (animated)
                                    v.animate().y(0F).setDuration(250).start()
                                else
                                    v.y = 0F
                            }
                        }
                    }
                    Constants.STICKY.AXIS_XY -> {
                        if (event.rawX >= PARENT_WIDTH / 2) {
                            if (animated)
                                v.animate().x((PARENT_WIDTH) - (v.width).toFloat()).setDuration(250).start()
                            else
                                v.x = (PARENT_WIDTH) - (v.width).toFloat()
                        } else {
                            if (animated)
                                v.animate().x(0F).setDuration(250).start()
                            v.x = 0F
                        }

                        if (event.rawY >= PARENT_HEIGHT / 2) {
                            if (animated)
                                v.animate().y((PARENT_HEIGHT) - (v.height).toFloat()).setDuration(
                                    250
                                ).start()
                            else
                                v.y = (PARENT_HEIGHT) - (v.height).toFloat()
                        } else {
                            if (animated)
                                v.animate().y(0F).setDuration(250).start()
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