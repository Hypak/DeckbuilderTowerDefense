package com.hycap.dbt.units;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class SuperKnightUnit extends Unit {
    public static Texture texture;
    float attackTime = 0.4f;
    float attackDamage = 20;
    private float timeUntilNextAttack;

    public SuperKnightUnit(final Vector2 position) {
        super(position);
        attackRange = 0.8f;
        moveSpeed = 1.9f;
        health = 120;
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
        return new SuperKnightUnit(super.position);
    }

    @Override
    public String getName() {
        return "Super Knight";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }
}
