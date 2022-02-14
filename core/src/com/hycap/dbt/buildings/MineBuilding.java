package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.GameState;

public class MineBuilding implements Building {
    public static Texture texture;
    static final int goldCapacity = 1;
    static final int goldPerTurn = 1;
    @Override
    public String getName() {
        return "Mine";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public void onCreate(GameState gameState) {
        gameState.maxGold += goldCapacity;
        gameState.goldPerTurn += goldPerTurn;
    }

    @Override
    public void onDestroy(GameState gameState) {
        gameState.maxGold -= goldCapacity;
        if (gameState.gold > gameState.maxGold) {
            gameState.gold = gameState.maxGold;
        }
        gameState.goldPerTurn -= goldPerTurn;
    }
}
