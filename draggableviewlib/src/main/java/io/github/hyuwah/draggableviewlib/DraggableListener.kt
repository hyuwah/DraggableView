package io.github.hyuwah.draggableviewlib

import android.view.View

interface DraggableListener {

    fun onPositionChanged(view: View)

    fun onLongPress(view: View)

}