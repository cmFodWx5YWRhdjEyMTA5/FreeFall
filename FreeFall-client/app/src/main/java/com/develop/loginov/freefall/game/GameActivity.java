package com.develop.loginov.freefall.game;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.develop.loginov.freefall.R;

public class GameActivity extends AppCompatActivity {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameView = findViewById(R.id.activity_main__game_view);
        gameView.setOnEndGameListener(text -> {
//            Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
            finish();
        });

        final ImageView imageAttack = findViewById(R.id.activity_main__attack);
        final ImageView imageDef = findViewById(R.id.activity_main__def);

        imageAttack.setOnClickListener(v -> gameView.attack());
        imageDef.setOnClickListener(v -> gameView.defense());
    }
}
