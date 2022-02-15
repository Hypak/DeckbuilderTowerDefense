package com.hycap.dbt.buildings;

import com.badlogic.gdx.math.Vector2;
import com.hycap.dbt.GameState;

public abstract class AttackableBuilding extends Building {
    public float health;

    public void attack(float damage) {
        health -= damage;
        GameState.gameState.addHurtParticle(new Vector2(position.getLeft(), position.getRight()));
        if (health < 0) {
            GameState.gameState.map.removeBuilding(this);
            onDestroy(GameState.gameState);
        }
    }
}
