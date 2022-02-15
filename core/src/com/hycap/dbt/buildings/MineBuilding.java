package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.GameState;
import com.hycap.dbt.Pair;

public class MineBuilding extends AttackableBuilding {
    public static Texture texture;

    static final int goldCapacity = 2;
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
        super.health = 20;
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

    @Override
    public Building duplicate() {
        return new MineBuilding();
    }
}
