@file:JvmName("DraggableUtils")

package io.github.hyuwah.draggableviewlib

import android.view.MotionEvent
import android.view.View
import io.github.hyuwah.draggableviewlib.Draggable.DRAG_TOLERANCE
import io.github.hyuwah.draggableviewlib.Draggable.DURATION_MILLIS
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

@JvmOverloads
fun View.makeDraggable(
    stickyAxis: Draggable.STICKY = Draggable.STICKY.NONE,
    animated: Boolean = true,
    draggableListener: DraggableListener? = null
) {
    var widgetInitialX = 0f
    var widgetDX = 0f
    var widgetInitialY = 0f
    var widgetDY = 0f
    setOnTouchListener { v, event ->
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
                    Draggable.STICKY.AXIS_X -> {
                        if (event.rawX >= xMiddle) {
                            if (animated)
                                v.animate().x(xMax.toFloat())
                                    .setDuration(DURATION_MILLIS)
                                    .setUpdateListener { draggableListener?.onPositionChanged(v) }
                                    .start()
                            else
                                v.x = xMax.toFloat()
                        } else {
                            if (animated)
                                v.animate().x(0F).setDuration(DURATION_MILLIS)
                                    .setUpdateListener { draggableListener?.onPositionChanged(v) }
                                    .start()
                            else
                                v.x = 0F
                        }
                    }
                    Draggable.STICKY.AXIS_Y -> {
                        if (event.rawY >= yMiddle) {
                            if (animated)
                                v.animate().y(yMax.toFloat())
                                    .setDuration(DURATION_MILLIS)
                                    .setUpdateListener { draggableListener?.onPositionChanged(v) }
                                    .start()
                            else
                                v.y = yMax.toFloat()
                        } else {
                            if (animated)
                                v.animate().y(0F)
                                    .setDuration(DURATION_MILLIS)
                                    .setUpdateListener { draggableListener?.onPositionChanged(v) }
                                    .start()
                            else {
                                if (animated)
                                    v.animate().y(0F).setDuration(DURATION_MILLIS)
                                        .setUpdateListener { draggableListener?.onPositionChanged(v) }
                                        .start()
                                else
                                    v.y = 0F
                            }
                        }
                    }
                    Draggable.STICKY.AXIS_XY -> {
                        if (event.rawX >= xMiddle) {
                            if (animated)
                                v.animate().x(xMax.toFloat())
                                    .setDuration(DURATION_MILLIS)
                                    .setUpdateListener { draggableListener?.onPositionChanged(v) }
                                    .start()
                            else
                                v.x = xMax.toFloat()
                        } else {
                            if (animated)
                                v.animate().x(0F).setDuration(DURATION_MILLIS)
                                    .setUpdateListener { draggableListener?.onPositionChanged(v) }
                                    .start()
                            v.x = 0F
                        }

                        if (event.rawY >= yMiddle) {
                            if (animated)
                                v.animate().y(yMax.toFloat())
                                    .setDuration(DURATION_MILLIS)
                                    .setUpdateListener { draggableListener?.onPositionChanged(v) }
                                    .start()
                            else
                                v.y = yMax.toFloat()
                        } else {
                            if (animated)
                                v.animate().y(0F).setDuration(DURATION_MILLIS)
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