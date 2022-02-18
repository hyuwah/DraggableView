package io.github.hyuwah.draggableview

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import io.github.hyuwah.draggableview.databinding.ActivityBasicExampleBinding
import io.github.hyuwah.draggableview.utils.toast
import io.github.hyuwah.draggableview.utils.viewBinding
import io.github.hyuwah.draggableviewlib.DraggableListener
import io.github.hyuwah.draggableviewlib.DraggableView
import io.github.hyuwah.draggableviewlib.setupDraggable

class BasicExampleActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityBasicExampleBinding::inflate)

    private lateinit var llTestDraggable: DraggableView<LinearLayout>
    private lateinit var tvTestDraggable: DraggableView<TextView>
    private lateinit var ivTestDraggable: DraggableView<ImageView>

    private var llDragListener = object : DraggableListener {
        override fun onPositionChanged(view: View) {
            with(binding) {
                tvLl1.text = "X: ${view.x.toString().take(6)}"
                tvLl2.text = "Y: ${view.y.toString().take(6)}"
            }
        }

        override fun onLongPress(view: View) {
            toast("Long press view : ${view.id}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupControl()
        setupDraggableView()
        setupDraggableViewOnClick()
    }

    private fun setupDraggableView() {
        // Using extension function
        tvTestDraggable = binding.tvTest.setupDraggable().build()
        // Using DraggableView Builder
        llTestDraggable = DraggableView.Builder(binding.llTest)
            .setStickyMode(DraggableView.Mode.NON_STICKY)
            .setListener(llDragListener)
            .build()
        // Using extension function
        ivTestDraggable = binding.ivTest.setupDraggable()
            .setAnimated(true)
            .setStickyMode(DraggableView.Mode.NON_STICKY)
            .build()
    }

    private fun setupDraggableViewOnClick() {
        binding.ivTest.setOnClickListener {
            toast("This is 0")
        }

        binding.llTest.setOnClickListener {
            toast("This is 1")
        }

        binding.tvTest.setOnClickListener {
            toast("This is 2")
        }
    }

    private fun setupControl() {
        with(binding.layoutControl) {

            // Set Sticky Mode
            etStickyMode.setText(DraggableView.Mode.NON_STICKY.name)
            val stickyModeAdapter = ArrayAdapter(
                this@BasicExampleActivity,
                android.R.layout.simple_spinner_dropdown_item,
                DraggableView.Mode.values().map { it.name }
            )
            etStickyMode.setAdapter(stickyModeAdapter)
            etStickyMode.doOnTextChanged { text, _, _, _ ->
                val selectedMode = DraggableView.Mode.values().find { it.name == text.toString() }
                toast(selectedMode?.name ?: "Null")

                selectedMode?.let {
                    llTestDraggable.sticky = it
                    tvTestDraggable.sticky = it
                    ivTestDraggable.sticky = it
                }
            }

            // Set animation
            swAnimate.isChecked = true
            swAnimate.setOnCheckedChangeListener { _, isChecked ->
                llTestDraggable.animated = isChecked
                tvTestDraggable.animated = isChecked
                ivTestDraggable.animated = isChecked
            }

            // Set drag enabled / disabled
            swDisableDraggable.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    llTestDraggable.disableDrag()
                    tvTestDraggable.disableDrag()
                    ivTestDraggable.disableDrag()
                } else {
                    llTestDraggable.enableDrag()
                    tvTestDraggable.enableDrag()
                    ivTestDraggable.enableDrag()
                }
            }

            // Set show/hide draggable view
            swHideDraggable.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    tvTestDraggable.hide()
                    llTestDraggable.hide()
                    ivTestDraggable.hide()
                } else {
                    tvTestDraggable.show()
                    llTestDraggable.show()
                    ivTestDraggable.show()
                }
            }
        }
    }

}