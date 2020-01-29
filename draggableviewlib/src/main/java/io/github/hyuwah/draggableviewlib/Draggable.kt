package io.github.hyuwah.draggableviewlib

object Draggable {

    enum class STICKY {
        NONE,
        AXIS_X,
        AXIS_Y,
        AXIS_XY
    }

    const val DRAG_TOLERANCE = 16
    const val DURATION_MILLIS = 250L
}