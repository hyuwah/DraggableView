package io.github.hyuwah.draggableview

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import io.github.hyuwah.draggableview.overlay.OverlayService
import kotlinx.android.synthetic.main.activity_draggable_overlay.*

class OverlayDraggableActivity : AppCompatActivity() {

    private var isOverlayOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Overlay Draggable Activity"
        setContentView(R.layout.activity_draggable_overlay)
        btn_show_overlay.setOnClickListener {
            toggleOverlay()
        }
    }

    private fun toggleOverlay() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
            !Settings.canDrawOverlays(this)
        ) {
            // Get permission first on Android M & above
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, 1234)
        } else {
            val i = Intent(this, OverlayService::class.java)
            if (!isOverlayOn) {
                // Show
                startService(i)
            } else {
                // Hide
                stopService(i)
            }
            isOverlayOn = !isOverlayOn
        }
    }


}
