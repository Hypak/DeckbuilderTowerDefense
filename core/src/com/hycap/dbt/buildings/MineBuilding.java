package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.GameState;

public class MineBuilding extends AttackableBuilding {
    public static Texture texture;

    private static final int goldCapacity = 2;
    private static final int goldPerTurn = 1;

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
        return "Generate " + goldPerTurn + " gold per turn.\nHold "
                + goldCapacity + " more gold.";
    }

    @Override
    public void onCreate(final GameState gameState, final boolean onRift) {
        health = 25;
        gameState.maxGold += goldCapacity;
        gameState.goldPerTurn += goldPerTurn;
        if (onRift) {
            gameState.maxGold += goldCapacity;
            gameState.goldPerTurn += goldPerTurn;
        }
        super.onCreate(gameState, onRift);
    }

    @Override
    public void onDestroy(final GameState gameState) {
        gameState.maxGold -= goldCapacity;
        gameState.goldPerTurn -= goldPerTurn;
        if (onRift) {
            gameState.maxGold -= goldCapacity;
            gameState.goldPerTurn -= goldPerTurn;
        }
        if (gameState.gold > gameState.maxGold) {
            gameState.gold = gameState.maxGold;
        }
    }

    @Override
    public Building duplicate() {
        return new MineBuilding();
    }
}
