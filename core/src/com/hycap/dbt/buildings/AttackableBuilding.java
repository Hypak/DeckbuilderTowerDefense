package com.hycap.dbt.buildings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.hycap.dbt.GameState;

public abstract class AttackableBuilding extends Building {
    static Sound attackSound;
    static Sound destroySound;
    static {
        attackSound = Gdx.audio.newSound(Gdx.files.internal("BuildingHit.ogg"));
        destroySound = Gdx.audio.newSound(Gdx.files.internal("BuildingDestroy.ogg"));
    }

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
            destroySound.play();
        }
        attackSound.play();
    }
}
