package com.hycap.dbt.projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.hycap.dbt.GameState;
import com.hycap.dbt.Updatable;
import com.hycap.dbt.enemies.Enemy;

public abstract class Projectile implements Updatable {
    public Enemy targetEnemy;
    public Vector2 positionVector;
    public float projectileSpeed;
    public float projectileRadius;
    public float damage;

    @Override
    public void update(float deltaT) {
        if (targetEnemy == null) {
            GameState.gameState.projectilesToRemove.add(this);
        }
        Vector2 move = new Vector2(targetEnemy.getPosition()).sub(positionVector);
        float targetDist = move.len();
        if (targetDist > projectileRadius) {
            move.scl(1 / targetDist);
            positionVector.add(move.scl(deltaT * projectileSpeed));
        } else {
            targetEnemy.attack(damage);
            GameState.gameState.projectilesToRemove.add(this);
        }
    }

    @Override
    public boolean keepActive() {
        return false;
    }

    public abstract Projectile duplicate();

    public abstract Texture getTexture();

    public abstract float getTextureScale();
}
