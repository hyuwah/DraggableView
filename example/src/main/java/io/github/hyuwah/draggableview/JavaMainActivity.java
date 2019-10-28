package io.github.hyuwah.draggableview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import io.github.hyuwah.draggableviewlib.Draggable;
import io.github.hyuwah.draggableviewlib.DraggableUtils;

public class JavaMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java_main);

        Button button = findViewById(R.id.btn_java);

        DraggableUtils.makeDraggable(button, Draggable.STICKY.AXIS_X, true);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(JavaMainActivity.this, "Button Clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
