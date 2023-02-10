package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.GameState;

public class MageBuilding extends AttackableBuilding {
    public static Texture texture;

    public static int energyIncrease = 1;

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
        return "Generate " + MageBuilding.energyIncrease + " more energy per turn.";
    }

    @Override
    public void onCreate(GameState gameState, boolean onRift) {
        super.health = 50;
        gameState.baseEnergy += energyIncrease;
        if (onRift) {
            gameState.baseEnergy += energyIncrease;
        }
        super.onCreate(gameState, onRift);
    }

    @Override
    public void onDestroy(GameState gameState) {
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
