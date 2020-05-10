package com.gauravnadar.androidtest.Notification;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gauravnadar.androidtest.R;

public class Notification extends AppCompatActivity {

    TextView msg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);

        msg = findViewById(R.id.notify);

        String message = getIntent().getStringExtra("message");
        msg.setText(message);
    }
}
