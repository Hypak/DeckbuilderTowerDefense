package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.GameState;

public class MageBuilding implements Building {
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
    public void onCreate(GameState gameState) {
        gameState.baseEnergy += energyIncrease;
    }

    @Override
    public void onDestroy(GameState gameState) {
        gameState.baseEnergy -= energyIncrease;
        if (gameState.currentEnergy > gameState.baseEnergy) {
            gameState.currentEnergy = gameState.baseEnergy;
        }
    }
}
