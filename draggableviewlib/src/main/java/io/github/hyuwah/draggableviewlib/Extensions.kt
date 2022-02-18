@file:JvmName("DraggableUtils")

package io.github.hyuwah.draggableviewlib

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.PixelFormat
import android.os.Build
import android.util.Log
import android.view.*
import io.github.hyuwah.draggableviewlib.Draggable.DRAG_TOLERANCE
import io.github.hyuwah.draggableviewlib.Draggable.DURATION_MILLIS
//import io.github.hyuwah.draggableviewlib.DraggableView.StickyRestSide
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

internal fun View.marginStart(): Float {
    return ((layoutParams as? ViewGroup.MarginLayoutParams)?.marginStart ?: 0).toFloat()
}

internal fun View.marginEnd(): Float {
    return ((layoutParams as? ViewGroup.MarginLayoutParams)?.marginEnd ?: 0).toFloat()
}

internal fun View.marginTop(): Float {
    return ((layoutParams as? ViewGroup.MarginLayoutParams)?.topMargin ?: 0).toFloat()
}

internal fun View.marginBottom(): Float {
    return ((layoutParams as? ViewGroup.MarginLayoutParams)?.bottomMargin ?: 0).toFloat()
}

@JvmOverloads
internal fun View.setupDraggable(
//    minimizeBtnListener: DraggableView.Listener,
    stickyAxis: DraggableView.Mode = DraggableView.Mode.NON_STICKY,
    animated: Boolean = true,
    draggableListener: DraggableListener? = null,
) {
    var widgetInitialX = 0f
    var widgetDX = 0f
    var widgetInitialY = 0f
    var widgetDY = 0f

    val marginStart = marginStart()
    val marginTop = marginTop()
    val marginEnd = marginEnd()
    val marginBottom = marginBottom()


    fun longClickSetup(v:View) : GestureDetector {
        return GestureDetector(this.context,object : GestureDetector.SimpleOnGestureListener(){
            override fun onLongPress(e: MotionEvent?) {
                draggableListener?.onLongPress(v)
            }
        })
    }

    setOnTouchListener { v, event ->
        val viewParent = v.parent as View
        val parentHeight = viewParent.height
        val parentWidth = viewParent.width
        val xMax = parentWidth - v.width - marginEnd
        val xMiddle = parentWidth / 2
        val yMax = parentHeight - v.height - marginBottom
        val yMiddle = parentHeight / 2

        longClickSetup(v).onTouchEvent(event)

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                widgetDX = v.x - event.rawX
                widgetDY = v.y - event.rawY
                widgetInitialX = v.x
                widgetInitialY = v.y
            }
            MotionEvent.ACTION_MOVE -> {
                var newX = event.rawX + widgetDX
                newX = max(marginStart, newX)
                newX = min(xMax, newX)
                v.x = newX

                var newY = event.rawY + widgetDY
                newY = max(marginTop, newY)
                newY = min(yMax, newY)
                v.y = newY

                draggableListener?.onPositionChanged(v)
//                minimizeBtnListener.onPositionChanged(v, StickyRestSide.HIDE)
            }
            MotionEvent.ACTION_UP -> {
                when (stickyAxis) {
                    DraggableView.Mode.STICKY_X -> {
                        if (event.rawX >= xMiddle) {
                            if (animated)
                                v.animate().x(xMax)
                                    .setDuration(DURATION_MILLIS)
                                    .setUpdateListener {
                                        draggableListener?.onPositionChanged(v)
//                                        minimizeBtnListener.onPositionChanged(v, StickyRestSide.RIGHT)
                                    }
                                    .setListener(object : AnimatorListenerAdapter() {
                                        override fun onAnimationEnd(animation: Animator?) {
                                            super.onAnimationEnd(animation)
                                            Log.d("drg", "Animate END Sticky X RIGHT")
                                        }
                                    })
                                    .start()
                            else {
                                v.x = xMax
//                                minimizeBtnListener.onPositionChanged(v, StickyRestSide.RIGHT)
                            }
                        } else {
                            if (animated)
                                v.animate().x(marginStart).setDuration(DURATION_MILLIS)
                                    .setUpdateListener {
                                        draggableListener?.onPositionChanged(v)
//                                        minimizeBtnListener.onPositionChanged(v, StickyRestSide.LEFT)
                                    }
                                    .start()
                            else {
                                v.x = marginStart
//                                minimizeBtnListener.onPositionChanged(v, StickyRestSide.LEFT)
                            }
                        }
                    }
                    DraggableView.Mode.STICKY_Y -> {
                        if (event.rawY >= yMiddle) {
                            if (animated)
                                v.animate().y(yMax)
                                    .setDuration(DURATION_MILLIS)
                                    .setUpdateListener {
                                        draggableListener?.onPositionChanged(v)
//                                        minimizeBtnListener.onPositionChanged(v, StickyRestSide.BOTTOM)
                                    }
                                    .start()
                            else
                                v.y = yMax
                        } else {
                            if (animated)
                                v.animate().y(marginTop)
                                    .setDuration(DURATION_MILLIS)
                                    .setUpdateListener {
                                        draggableListener?.onPositionChanged(v)
                                    }
                                    .start()
                            else
                                v.y = marginTop
                        }
                    }
                    DraggableView.Mode.STICKY_XY -> {
                        if (event.rawX >= xMiddle) {
                            if (animated)
                                v.animate().x(xMax)
                                    .setDuration(DURATION_MILLIS)
                                    .setUpdateListener {
                                        draggableListener?.onPositionChanged(v)
                                    }
                                    .start()
                            else
                                v.x = xMax
                        } else {
                            if (animated)
                                v.animate().x(marginStart).setDuration(DURATION_MILLIS)
                                    .setUpdateListener {
                                        draggableListener?.onPositionChanged(v)
                                    }
                                    .start()
                            v.x = marginStart
                        }

                        if (event.rawY >= yMiddle) {
                            if (animated)
                                v.animate().y(yMax)
                                    .setDuration(DURATION_MILLIS)
                                    .setUpdateListener {
                                        draggableListener?.onPositionChanged(v)
                                    }
                                    .start()
                            else
                                v.y = yMax
                        } else {
                            if (animated)
                                v.animate().y(marginTop).setDuration(DURATION_MILLIS)
                                    .setUpdateListener {
                                        draggableListener?.onPositionChanged(v)
                                    }
                                    .start()
                            else
                                v.y = marginTop
                        }
                    }
                    else -> {
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

@Deprecated(
    "Use / Setup with DraggableView class or setupDraggable() builder instead",
    ReplaceWith(
        "DraggableView.Builder(view).build()",
        "io.github.hyuwah.draggableviewlib.DraggableView",
    )
)
@JvmOverloads
fun View.makeDraggable(
    stickyAxis: Draggable.STICKY = Draggable.STICKY.NONE,
    animated: Boolean = true,
    draggableListener: DraggableListener? = null
) {
    val axisMode = when (stickyAxis) {
        Draggable.STICKY.NONE -> DraggableView.Mode.NON_STICKY
        Draggable.STICKY.AXIS_X -> DraggableView.Mode.STICKY_X
        Draggable.STICKY.AXIS_Y -> DraggableView.Mode.STICKY_Y
        Draggable.STICKY.AXIS_XY -> DraggableView.Mode.STICKY_XY
    }
    DraggableView.Builder(this)
        .setStickyMode(axisMode)
        .setAnimated(animated)
        .setListener(draggableListener)
        .build()
}

/**
 * Shortcut / extension function way for DraggableView.Builder
 * essentially the same
 */
fun <T : View> T.setupDraggable(): DraggableView.Builder<T> {
    return DraggableView.Builder(this)
}

/**
 * Make floating draggable overlay view (on top of other application).
 * You still have to manually manage adding, updating & removing the view via Window Manager from where
 * you call this function
 * @param listener callback for new LayoutParams, do `windowManager.updateViewLayout()` here
 * @param layoutParams if you need to customize the layout params (e.g. Gravity),
 * note that you still have to use correct Layout Flag, can be omitted for default value
 * @return layoutParams to be used when adding the view on window manager (outside this function)
 */
@JvmOverloads
fun View.makeOverlayDraggable(
    listener: OverlayDraggableListener,
    layoutParams: WindowManager.LayoutParams? = null
): WindowManager.LayoutParams {
    var widgetInitialX = 0
    var widgetDX = 0f
    var widgetInitialY = 0
    var widgetDY = 0f

    val layoutFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
    } else {
        WindowManager.LayoutParams.TYPE_PHONE
    }
    val params = layoutParams
        ?: WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutFlag,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

    setOnTouchListener { _, event ->
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                widgetInitialX = params.x
                widgetInitialY = params.y
                widgetDX = widgetInitialX - event.rawX
                widgetDY = widgetInitialY - event.rawY
                return@setOnTouchListener true
            }
            MotionEvent.ACTION_MOVE -> {
                val newX = event.rawX + widgetDX
                val newY = event.rawY + widgetDY
                params.x = newX.roundToInt()
                params.y = newY.roundToInt()
                listener.onParamsChanged(params)
                return@setOnTouchListener true
            }
            MotionEvent.ACTION_UP -> {
                if (abs(params.x - widgetInitialX) <= DRAG_TOLERANCE && abs(params.y - widgetInitialY) <= DRAG_TOLERANCE) {
                    performClick()
                }
                return@setOnTouchListener true
            }
            else -> return@setOnTouchListener false
        }
    }

    return params
}