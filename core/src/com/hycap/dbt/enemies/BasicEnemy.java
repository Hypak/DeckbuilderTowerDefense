package com.hycap.dbt.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class BasicEnemy extends Enemy {
    public static Texture texture;
    float attackTime = 0.5f;
    float attackDamage = 4;
    private float timeUntilNextAttack;

    public BasicEnemy(final Vector2 position) {
        super(position);
        attackRange = 0.5f;
        moveSpeed = 1f;
        health = 15;
        timeUntilNextAttack = 0;
    }

    @Override
    public void update(final float deltaT) {
        if (health < 0) {
            return;
        }
        timeUntilNextAttack -= deltaT;
        super.update(deltaT);
        if (targetDist <= attackRange && timeUntilNextAttack <= 0 && targetBuilding != null) {
            targetBuilding.attack(attackDamage);
            timeUntilNextAttack = attackTime;
        }
    }

    @Override
    public Enemy clone() {
        return new BasicEnemy(new Vector2(position));
    }

    @Override
    public String getName() {
        return "Basic Enemy";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }
}
