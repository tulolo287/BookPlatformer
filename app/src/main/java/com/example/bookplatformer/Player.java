package com.example.bookplatformer;

import android.content.Context;

public class Player extends GameObject {

    public Player(Context context, float worldStartX, float worldStartY, int pixelPerMetre) {
        final float HEIGHT = 1;
        final float WIDTH = 2;

        setHeight(HEIGHT);
        setWidth(WIDTH);

        setType('p');

        setBitmapName("bunny");

        setWorldLocation(worldStartX, worldStartY, 0);
    }

    @Override
    public void update(long fps, float gravity) {

    }
}
