package com.hycap.dbt.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.hycap.dbt.GameState;
import com.hycap.dbt.projectiles.RangedEnemyProjectile;

public class RangedEnemy extends Enemy {
    public static Texture texture;
    public float attackTime;
    public float attackDamage;
    float timeUntilNextAttack;

    public RangedEnemy(Vector2 position) {
        super(position, (float) Math.PI / 6);
        super.moveSpeed = 0.75f;
        super.health = 8;
        attackTime = 0.7f;
        timeUntilNextAttack = 0;
        attackDamage = 4;
        super.attackRange = 3;
    }

    @Override
    public void update(float deltaT) {
        if (super.health < 0) {
            return;
        }
        timeUntilNextAttack -= deltaT;
        super.update(deltaT);
        if (super.targetDist <= super.attackRange && timeUntilNextAttack <= 0 && super.targetBuilding != null) {
            RangedEnemyProjectile projectile = new RangedEnemyProjectile();
            projectile.positionVector = new Vector2(position);
            projectile.targetBuilding = super.targetBuilding;
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
