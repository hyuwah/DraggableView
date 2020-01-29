package io.github.hyuwah.draggableview

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import io.github.hyuwah.draggableviewlib.Draggable
import io.github.hyuwah.draggableviewlib.DraggableImageView
import io.github.hyuwah.draggableviewlib.DraggableListener
import io.github.hyuwah.draggableviewlib.makeDraggable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var currentStickyAxis = Draggable.STICKY.AXIS_X

    val llTestDraggableListener = object : DraggableListener {
        override fun onViewMove(view: View) {
            tv_ll_1.text = "X: ${view.x}"
            tv_ll_2.text = "Y: ${view.y}"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Make any view Draggable via Kotlin Extension
        ll_test_draggable.makeDraggable(currentStickyAxis, true, llTestDraggableListener)
        tv_test_draggable.makeDraggable(currentStickyAxis, true)

        // Via DraggableImageView in activity_main.xml
        val dvTest = findViewById<DraggableImageView>(R.id.dv_test)

        // Set on view move listener
        dvTest.setListener(object : DraggableListener {
            override fun onViewMove(view: View) {
                tv_test_draggable.text = "Icon Coordinates\nX: ${view.x}\nY: ${view.y}"
            }
        })

        val swAnimate = findViewById<Switch>(R.id.sw_animate)
        val btnStickyX = findViewById<Button>(R.id.btn_sticky_x)
        val btnStickyY = findViewById<Button>(R.id.btn_sticky_y)
        val btnStickyXY = findViewById<Button>(R.id.btn_sticky_xy)
        val btnNonSticky = findViewById<Button>(R.id.btn_nonsticky)

        swAnimate.isChecked = dvTest.isAnimate()

        swAnimate.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                dvTest.setAnimate(true)
                ll_test_draggable.makeDraggable(currentStickyAxis, true, llTestDraggableListener)
                tv_test_draggable.makeDraggable(currentStickyAxis, true)
            } else {
                dvTest.setAnimate(false)
                ll_test_draggable.makeDraggable(currentStickyAxis, false, llTestDraggableListener)
                tv_test_draggable.makeDraggable(currentStickyAxis, false)
            }
        }

        // Set sticky axis to X axis
        btnStickyX.setOnClickListener {
            dvTest.setStickyAxis(DraggableImageView.STICKY_AXIS_X)
            currentStickyAxis = Draggable.STICKY.AXIS_X
            ll_test_draggable.makeDraggable(
                currentStickyAxis,
                swAnimate.isChecked,
                llTestDraggableListener
            )
            tv_test_draggable.makeDraggable(currentStickyAxis, swAnimate.isChecked)
            Toast.makeText(this@MainActivity, "Sticky Axis set to X", Toast.LENGTH_SHORT).show()
        }

        // Set sticky axis to Y axis
        btnStickyY.setOnClickListener {
            dvTest.setStickyAxis(DraggableImageView.STICKY_AXIS_Y)
            currentStickyAxis = Draggable.STICKY.AXIS_Y
            ll_test_draggable.makeDraggable(
                currentStickyAxis,
                swAnimate.isChecked,
                llTestDraggableListener
            )
            tv_test_draggable.makeDraggable(currentStickyAxis, swAnimate.isChecked)
            Toast.makeText(this@MainActivity, "Sticky Axis set to Y", Toast.LENGTH_SHORT).show()
        }

        // Set sticky axis to XY axis
        btnStickyXY.setOnClickListener {
            dvTest.setStickyAxis(DraggableImageView.STICKY_AXIS_XY)
            currentStickyAxis = Draggable.STICKY.AXIS_XY
            ll_test_draggable.makeDraggable(
                currentStickyAxis,
                swAnimate.isChecked,
                llTestDraggableListener
            )
            tv_test_draggable.makeDraggable(currentStickyAxis, swAnimate.isChecked)
            Toast.makeText(this@MainActivity, "Sticky Axis set to XY", Toast.LENGTH_SHORT).show()
        }

        // Remove sticky axis / will float
        btnNonSticky.setOnClickListener {
            dvTest.setStickyAxis(DraggableImageView.NON_STICKY)
            currentStickyAxis = Draggable.STICKY.NONE
            ll_test_draggable.makeDraggable(
                currentStickyAxis,
                swAnimate.isChecked,
                llTestDraggableListener
            )
            tv_test_draggable.makeDraggable(currentStickyAxis, swAnimate.isChecked)
            Toast.makeText(this@MainActivity, "Sticky Axis set none", Toast.LENGTH_SHORT).show()
        }

        // Set click listener
        dvTest.setOnClickListener {
            Toast.makeText(this@MainActivity, "Clicked", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, JavaMainActivity::class.java))
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
