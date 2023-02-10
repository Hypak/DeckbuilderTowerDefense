package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.GameState;

public class CoffersBuilding extends AttackableBuilding {
    public static Texture texture;

    public static final int goldCapacity = 8;

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
        return "Store " + CoffersBuilding.goldCapacity + " gold.";
    }

    @Override
    public void onCreate(GameState gameState, boolean onRift) {
        super.health = 50;
        gameState.maxGold += goldCapacity;
        if (onRift) {
            gameState.maxGold += goldCapacity;
        }
        super.onCreate(gameState, onRift);
    }

    @Override
    public void onDestroy(GameState gameState) {
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
