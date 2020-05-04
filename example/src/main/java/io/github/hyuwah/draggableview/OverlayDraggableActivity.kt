package io.github.hyuwah.draggableview

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import io.github.hyuwah.draggableviewlib.OverlayDraggableListener
import io.github.hyuwah.draggableviewlib.makeOverlayDraggable
import kotlinx.android.synthetic.main.activity_draggable_overlay.*

class OverlayDraggableActivity : AppCompatActivity(), SensorEventListener,
    OverlayDraggableListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var luxSensor: Sensor
    private lateinit var overlayView: TextView
    private var isOverlayOn = false
    private var luxValue = 0f
    private var params: WindowManager.LayoutParams? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Overlay Draggable Activity"
        setContentView(R.layout.activity_draggable_overlay)
        setupSensor()
        initOverlayView()
        btn_show_overlay.setOnClickListener {
            toggleOverlay()
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, luxSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isOverlayOn) {
            windowManager.removeViewImmediate(overlayView)
            isOverlayOn = false
        }
        sensorManager.unregisterListener(this, luxSensor)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent) {
        luxValue = event.values[0]
        runOnUiThread { overlayView.text = luxValue.toString() }
    }

    override fun onParamsChanged(updatedParams: WindowManager.LayoutParams) {
        windowManager.updateViewLayout(overlayView, updatedParams)
    }

    private fun setupSensor() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        luxSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    }

    private fun initOverlayView() {
        overlayView = TextView(this)
        overlayView.text = "Overlay Text View"
        overlayView.textSize = 32f
        overlayView.setTextColor(Color.rgb(255, 255, 0))
        overlayView.setShadowLayer(10f, 5f, 5f, Color.rgb(56, 56, 56))
        overlayView.setOnClickListener {
            Toast.makeText(this, "Overlay view clicked", Toast.LENGTH_SHORT).show()
        }

        params = overlayView.makeOverlayDraggable(this)

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
            if (!isOverlayOn) {
                // Show
                windowManager.addView(overlayView, params)
            } else {
                // Hide
                windowManager.removeViewImmediate(overlayView)
            }
            isOverlayOn = !isOverlayOn
        }
    }


}
