package io.github.hyuwah.draggableview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.hyuwah.draggableview.databinding.ActivityMainBinding
import io.github.hyuwah.draggableview.utils.launch
import io.github.hyuwah.draggableview.utils.viewBinding

class MainActivity : AppCompatActivity() {

    private val vb by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(vb.root)

        vb.btnBasicActivity.setOnClickListener {
            launch<BasicExampleActivity>()
        }

        vb.btnJavaActivity.setOnClickListener {
            launch<JavaMainActivity>()
        }

        vb.btnOverlayActivity.setOnClickListener {
            launch<OverlayDraggableActivity>()
        }

        vb.btnScrollingActivity.setOnClickListener {
            launch<ScrollingActivity>()
        }

    }
}
