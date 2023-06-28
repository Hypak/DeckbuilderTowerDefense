package com.hycap.dbt.buildings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.hycap.dbt.Attackable;
import com.hycap.dbt.GameState;
import com.hycap.dbt.UIManager;

public abstract class AttackableBuilding extends Building implements Attackable {
    private static final Sound attackSound;
    private static final Sound destroySound;
    static {
        attackSound = Gdx.audio.newSound(Gdx.files.internal("BuildingHit.ogg"));
        destroySound = Gdx.audio.newSound(Gdx.files.internal("BuildingDestroy.ogg"));
    }

    float health;
    private float maxHealth;
    private float healthRepaired = 0;
    private boolean destroyed = false;

    @Override
    public void onCreate(final GameState gameState, final boolean onRift) {
        super.onCreate(gameState, onRift);
        if (onRift) {
            health *= 1.5;
        }
        maxHealth = health;
    }

    @Override
    public String getStats() {
        String res = "Max Health: " + Math.round(maxHealth);
        if (healthRepaired > 0) {
            res += "\nTotal Health Repaired: " + Math.round(healthRepaired);
        }
        return res;
    }

    @Override
    public void attack(final float damage) {
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

    public void newTurn() {
        if (maxHealth > health) {
            healthRepaired += (maxHealth - health);
            UIManager.updateInfoIfSelected(this);
        }
        health = maxHealth;
    }

    @Override
    public Vector2 getVecPosition() {
        return new Vector2(position.getLeft(), position.getRight());
    }
}
