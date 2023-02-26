package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.GameState;

public class MageBuilding extends AttackableBuilding {
    public static Texture texture;

    private static final int energyIncrease = 1;

    @Override
    public String getName() {
        return "Mage";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public String getInfo() {
        int _energyIncrease = energyIncrease;
        if (onRift) {
            _energyIncrease *= 2;
        }
        return "Generate " + _energyIncrease + " more energy per turn.";
    }

    @Override
    public void onCreate(final GameState gameState, final boolean onRift) {
        health = 50;
        gameState.baseEnergy += energyIncrease;
        if (onRift) {
            gameState.baseEnergy += energyIncrease;
        }
        super.onCreate(gameState, onRift);
    }

    @Override
    public void onDestroy(final GameState gameState) {
        gameState.baseEnergy -= energyIncrease;
        if (onRift) {
            gameState.baseEnergy -= energyIncrease;
        }
        if (gameState.currentEnergy > gameState.baseEnergy) {
            gameState.currentEnergy = gameState.baseEnergy;
        }
    }

    @Override
    public Building duplicate() {
        return new MageBuilding();
    }

}
