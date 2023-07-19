package io.github.hyuwah.draggableview;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import io.github.hyuwah.draggableviewlib.DraggableListener;
import io.github.hyuwah.draggableviewlib.DraggableView;

import static io.github.hyuwah.draggableview.utils.ExtensionsKt.toast;

public class JavaMainActivity extends AppCompatActivity implements DraggableListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java_main);
        setTitle("Java Activity");

        Button button = findViewById(R.id.btn_java);

        DraggableView<Button> buttonDraggable = new DraggableView.Builder<>(button)
                .setStickyMode(DraggableView.Mode.NON_STICKY) // default NON_STICKY
                .setAnimated(true) // default true
                .setListener(this) // default null
                .build();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast(JavaMainActivity.this, "Button Clicked");
            }
        });

    }

    @Override
    public void onPositionChanged(@NotNull View view) {
        TextView textView = findViewById(R.id.tv_coordinate);
        textView.setText("X: " + view.getX() + "\tY: " + view.getY());
    }

    @Override
    public void onLongPress(@NonNull View view) {
        toast(this,"Long press view : " + view.getId());
    }
}
