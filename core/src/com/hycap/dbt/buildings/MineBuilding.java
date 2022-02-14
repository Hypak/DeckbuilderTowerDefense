package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.GameState;
import com.hycap.dbt.Pair;

public class MineBuilding implements AttackableBuilding {
    public static Texture texture;
    static final int goldCapacity = 1;
    static final int goldPerTurn = 1;

    float health = 20;
    Pair<Integer> position;

    @Override
    public String getName() {
        return "Mine";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public void onCreate(GameState gameState) {
        gameState.maxGold += goldCapacity;
        gameState.goldPerTurn += goldPerTurn;
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
    public void setPosition(Pair<Integer> position) {
        this.position = position;
    }

    @Override
    public Pair<Integer> getPosition() {
        return position;
    }

    @Override
    public Building duplicate() {
        return new MineBuilding();
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
