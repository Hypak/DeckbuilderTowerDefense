package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.GameState;

public class CentralBuilding extends AttackableBuilding implements CannotBeRemoved {
    public static Texture texture;
    private static final int goldCapacity = 8;
    public static int energyPerTurn = 3;

    @Override
    public String getName() {
        return "Central";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public String getInfo() {
        return "Center of your base.";
    }

    @Override
    public void onCreate(final GameState gameState, final boolean onRift) {
        health = 100;
        gameState.baseEnergy += energyPerTurn;
        gameState.maxGold += goldCapacity;
        gameState.gold += goldCapacity;
        super.onCreate(gameState, onRift);
    }

    @Override
    public void onDestroy(final GameState gameState) {
        gameState.maxGold -= goldCapacity;
        if (gameState.gold > gameState.maxGold) {
            gameState.gold = gameState.maxGold;
        }
        gameState.baseEnergy -= energyPerTurn;
        if (gameState.currentEnergy > gameState.baseEnergy) {
            gameState.currentEnergy = gameState.baseEnergy;
        }
    }

    @Override
    public Building duplicate() {
        return new CentralBuilding();
    }

}
