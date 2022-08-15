package com.example.bookplatformer;

import android.content.Context;

public class Player extends GameObject {
    final float MAX_X_VELOCITY = 10;
    boolean isPressingRight = false;
    boolean isPressingLeft = false;

    private boolean isFalling;
    private boolean isJumping;
    private long jumpTime;
    private long maxJumpTime = 700;

    RectHitbox rectHitboxFeet;
    RectHitbox rectHitboxHead;
    RectHitbox rectHitboxLeft;
    RectHitbox rectHitboxRight;

    public Player(Context context, float worldStartX, float worldStartY, int pixelPerMetre) {
        final float HEIGHT = 1;
        final float WIDTH = 2;

        setHeight(HEIGHT);
        setWidth(WIDTH);

        setType('p');

        setBitmapName("bunny");

        setxVelocity(0);
        setyVelocity(0);
        setFacing(LEFT);
        isFalling = false;

        setMove(true);
        setActive(true);
        setVisible(true);

        rectHitboxFeet = new RectHitbox();
        rectHitboxHead = new RectHitbox();
        rectHitboxLeft = new RectHitbox();
        rectHitboxRight = new RectHitbox();

        setWorldLocation(worldStartX, worldStartY, 0);
    }

    @Override
    public void update(long fps, float gravity) {
        if (isPressingRight) {
            this.setxVelocity(MAX_X_VELOCITY);
        } else if (isPressingLeft) {
            this.setyVelocity(-MAX_X_VELOCITY);
        } else {
            this.setxVelocity(0);
        }

        if (this.getxVelocity() > 0) {
            setFacing(RIGHT);
        } else if (this.getxVelocity() < 0) {
            setFacing(LEFT);
        }

        if (isJumping) {
            long timeJumping = System.currentTimeMillis() - jumpTime;
            if (timeJumping < maxJumpTime) {
                if (timeJumping < maxJumpTime / 2) {
                    this.setyVelocity(-gravity);
                } else if (timeJumping > maxJumpTime / 2) {
                    this.setyVelocity(gravity);
                }
            } else {
                isJumping = false;
            }
        } else {
            this.setyVelocity(gravity);
            isFalling = true;
        }

        this.move(fps);

        Vector2Point5D location = getWorldLocation();
        float lx = location.x;
        float ly = location.y;

        rectHitboxFeet.top = ly + getHeight() * .95f;
        rectHitboxFeet.bottom = ly + getHeight() * .2f;
        rectHitboxFeet.left = lx + getWidth() * .95f;
        rectHitboxFeet.right = lx + getWidth() * .8f;

    }
}
