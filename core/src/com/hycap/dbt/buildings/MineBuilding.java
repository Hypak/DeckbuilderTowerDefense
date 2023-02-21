package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.GameState;

public class MineBuilding extends AttackableBuilding implements CanBuildOver {
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
        return "Generate " + MineBuilding.goldPerTurn + " gold per turn.\nHold "
                + MineBuilding.goldCapacity + " more gold.";
    }

    @Override
    public void onCreate(GameState gameState, boolean onRift) {
        super.health = 25;
        gameState.maxGold += goldCapacity;
        gameState.goldPerTurn += goldPerTurn;
        if (onRift) {
            gameState.maxGold += goldCapacity;
            gameState.goldPerTurn += goldPerTurn;
        }
        super.onCreate(gameState, onRift);
    }

    @Override
    public void onDestroy(GameState gameState) {
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
