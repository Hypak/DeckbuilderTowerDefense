package com.hycap.dbt.buildings;

import com.badlogic.gdx.math.Vector2;
import com.hycap.dbt.GameState;
import com.hycap.dbt.Map;

public abstract class AttackableBuilding extends Building {
    public float health;
    public float maxHealth;
    boolean destroyed = false;

    @Override
    public void onCreate(GameState gameState) {
        this.maxHealth = health;
    }

    @Override
    public String getStats() {
        return "Max Health: " + maxHealth;
    }

    public void attack(float damage) {
        health -= damage;
        GameState.gameState.addHurtParticle(new Vector2(position.getLeft(), position.getRight()));
        if (health < 0 && !destroyed) {
            GameState.gameState.map.removeBuilding(this);
            onDestroy(GameState.gameState);
            destroyed = true;  // Prevent triggering twice
        }
    }
}
