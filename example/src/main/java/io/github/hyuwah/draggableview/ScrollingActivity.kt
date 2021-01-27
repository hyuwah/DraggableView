package io.github.hyuwah.draggableview

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import io.github.hyuwah.draggableview.databinding.ActivityScrollingBinding
import io.github.hyuwah.draggableview.utils.viewBinding
import io.github.hyuwah.draggableviewlib.DraggableListener
import io.github.hyuwah.draggableviewlib.DraggableView
import io.github.hyuwah.draggableviewlib.setupDraggable

class ScrollingActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityScrollingBinding::inflate)
    private lateinit var dvImageDraggable: DraggableView<ImageView>

    private val debugPos = object : DraggableListener {
        override fun onPositionChanged(view: View) {
            binding.tvDebug.text = """
                X: ${view.x}
                Y: ${view.y}
            """.trimIndent()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        title = "Scrolling Activity"

        // Setup draggable view
        dvImageDraggable = binding.dvImage.setupDraggable()
            .setStickyMode(DraggableView.Mode.STICKY_X)
            .setListener(debugPos)
            .build()

        // set click handler of draggable view
        // example of dock/undocking the view
        binding.dvImage.setOnClickListener {
            if (dvImageDraggable.isMinimized) {
                dvImageDraggable.undock()
            } else {
                dvImageDraggable.dockToEdge()
            }
        }

        // example of hide/showing the view based on scrolling activity
        binding.scrollView.scrollState(
            onIdle = {
                binding.tvScroll.text = "Idle/Show"
                dvImageDraggable.show()
            },
            onScrolled = {
                binding.tvScroll.text = "Scrolling/Hide"
                dvImageDraggable.hide()
            }
        )
    }


    /**
     * Helper function to detect scroll / idle state of scrollview
     */
    @SuppressLint("ClickableViewAccessibility")
    inline fun NestedScrollView.scrollState(
        crossinline onIdle: () -> Unit,
        crossinline onScrolled: () -> Unit
    ) {
        val handler = Handler()
        var isScrolled = false
        setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_SCROLL,
                MotionEvent.ACTION_DOWN -> {
                    handler.removeCallbacksAndMessages(null)
                    onScrolled()
                    isScrolled = true
                }
                MotionEvent.ACTION_MOVE -> {
                    handler.removeCallbacksAndMessages(null)
                    if (!isScrolled) {
                        onScrolled()
                        isScrolled = true
                    }
                }
                MotionEvent.ACTION_CANCEL,
                MotionEvent.ACTION_UP -> {
                    handler.postDelayed({ onIdle() }, 500)
                    isScrolled = false
                }
            }
            false
        }
    }
}