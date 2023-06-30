package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.GameState;

public class FieldBuilding extends AttackableBuilding implements CannotPath, CanBeBuiltOver {
    public static Texture texture;

    private static final int goldPerTurn = 1;

    @Override
    public String getName() {
        return "Field";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public String getInfo() {
        int _goldPerTurn = goldPerTurn;
        if (onRift) {
            _goldPerTurn *= 2;
        }
        return "Generate " + _goldPerTurn + " gold per turn.\nThese do not extend your base.";
    }

    @Override
    public void onCreate(final GameState gameState, final boolean onRift) {
        health = 5;
        gameState.goldPerTurn += goldPerTurn;
        if (onRift) {
            gameState.goldPerTurn += goldPerTurn;
        }
        super.onCreate(gameState, onRift);
    }

    @Override
    public void onDestroy(final GameState gameState) {
        gameState.goldPerTurn -= goldPerTurn;
        if (onRift) {
            gameState.goldPerTurn -= goldPerTurn;
        }
        if (gameState.gold > gameState.maxGold) {
            gameState.gold = gameState.maxGold;
        }
    }

    @Override
    public Building duplicate() {
        return new FieldBuilding();
    }
}
