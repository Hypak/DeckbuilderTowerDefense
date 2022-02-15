package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.GameState;
import com.hycap.dbt.Pair;

public class CoffersBuilding extends AttackableBuilding {
    public static Texture texture;

    static final int goldCapacity = 8;

    @Override
    public String getName() {
        return "Coffers";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public void onCreate(GameState gameState) {
        super.health = 50;
        gameState.maxGold += goldCapacity;
    }

    @Override
    public void onDestroy(GameState gameState) {
        gameState.maxGold -= goldCapacity;
        if (gameState.gold > gameState.maxGold) {
            gameState.gold = gameState.maxGold;
        }
    }

    @Override
    public Building duplicate() {
        return new CoffersBuilding();
    }
}
