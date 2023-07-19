package io.github.hyuwah.draggableviewlib

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator

class DraggableView<T : View> private constructor(
    targetView: T,
    sticky: Mode,
    animated: Boolean,
    listener: DraggableListener?
) {

    private var targetView: T = targetView
    var sticky: Mode = Mode.NON_STICKY
        set(value) {
            field = value
            enableDrag()
        }
    var animated: Boolean = true
        set(value) {
            field = value
            enableDrag()
        }
    var listener: DraggableListener? = null
        set(value) {
            field = value
            enableDrag()
        }
    var isMinimized: Boolean = false
        private set

    init {
        this.sticky = sticky
        this.animated = animated
        this.listener = listener
        enableDrag()
    }

    fun getView(): T = targetView

    fun setViewPosition(x: Float, y: Float) {
        targetView.x = x
        targetView.y = y
    }

    /**
     *
     */
    fun disableDrag() {
        targetView.setOnTouchListener(null)
    }

    /**
     *
     */
    fun enableDrag() {
        targetView.setupDraggable(sticky, animated, listener)
    }

    /**
     *
     */
    fun show(durationMs: Int = 300) {
        with(targetView) {
            if (visibility != View.VISIBLE) {
                visibility = View.VISIBLE
                animate().scaleY(1f).scaleX(1f)
                    .setDuration(durationMs.toLong())
                    .setInterpolator(DecelerateInterpolator())
                    .setListener(null)
                    .start()
            }
        }
    }

    /**
     *
     */
    fun hide(durationMs: Int = 300) {
        with(targetView) {
            if (visibility != View.GONE) {
                if (animation?.hasEnded() != false) {
                    animate().scaleY(0f).scaleX(0f)
                        .setInterpolator(AccelerateInterpolator())
                        .setDuration(durationMs.toLong())
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                super.onAnimationEnd(animation)
                                visibility = View.GONE
                            }
                        })
                        .start()
                }
            }
        }
    }

    /**
     *
     */
    fun undock() {
        if (!isMinimized) return
        with(targetView) {
            val parentWidth = (parent as View).width.toFloat()
            when (sticky) {
                Mode.STICKY_X -> {
                    when {
                        x < marginStart() -> {
                            animate().translationX(0f).start()
//                            minimizeButton?.animate()?.rotationBy(180f)
                        }
                        x > parentWidth - width - marginEnd() ->
                            animate().translationXBy(-(width / 2f) - marginEnd()).start()
                    }
                    enableDrag()
                    isMinimized = false
                }
                Mode.STICKY_Y -> {
                    // TODO
                }
                else -> return
            }
        }
    }

    /**
     *
     */
    fun dockToEdge() {
        if (isMinimized) return
        with(targetView) {
            val parentWidth = (parent as View).width.toFloat()
            when (sticky) {
                Mode.STICKY_X -> {
                    when (x) {
                        marginStart() -> {
                            val targetDockedX = -((width.toFloat()) / 2 + marginStart())
                            animate().translationXBy(targetDockedX).start()
//                            minimizeButton?.animate()?.rotationBy(-180f)
                        }
                        parentWidth - width - marginEnd() -> {
                            val targetDockedX = ((width.toFloat() / 2) + marginEnd())
                            animate().translationXBy(targetDockedX).start()
                        }
                    }
                    disableDrag()
                    isMinimized = true
                }
                Mode.STICKY_Y -> {
                    // TODO
                }
                else -> return
            }
        }
    }

    /**
     * Initialize draggable view
     * - sticky mode set to Draggable.STICKY.NONE by default
     * - animated set to true by default
     *
     * @param targetView target view
     */
    class Builder<VIEW : View>(private var targetView: VIEW) {
        private var stickyMode: Mode = Mode.NON_STICKY
        private var animated: Boolean = true
        private var listener: DraggableListener? = null

        fun setStickyMode(mode: Mode) = apply { this.stickyMode = mode }
        fun setAnimated(value: Boolean) = apply { this.animated = value }
        fun setListener(listener: DraggableListener?) = apply {
            this.listener = listener
            if (listener == null) {
                targetView.setOnLongClickListener(null)
            } else {
                targetView.setOnLongClickListener {
                    listener.onLongPress(it)
                    true
                }
            }
        }
        fun build() = DraggableView(targetView, stickyMode, animated, listener)
    }

    enum class Mode {
        NON_STICKY,
        STICKY_X,
        STICKY_Y,
        STICKY_XY
    }

//    internal enum class StickyRestSide {
//        INITIAL, HIDE, LEFT, TOP, RIGHT, BOTTOM,
//    }
}