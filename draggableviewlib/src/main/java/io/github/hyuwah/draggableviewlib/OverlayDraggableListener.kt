package io.github.hyuwah.draggableviewlib

import android.view.WindowManager

interface OverlayDraggableListener {
    fun onParamsChanged(updatedParams: WindowManager.LayoutParams)
}