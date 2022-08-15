package com.example.bookplatformer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.ArrayList;

public class LevelManager {
    private String level;

    int mapWidth;
    int mapHeight;

    Player player;
    int playerIndex;

    private boolean playing;
    float gravity;

    LevelData levelData;
    ArrayList<GameObject> gameObjects;

    ArrayList<Rect> currentButtons;
    Bitmap[] bitmaps;

    public LevelManager(
            Context context,
            int pixelsPerMetre,
            int screenWidth,
            InputController inputController,
            String level,
            float px, float py
    ) {
        switch (level) {
            case "LevelCave":
                levelData = new LevelCave();
                break;
        }
        gameObjects = new ArrayList<>();
        bitmaps = new Bitmap[25];

        loadMapData(context, pixelsPerMetre, px, py);

        playing = true;
    }

    public boolean isPlaying() {
        return playing;
    }

    public Bitmap getBitmap(char blockType) {

        int index;
        switch (blockType) {
            case '.':
                index = 0;
                break;
            case '1':
                index = 1;
                break;
            case 'p':
                index = 2;
                break;
            default:
                index = 0;
                break;
        }
        return bitmaps[index];
    }

    public int getBitmapIndex(char blockType) {
        switch (blockType) {
            case '.':
                return 0;
            case '1':
                return 1;
            case '2':
                return 2;
        }
        return 0;
    }

    private void loadMapData(Context context, int pixelsPerMetre, float px, float py) {
        char c;
        int currentIndex = -1;

        mapHeight = levelData.tiles.size();
        mapWidth = levelData.tiles.get(0).length();

        for (int i = 0; i < levelData.tiles.size(); i++) {
            for (int j = 0; j < levelData.tiles.get(i).length(); j++) {
                c = levelData.tiles.get(i).charAt(j);
                if (c != '.') {
                    currentIndex++;
                    switch (c) {
                        case '1':
                            gameObjects.add(new Grass(j, i, c));
                            break;
                        case 'p':
                            gameObjects.add(new Player(context, px, py, pixelsPerMetre));
                            playerIndex = currentIndex;
                            player = (Player) gameObjects.get(playerIndex);
                            break;
                    }
                    if (bitmaps[getBitmapIndex(c)] == null) {
                        bitmaps[getBitmapIndex(c)] =
                                gameObjects.get(currentIndex).
                                        prepareBitmap(context, gameObjects.get(currentIndex).getBitmapName(),
                                                pixelsPerMetre);
                    }
                }
            }
        }
    }


}
