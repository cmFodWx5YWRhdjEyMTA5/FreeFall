package com.develop.loginov.freefall.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Person {
    private int hp;
    private int damage;
    private int defense;
    private final Bitmap picture;
    private final Bitmap sword;
    private final Bitmap shield;
    private final Bitmap heart;
    private final Rect dst;
    private final Rect src;

    private final Rect dstSword;
    private final Rect dstShield;
    private final Rect dstHeart;
    private final Rect srcSword;
    private final Rect srcShield;
    private final Rect srcHeart;
    private PersonAction action;
    private final Rect dstAction;

    private final Paint paint;
    private int toolBarHeight;

    public Person(final int hp, final int damage, final int defense, final Bitmap picture,
                  final Bitmap sword, final Bitmap shield, final Bitmap heart, final Rect dst) {
        this.hp = hp;
        this.damage = damage;
        this.defense = defense;
        this.picture = picture;

        this.sword = sword;
        this.shield = shield;
        this.heart = heart;

        this.dst = dst;

        toolBarHeight = Math.min(dst.height(), dst.width()) / 6;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setTextSize(toolBarHeight / 1.2f);

        src = new Rect(0, 0, picture.getWidth(), picture.getHeight());
        srcSword = new Rect(0, 0, sword.getWidth(), sword.getHeight());
        srcShield = new Rect(0, 0, shield.getWidth(), shield.getHeight());
        srcHeart = new Rect(0, 0, heart.getWidth(), heart.getHeight());
//
        dstHeart = new Rect(dst.left, dst.top - toolBarHeight, dst.left + toolBarHeight, dst.top);
        dstShield = new Rect(dstHeart.right + toolBarHeight,
                             dst.top - toolBarHeight,
                             dstHeart.right + 2 * toolBarHeight,
                             dst.top);
        dstSword = new Rect(dstShield.right + toolBarHeight,
                            dst.top - toolBarHeight,
                            dstShield.right + 2 * toolBarHeight,
                            dst.top);

        dstAction = new Rect(dst.centerX() - toolBarHeight / 2,
                             dst.bottom,
                             dst.centerX() + toolBarHeight / 2,
                             dst.bottom + toolBarHeight);

        action = PersonAction.STAY;
    }

    public void draw(final Canvas canvas) {
        canvas.drawBitmap(picture, src, dst, paint);

        canvas.drawBitmap(heart, srcHeart, dstHeart, paint);
        canvas.drawText(Integer.toString(hp), dstHeart.right, dstHeart.centerY(), paint);

        canvas.drawBitmap(shield, srcShield, dstShield, paint);
        canvas.drawText(Integer.toString(defense), dstShield.right, dstShield.centerY(), paint);

        canvas.drawBitmap(sword, srcSword, dstSword, paint);
        canvas.drawText(Integer.toString(damage), dstSword.right, dstSword.centerY(), paint);

        if (action == PersonAction.ATTACK) {
            canvas.drawBitmap(sword, srcSword, dstAction, paint);
        } else if (action == PersonAction.DEFENSE) {
            canvas.drawBitmap(shield, srcShield, dstAction, paint);
        }
    }

    private void move(final Rect rect, final int dx) {
        rect.left += dx;
        rect.right += dx;
    }

    public void goRight(final int dx) {
        move(dst, dx);
        move(dstHeart, dx);
        move(dstShield, dx);
        move(dstSword, dx);
        move(dstAction, dx);
    }

    public void goLeft(final int dx) {
        move(dst, -dx);
        move(dstHeart, -dx);
        move(dstShield, -dx);
        move(dstSword, -dx);
        move(dstAction, -dx);
    }

    public static boolean isNear(final Person person1, final Person person2) {
        return Rect.intersects(person1.dst, person2.dst);
    }

    public void fight(final Person person) {
        if (person.action == PersonAction.ATTACK) {
            if (action == PersonAction.DEFENSE) {
                takeDamageWithBlock(person.damage);
            } else {
                takeDamage(person.damage);
            }
        }
    }

    private void takeDamage(int damage) {
        if (defense > 0) {
            defense = Math.max(0, defense - damage);
        } else {
            hp = Math.max(0, hp - damage);
        }
    }

    private void takeDamageWithBlock(int damage) {
        if (defense > 0) {
            defense = Math.max(0, defense - damage / 2);
        } else {
            hp = Math.max(0, hp - damage / 2);
        }
    }

    public void setAction(final PersonAction action) {
        this.action = action;
    }

    public boolean isDead() {
        return hp <= 0;
    }

    public int getX() {
        return dst.left;
    }

    public Rect getRect() {
        return dst;
    }
}
