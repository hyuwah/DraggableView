package io.github.hyuwah.draggableview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import io.github.hyuwah.draggableviewlib.Constants
import io.github.hyuwah.draggableviewlib.DraggableImageView
import io.github.hyuwah.draggableviewlib.makeDraggable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var currentStickyAxis = Constants.STICKY.AXIS_X

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Make any view Draggable via Kotlin Extension
        ll_test_draggable.makeDraggable(currentStickyAxis, true)
        tv_test_draggable.makeDraggable(currentStickyAxis, true)

        // Via DraggableImageView in activity_main.xml
        val dvTest = findViewById<DraggableImageView>(R.id.dv_test)

        val swAnimate = findViewById<Switch>(R.id.sw_animate)
        val btnStickyX = findViewById<Button>(R.id.btn_sticky_x)
        val btnStickyY = findViewById<Button>(R.id.btn_sticky_y)
        val btnStickyXY = findViewById<Button>(R.id.btn_sticky_xy)
        val btnNonSticky = findViewById<Button>(R.id.btn_nonsticky)

        swAnimate.isChecked = dvTest.isAnimate()

        swAnimate.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                dvTest.setAnimate(true)
                ll_test_draggable.makeDraggable(currentStickyAxis, true)
                tv_test_draggable.makeDraggable(currentStickyAxis, true)
            } else {
                dvTest.setAnimate(false)
                ll_test_draggable.makeDraggable(currentStickyAxis, false)
                tv_test_draggable.makeDraggable(currentStickyAxis, false)
            }
        }

        // Set sticky axis to X axis
        btnStickyX.setOnClickListener {
            dvTest.setStickyAxis(DraggableImageView.STICKY_AXIS_X)
            currentStickyAxis = Constants.STICKY.AXIS_X
            ll_test_draggable.makeDraggable(currentStickyAxis, swAnimate.isChecked)
            tv_test_draggable.makeDraggable(currentStickyAxis, swAnimate.isChecked)
            Toast.makeText(this@MainActivity, "Sticky Axis set to X", Toast.LENGTH_SHORT).show()
        }

        // Set sticky axis to Y axis
        btnStickyY.setOnClickListener {
            dvTest.setStickyAxis(DraggableImageView.STICKY_AXIS_Y)
            currentStickyAxis = Constants.STICKY.AXIS_Y
            ll_test_draggable.makeDraggable(currentStickyAxis, swAnimate.isChecked)
            tv_test_draggable.makeDraggable(currentStickyAxis, swAnimate.isChecked)
            Toast.makeText(this@MainActivity, "Sticky Axis set to Y", Toast.LENGTH_SHORT).show()
        }

        // Set sticky axis to XY axis
        btnStickyXY.setOnClickListener {
            dvTest.setStickyAxis(DraggableImageView.STICKY_AXIS_XY)
            currentStickyAxis = Constants.STICKY.AXIS_XY
            ll_test_draggable.makeDraggable(currentStickyAxis, swAnimate.isChecked)
            tv_test_draggable.makeDraggable(currentStickyAxis, swAnimate.isChecked)
            Toast.makeText(this@MainActivity, "Sticky Axis set to XY", Toast.LENGTH_SHORT).show()
        }

        // Remove sticky axis / will float
        btnNonSticky.setOnClickListener {
            dvTest.setStickyAxis(DraggableImageView.NON_STICKY)
            currentStickyAxis = Constants.STICKY.NONE
            ll_test_draggable.makeDraggable(currentStickyAxis, swAnimate.isChecked)
            tv_test_draggable.makeDraggable(currentStickyAxis, swAnimate.isChecked)
            Toast.makeText(this@MainActivity, "Sticky Axis set none", Toast.LENGTH_SHORT).show()
        }

        // Set click listener
        dvTest.setOnClickListener {
            Toast.makeText(this@MainActivity, "Clicked", Toast.LENGTH_SHORT).show()
        }

        ll_test_draggable.setOnClickListener {
            Toast.makeText(this@MainActivity, "Draggable LinearLayout Clicked", Toast.LENGTH_SHORT)
                .show()
        }

        tv_test_draggable.setOnClickListener {
            Toast.makeText(this@MainActivity, "Draggable Textview Clicked", Toast.LENGTH_SHORT)
                .show()
        }

    }
}
