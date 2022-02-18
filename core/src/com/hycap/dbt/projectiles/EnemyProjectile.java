package com.hycap.dbt.projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.hycap.dbt.GameState;
import com.hycap.dbt.Updatable;
import com.hycap.dbt.buildings.AttackableBuilding;
import com.hycap.dbt.buildings.Building;
import com.hycap.dbt.enemies.Enemy;

public abstract class EnemyProjectile implements Updatable {
    public AttackableBuilding targetBuilding;
    public Vector2 positionVector;
    public float projectileSpeed;
    public float projectileRadius;
    public float damage;

    @Override
    public void update(float deltaT) {
        if (targetBuilding == null) {
            GameState.gameState.enemyProjectilesToRemove.add(this);
        }
        Vector2 target = new Vector2(targetBuilding.getPosition().getLeft(), targetBuilding.getPosition().getRight());
        Vector2 move = target.sub(positionVector);
        float targetDist = move.len();
        if (targetDist > projectileRadius) {
            move.scl(1 / targetDist);
            positionVector.add(move.scl(deltaT * projectileSpeed));
        } else {
            targetBuilding.attack(damage);
            GameState.gameState.enemyProjectilesToRemove.add(this);
        }
    }

    @Override
    public boolean keepActive() {
        return true;
    }

    public abstract EnemyProjectile duplicate();

    public abstract Texture getTexture();

    public abstract float getTextureScale();
}
