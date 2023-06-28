package com.hycap.dbt.units;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class KnightUnit extends Unit {
    public static Texture texture;
    float attackTime = 0.5f;
    float attackDamage = 15;
    private float timeUntilNextAttack;

    public KnightUnit(final Vector2 position) {
        super(position);
        attackRange = 0.6f;
        moveSpeed = 1.2f;
        health = 80;
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
        return new KnightUnit(super.position);
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
