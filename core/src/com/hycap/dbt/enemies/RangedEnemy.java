package com.hycap.dbt.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.hycap.dbt.GameState;
import com.hycap.dbt.projectiles.RangedEnemyProjectile;

public class RangedEnemy extends Enemy {
    public static Texture texture;
    private final float attackTime;
    private final float attackDamage;
    private float timeUntilNextAttack;

    public RangedEnemy(final Vector2 position) {
        super(position, (float) Math.PI / 4);
        super.buildingTargetPriority = Neutral.neutral;
        moveSpeed = 0.75f;
        health = 8;
        attackTime = 0.7f;
        timeUntilNextAttack = 0;
        attackDamage = 4;
        attackRange = 3;
    }

    @Override
    public void update(final float deltaT) {
        if (health < 0) {
            return;
        }
        timeUntilNextAttack -= deltaT;
        super.update(deltaT);
        if (targetDist <= attackRange && timeUntilNextAttack <= 0 && targetBuilding != null) {
            final RangedEnemyProjectile projectile = new RangedEnemyProjectile();
            projectile.positionVector = new Vector2(position);
            projectile.targetBuilding = targetBuilding;
            projectile.damage = attackDamage;
            GameState.gameState.enemyProjectiles.add(projectile);
            timeUntilNextAttack = attackTime;
        }
    }

    @Override
    public Enemy clone() {
        return new RangedEnemy(new Vector2(position));
    }

    @Override
    public String getName() {
        return "Ranged Enemy";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

}
