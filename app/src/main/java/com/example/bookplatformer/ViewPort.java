package com.example.bookplatformer;

import android.graphics.Rect;

public class ViewPort {
    private Vector2Point5D currentViewportWorldCenter;
    private Rect convertedRect;
    private int pixelsPerMetreX;
    private int pixelsPerMetreY;
    private int screenXResolution;
    private int screenYResolution;
    private int screenCenterX;
    private int screenCenterY;
    private int metresToShowX;
    private int metresToShowY;
    private int numClipped;

    public ViewPort(int x, int y) {
        this.screenXResolution = x;
        this.screenYResolution = y;

        screenCenterX = screenXResolution / 2;
        screenCenterY = screenYResolution / 2;

        pixelsPerMetreX = screenXResolution / 32;
        pixelsPerMetreY = screenYResolution / 18;

        metresToShowX = 34;
        metresToShowY = 20;

        convertedRect = new Rect();
        currentViewportWorldCenter = new Vector2Point5D();
    }

    public void setWorldCenter(float x, float y) {
        currentViewportWorldCenter.x = x;
        currentViewportWorldCenter.y = y;
    }

    public int getScreenWidth() {
        return screenXResolution;
    }


    public int getScreenHeight() {
        return screenYResolution;
    }

    public int getPixelsPerMetreX() {
        return pixelsPerMetreX;
    }

    public Rect worldToScreen(
            float objectX,
            float objectY,
            float objectWidth,
            float objectHeight
    ) {
        int left = (int) (screenCenterX - (currentViewportWorldCenter.x - objectX) * pixelsPerMetreX);
        int top = (int) (screenCenterY - (currentViewportWorldCenter.y - objectY) * pixelsPerMetreY);
        int right = (int) (left + objectWidth) * pixelsPerMetreX;
        int bottom = (int) (top - objectHeight) * pixelsPerMetreY;

        convertedRect.set(left, top, right, bottom);
        return convertedRect;

    }

    public boolean clipObject(
            float objectX,
            float objectY,
            float objectWidth,
            float objectHeight
    ) {
        boolean clipped = true;

        if (objectX - objectWidth < currentViewportWorldCenter.x + (metresToShowX / 2)) {
            if (objectX + objectWidth > currentViewportWorldCenter.x - (metresToShowX / 2)) {
                if (objectY - objectHeight < currentViewportWorldCenter.y + (metresToShowY / 2)) {
                    if (objectY + objectHeight > currentViewportWorldCenter.y - (metresToShowY / 2)) {
                        clipped = false;
                    }
                }
            }
            if (clipped) {
                numClipped++;
            }
        }
        return clipped;
    }

    public int getNumClipped() {
        return numClipped;
    }

    public void resetNumClipped() {
        numClipped = 0;
    }
}
