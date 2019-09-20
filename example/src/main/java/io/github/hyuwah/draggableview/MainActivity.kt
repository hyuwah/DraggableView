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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var dvTest = findViewById<DraggableImageView>(R.id.dv_test)

        var swAnimate = findViewById<Switch>(R.id.sw_animate)
        var btnStickyX= findViewById<Button>(R.id.btn_sticky_x)
        var btnStickyY= findViewById<Button>(R.id.btn_sticky_y)
        var btnStickyXY= findViewById<Button>(R.id.btn_sticky_xy)
        var btnNonSticky= findViewById<Button>(R.id.btn_nonsticky)

        swAnimate.isChecked = dvTest.isAnimate()
        swAnimate.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                dvTest.setAnimate(true)
                tv_test_draggable.makeDraggable(Constants.STICKY.AXIS_X, true)
            } else {
                tv_test_draggable.makeDraggable(Constants.STICKY.AXIS_X, false)
                dvTest.setAnimate(false)
            }
        }

        btnStickyX.setOnClickListener {
            dvTest.setStickyAxis(DraggableImageView.STICKY_AXIS_X)
            Toast.makeText(this@MainActivity, "Sticky Axis set to X", Toast.LENGTH_SHORT).show()
        }

        btnStickyY.setOnClickListener {
            dvTest.setStickyAxis(DraggableImageView.STICKY_AXIS_Y)
            Toast.makeText(this@MainActivity, "Sticky Axis set to Y", Toast.LENGTH_SHORT).show()
        }

        btnStickyXY.setOnClickListener {
            dvTest.setStickyAxis(DraggableImageView.STICKY_AXIS_XY)
            Toast.makeText(this@MainActivity, "Sticky Axis set to XY", Toast.LENGTH_SHORT).show()
        }

        btnNonSticky.setOnClickListener {
            dvTest.setStickyAxis(DraggableImageView.NON_STICKY)
            Toast.makeText(this@MainActivity, "Sticky Axis set none", Toast.LENGTH_SHORT).show()
        }

        dvTest.setOnClickListener {
            Toast.makeText(this@MainActivity, "Clicked", Toast.LENGTH_SHORT).show()
        }

        ll_test_draggable.makeDraggable(Constants.STICKY.AXIS_Y)
        tv_test_draggable.makeDraggable(Constants.STICKY.AXIS_X, true)
    }
}
