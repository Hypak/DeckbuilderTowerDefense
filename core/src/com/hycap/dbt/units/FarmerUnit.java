package com.hycap.dbt.units;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.hycap.dbt.enemies.Enemy;

public class FarmerUnit extends Unit {
    public static Texture texture;
    float attackTime = 0.5f;
    float attackDamage = 5;
    private float timeUntilNextAttack;

    public FarmerUnit(final Vector2 position) {
        super(position);
        attackRange = 0.5f;
        moveSpeed = 1f;
        health = 15;
    }

    @Override
    public void update(float deltaT) {
        if (health < 0) {
            return;
        }
        timeUntilNextAttack -= deltaT;
        super.update(deltaT);
        if (targetDist <= attackRange && timeUntilNextAttack <= 0 && targetEnemy != null) {
            targetEnemy.attack(attackDamage);
            timeUntilNextAttack = attackTime;
        }
    }

    @Override
    public Unit clone() {
        return new FarmerUnit(super.position);
    }

    @Override
    public String getName() {
        return "Farmer";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }
}
