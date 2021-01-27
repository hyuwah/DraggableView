package io.github.hyuwah.draggableviewlib

object Draggable {

    @Deprecated(
        "Use DraggableView.Mode",
        ReplaceWith("DraggableView.Mode", "io.github.hyuwah.draggableviewlib.DraggableView.Mode")
    )
    enum class STICKY {
        NONE,
        AXIS_X,
        AXIS_Y,
        AXIS_XY
    }

    const val DRAG_TOLERANCE = 16
    const val DURATION_MILLIS = 250L
}