package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.GameState;
import com.hycap.dbt.Pair;

public class MageBuilding implements AttackableBuilding {
    public static Texture texture;
    public static int energyIncrease = 1;
    float health = 30;
    Pair<Integer> position;

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

    @Override
    public void setPosition(Pair<Integer> position) {
        this.position = position;
    }

    @Override
    public Pair<Integer> getPosition() {
        return position;
    }

    @Override
    public Building duplicate() {
        return new MageBuilding();
    }

    @Override
    public void attack(float damage) {
        health -= damage;
        if (health < 0) {
            onDestroy(GameState.gameState);
            GameState.gameState.map.removeBuilding(this);
        }
    }
}
