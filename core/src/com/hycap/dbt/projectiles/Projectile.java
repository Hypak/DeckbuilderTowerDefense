package com.hycap.dbt.projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.hycap.dbt.GameState;
import com.hycap.dbt.UIManager;
import com.hycap.dbt.Updatable;
import com.hycap.dbt.buildings.AbstractTowerBuilding;
import com.hycap.dbt.enemies.Enemy;

public abstract class Projectile implements Updatable {
    public Enemy targetEnemy;
    public Vector2 positionVector;
    float projectileSpeed;
    float projectileRadius;
    public float damage;
    public AbstractTowerBuilding sourceBuilding = null;

    @Override
    public void update(final float deltaT) {
        if (targetEnemy == null || targetEnemy.invisSeconds > 0) {
            GameState.gameState.projectilesToRemove.add(this);
        }
        if (targetEnemy.invisSeconds > 0) {
            // Stops enemies becoming permanently invincible
            targetEnemy.damageToTake -= damage;
        }
        final Vector2 move = new Vector2(targetEnemy.getPosition()).sub(positionVector);
        final float targetDist = move.len();
        if (targetDist > projectileRadius) {
            move.scl(1 / targetDist);
            positionVector.add(move.scl(deltaT * projectileSpeed));
        } else {
            if (sourceBuilding != null) {
                if (targetEnemy.health <= damage) {
                    sourceBuilding.damageDealt += targetEnemy.health;
                    ++sourceBuilding.enemiesKilled;
                } else {
                    sourceBuilding.damageDealt += damage;
                }
                UIManager.updateInfoIfSelected(sourceBuilding);
            }
            targetEnemy.attack(damage);
            targetEnemy.damageToTake -= damage;
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
