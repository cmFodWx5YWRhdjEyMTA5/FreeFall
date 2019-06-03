package com.develop.loginov.freefall.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;

import com.develop.loginov.freefall.R;

import java.util.Random;

public class GameThread extends Thread {
    private OnEndGameListener onEndGameListener;
    private static final Random random = new Random();

    private final SurfaceHolder holder;
    private GameState state;
    private int width;
    private int height;
    private int step;
    private int margin;
    private boolean running;
    private final Person hero;
    private final Person enemy;
    private boolean pause;
    private Paint paintText;

    private long beginTime;

    public GameThread(final SurfaceHolder holder, final Resources resources, final int width,
                      final int height) {
        this.holder = holder;
        this.width = width;
        this.height = height;
        state = GameState.PAUSE;
        margin = width / 15;
        step = width / 200;
        paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setTextSize(margin);

        final Bitmap bitmapHero = BitmapFactory.decodeResource(resources, R.drawable.hero);
        final Bitmap bitmapEnemy = BitmapFactory.decodeResource(resources, R.drawable.enemy);
        final Bitmap bitmapSword = BitmapFactory.decodeResource(resources, R.drawable.sword);
        final Bitmap bitmapShield = BitmapFactory.decodeResource(resources, R.drawable.shield);
        final Bitmap bitmapHeart = BitmapFactory.decodeResource(resources, R.drawable.like);

        hero = new Person(5,
                          3,
                          1,
                          bitmapHero,
                          bitmapSword,
                          bitmapShield,
                          bitmapHeart,
                          new Rect(margin,
                                   height / 2 - 3 * margin / 2,
                                   4 * margin,
                                   height / 2 + 3 * margin / 2));

        enemy = new Person(4,
                           2,
                           2,
                           bitmapEnemy,
                           bitmapSword,
                           bitmapShield,
                           bitmapHeart,
                           new Rect(width - 4 * margin,
                                    height / 2 - 3 * margin / 2,
                                    width - margin,
                                    height / 2 + 3 * margin / 2));
        beginTime = -1;
    }

    public void setOnEndGameListener(OnEndGameListener onEndGameListener) {
        this.onEndGameListener = onEndGameListener;
    }

    public void attack() {
        state = GameState.FIGHT;
        hero.setAction(PersonAction.ATTACK);
        enemy.setAction(random.nextBoolean() ? PersonAction.ATTACK : PersonAction.DEFENSE);
        beginTime = -1;
    }

    public void defense() {
        state = GameState.FIGHT;
        hero.setAction(PersonAction.DEFENSE);
        enemy.setAction(random.nextBoolean() ? PersonAction.ATTACK : PersonAction.DEFENSE);
        beginTime = -1;
    }

    public void stopGame() {
        running = false;
    }

    @Override
    public void run() {
        running = true;
        pause = false;
        while (running) {
            if (!pause) {
                synchronized (holder) {
                    final Canvas canvas = holder.lockCanvas(null);
                    if (canvas != null) {

                        try {
                            canvas.drawColor(Color.WHITE);

                            switch (state) {
                                case PAUSE:
                                    if (beginTime == -1) {
                                        beginTime = System.currentTimeMillis();
                                    }
                                    final long modTime = 10 - (System.currentTimeMillis() - beginTime) / 1000;
                                    canvas.drawText(Long.toString(modTime),
                                                    (float) width / 2 - margin,
                                                    200,
                                                    paintText);
                                    if (modTime <= 0) {
                                        attack();
                                    }
                                    break;
                                case FIGHT:
                                    if (Person.isNear(hero, enemy)) {
                                        hero.fight(enemy);
                                        enemy.fight(hero);
                                        state = GameState.BACK;
                                        hero.setAction(PersonAction.STAY);
                                        enemy.setAction(PersonAction.STAY);
                                    } else {
                                        hero.goRight(step);
                                        enemy.goLeft(step);
                                    }
                                    break;
                                case BACK:
                                    if (hero.getX() > margin) {
                                        hero.goLeft(step);
                                    } else {
                                        state = GameState.PAUSE;
                                    }

                                    if (enemy.getX() < width - 4 * margin) {
                                        enemy.goRight(step);
                                    } else {
                                        state = GameState.PAUSE;
                                    }
                            }

                            hero.draw(canvas);
                            enemy.draw(canvas);
                            if (onEndGameListener != null) {
                                if (enemy.isDead()) {
                                    onEndGameListener.end("You win!");
                                } else if (hero.isDead()) {
                                    onEndGameListener.end("You lose!");
                                }
                            }
                        } finally {
                            holder.unlockCanvasAndPost(canvas);
                        }
                    }
                }
            }
        }
    }
}
