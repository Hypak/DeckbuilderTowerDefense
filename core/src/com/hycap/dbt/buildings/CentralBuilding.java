package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.GameState;
import com.hycap.dbt.Pair;

public class CentralBuilding implements AttackableBuilding {
    public static Texture texture;

    Pair<Integer> position;
    float health = 50;
    @Override
    public String getName() {
        return "Central";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public void onCreate(GameState gameState) {

    }

    @Override
    public void onDestroy(GameState gameState) {

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
        return new CentralBuilding();
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
