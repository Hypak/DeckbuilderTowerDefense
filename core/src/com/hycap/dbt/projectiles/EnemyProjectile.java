package com.hycap.dbt.projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.hycap.dbt.GameState;
import com.hycap.dbt.Updatable;
import com.hycap.dbt.buildings.AttackableBuilding;

public abstract class EnemyProjectile implements Updatable {
    public AttackableBuilding targetBuilding;
    public Vector2 positionVector;
    float projectileSpeed;
    float projectileRadius;
    public float damage;

    @Override
    public void update(final float deltaT) {
        if (targetBuilding == null) {
            GameState.gameState.enemyProjectilesToRemove.add(this);
        }
        final Vector2 target = new Vector2(targetBuilding.getPosition().getLeft(), targetBuilding.getPosition().getRight());
        final Vector2 move = target.sub(positionVector);
        final float targetDist = move.len();
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
