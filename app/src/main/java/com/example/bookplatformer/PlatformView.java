package com.example.bookplatformer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PlatformView extends SurfaceView implements Runnable {

    private boolean running = true;
    private boolean debug = true;
    private Thread gameThread = null;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder ourHolder;

    Context context;
    long startFrameTime;
    long timeThisFrame;
    long fps;

    private LevelManager levelManager;
    private ViewPort viewPort;
    InputController inputController;

    public PlatformView(Context context, int screenWidth, int screenHeight) {
        super(context);

        this.context = context;
        ourHolder = getHolder();
        paint = new Paint();

        viewPort = new ViewPort(screenWidth, screenHeight);

        loadLevel("LevelCave", 0, 0);
    }

    private void loadLevel(String level, int px, int py) {
        levelManager = null;
        levelManager = new LevelManager(context,
                viewPort.getPixelsPerMetreX(),
                viewPort.getScreenWidth(),
                inputController, level, px, py);

        inputController = new InputController(viewPort.getScreenWidth(),
                viewPort.getScreenHeight());

        viewPort.setWorldCenter(levelManager.gameObjects.get(levelManager.playerIndex).getWorldLocation().x,
                levelManager.gameObjects.get(levelManager.playerIndex).getWorldLocation().y);
    }


    public void pause() {
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("error", "failed to pause thread");
        }
    }

    public void resume() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while (running) {

            startFrameTime = System.currentTimeMillis();

            update();
            draw();

            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }
        }
    }

    private void update() {
        for (GameObject go : levelManager.gameObjects) {
            if (go.isActive()) {
                if (!viewPort.clipObject(go.getWorldLocation().x, go.getWorldLocation().y,
                        go.getWidth(), go.getHeight())) {
                    go.setVisible(true);
                    if (levelManager.isPlaying()) {
                        go.update(fps, levelManager.gravity);
                    }
                } else {
                    go.setVisible(false);
                }
            }
        }

        if (levelManager.isPlaying()) {
            viewPort.setWorldCenter(levelManager.gameObjects.get(levelManager.playerIndex).
                    getWorldLocation().x,
                    levelManager.gameObjects.get(levelManager.playerIndex).getWorldLocation().y);
        }
    }

    private void draw() {
        if (ourHolder.getSurface().isValid()) {
            canvas = ourHolder.lockCanvas();

            paint.setColor(Color.BLUE);
            paint.setTextSize(100);
            //canvas.drawColor(Color.GREEN);

            //canvas.drawText("Hello", 10, 100, paint);

            Rect toScreen2d = new Rect();

            for (int layer = -1; layer <= 1; layer++) {
                for (GameObject go : levelManager.gameObjects) {
                    if (go.isVisible() && go.getWorldLocation().z == layer) {
                        toScreen2d.set(viewPort.worldToScreen(
                                go.getWorldLocation().x,
                                go.getWorldLocation().y,
                                go.getWidth(),
                                go.getHeight()
                        ));
                        canvas.drawBitmap(
                                levelManager.bitmaps[levelManager.getBitmapIndex(go.getType())],
                                toScreen2d.left,
                                toScreen2d.top,
                                paint
                        );
                    }
                }
            }

            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & event.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                levelManager.switchPlayingStatus();
                break;
        }
        return true;
    }
}
