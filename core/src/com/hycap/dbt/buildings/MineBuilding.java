package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.GameState;
import com.hycap.dbt.Pair;

public class MineBuilding extends AttackableBuilding {
    public static Texture texture;

    public static final int goldCapacity = 2;
    public static final int goldPerTurn = 1;

    @Override
    public String getName() {
        return "Mine";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public String getInfo() {
        return "Generate " + MineBuilding.goldPerTurn + " gold per turn, and hold "
                + MineBuilding.goldCapacity + " more gold.";
    }

    @Override
    public void onCreate(GameState gameState) {
        super.health = 25;
        gameState.maxGold += goldCapacity;
        gameState.goldPerTurn += goldPerTurn;
        super.onCreate(gameState);
    }

    @Override
    public void onDestroy(GameState gameState) {
        gameState.maxGold -= goldCapacity;
        if (gameState.gold > gameState.maxGold) {
            gameState.gold = gameState.maxGold;
        }
        gameState.goldPerTurn -= goldPerTurn;
    }

    @Override
    public Building duplicate() {
        return new MineBuilding();
    }
}
