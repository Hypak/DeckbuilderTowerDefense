package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.GameState;

public class CoffersBuilding extends AttackableBuilding {
    public static Texture texture;

    private static final int goldCapacity = 10;

    @Override
    public String getName() {
        return "Coffers";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public String getInfo() {
        int _goldCapacity = goldCapacity;
        if (onRift) {
            _goldCapacity *= 2;
        }
        return "Store " + _goldCapacity + " gold.";
    }

    @Override
    public void onCreate(final GameState gameState, final boolean onRift) {
        health = 50;
        gameState.maxGold += goldCapacity;
        if (onRift) {
            gameState.maxGold += goldCapacity;
        }
        super.onCreate(gameState, onRift);
    }

    @Override
    public void onDestroy(final GameState gameState) {
        gameState.maxGold -= goldCapacity;
        if (onRift) {
            gameState.maxGold -= goldCapacity;
        }
        if (gameState.gold > gameState.maxGold) {
            gameState.gold = gameState.maxGold;
        }
    }

    @Override
    public Building duplicate() {
        return new CoffersBuilding();
    }
}
