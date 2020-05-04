package io.github.hyuwah.draggableview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import io.github.hyuwah.draggableviewlib.Draggable;
import io.github.hyuwah.draggableviewlib.DraggableListener;
import io.github.hyuwah.draggableviewlib.DraggableUtils;

public class JavaMainActivity extends AppCompatActivity implements DraggableListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java_main);
        setTitle("Java Activity");

        Button button = findViewById(R.id.btn_java);

        DraggableUtils.makeDraggable(button, Draggable.STICKY.AXIS_X, true, this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(JavaMainActivity.this, "Button Clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onPositionChanged(@NotNull View view) {
        TextView textView = findViewById(R.id.tv_coordinate);
        textView.setText("X: " + view.getX() + "\tY: " + view.getY());
    }
}
