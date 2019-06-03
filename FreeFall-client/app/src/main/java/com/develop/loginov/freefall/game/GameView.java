package com.develop.loginov.freefall.game;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private final SurfaceHolder holder;
    private OnEndGameListener onEndGameListener;
    private GameThread thread;

    public GameView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        holder.addCallback(this);
    }

    public GameView(final Context context) {
        this(context, null);
    }

    public void setOnEndGameListener(final OnEndGameListener onEndGameListener) {
        this.onEndGameListener = onEndGameListener;
        if (thread != null) {
            thread.setOnEndGameListener(onEndGameListener);
        }
    }

    public void attack() {
        thread.attack();
    }

    public void defense() {
        thread.defense();
    }

    @Override
    public void surfaceCreated(final SurfaceHolder holder) {
        thread = new GameThread(holder, getResources(), getWidth(), getHeight());
        thread.setOnEndGameListener(onEndGameListener);
        thread.start();
    }

    @Override
    public void surfaceChanged(final SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(final SurfaceHolder holder) {
        thread.stopGame();
    }
}
