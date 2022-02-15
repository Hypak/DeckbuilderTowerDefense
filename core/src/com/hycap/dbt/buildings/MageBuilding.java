package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.GameState;
import com.hycap.dbt.Pair;

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
    public void onCreate(GameState gameState) {
        super.health = 30;
        gameState.baseEnergy += energyIncrease;
    }

    @Override
    public void onDestroy(GameState gameState) {
        gameState.baseEnergy -= energyIncrease;
        if (gameState.currentEnergy > gameState.baseEnergy) {
            gameState.currentEnergy = gameState.baseEnergy;
        }
    }

    @Override
    public Building duplicate() {
        return new MageBuilding();
    }

}
