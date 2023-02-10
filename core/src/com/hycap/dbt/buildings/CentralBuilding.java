package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.GameState;

public class CentralBuilding extends AttackableBuilding {
    public static Texture texture;
    public static int goldCapacity = 3;
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
    public void onCreate(GameState gameState, boolean onRift) {
        super.health = 100;
        gameState.baseEnergy += energyPerTurn;
        gameState.maxGold += goldCapacity;
        super.onCreate(gameState, onRift);
    }

    @Override
    public void onDestroy(GameState gameState) {
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
