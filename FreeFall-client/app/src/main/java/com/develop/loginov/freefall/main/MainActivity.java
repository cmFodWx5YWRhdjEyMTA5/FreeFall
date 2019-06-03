package com.develop.loginov.freefall.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.develop.loginov.freefall.profile.ProfileActivity;
import com.develop.loginov.freefall.R;
import com.develop.loginov.freefall.game.GameActivity;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button profile = findViewById(R.id.activity_main__profile);
        final Button play = findViewById(R.id.activity_main__game);

        profile.setOnClickListener(v -> {
            final Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        play.setOnClickListener(v -> {
            final Intent intent = new Intent(MainActivity.this, GameActivity.class);
            startActivity(intent);
        });

        profile.setOnTouchListener(onTouchListener);
        play.setOnTouchListener(onTouchListener);
    }

    @SuppressLint("ClickableViewAccessibility")
    public static View.OnTouchListener onTouchListener = (v, event) -> {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                v.setBackgroundResource(R.color.ltgray);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                v.setBackgroundResource(R.color.white);
                break;
        }

        return false;
    };

}
