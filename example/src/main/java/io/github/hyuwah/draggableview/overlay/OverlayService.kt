package io.github.hyuwah.draggableview.overlay

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import io.github.hyuwah.draggableviewlib.OverlayDraggableListener
import io.github.hyuwah.draggableviewlib.makeOverlayDraggable

class OverlayService : Service(), SensorEventListener, OverlayDraggableListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var luxSensor: Sensor
    private lateinit var overlayView: TextView
    private var luxValue = 0f
    private var isOverlayOn = false
    private var params: WindowManager.LayoutParams? = null
    private lateinit var windowManager: WindowManager

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        isOverlayOn = if (isOverlayOn) {
            windowManager.removeViewImmediate(overlayView)
            false
        } else {
            windowManager.addView(overlayView, params)
            true
        }
        return START_NOT_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        setupSensor()
        initOverlayView()

        sensorManager.registerListener(this, luxSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isOverlayOn) {
            windowManager.removeViewImmediate(overlayView)
        }
        isOverlayOn = false
        sensorManager.unregisterListener(this, luxSensor)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent) {
        luxValue = event.values[0]
        overlayView.text = luxValue.toString()
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
}