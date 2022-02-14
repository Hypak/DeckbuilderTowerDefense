package com.hycap.dbt.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.hycap.dbt.GameState;
import com.hycap.dbt.Map;
import com.hycap.dbt.Updatable;

public class BasicEnemy extends Enemy implements Updatable {
    public static Texture texture;
    final float attackTime = 0.5f;
    final float attackDamage = 5;
    float timeUntilNextAttack;

    public BasicEnemy(Vector2 position) {
        super(position);
        super.attackRange = 0.5f;
        super.moveSpeed = 1f;
        super.health = 15;
        timeUntilNextAttack = 0;
    }

    public void update(float deltaT) {
        timeUntilNextAttack -= deltaT;
        super.update(deltaT);
        if (super.targetDist <= super.attackRange && timeUntilNextAttack <= 0 && super.targetBuilding != null) {
            super.targetBuilding.attack(attackDamage);
            timeUntilNextAttack = attackTime;
        }
    }

    @Override
    public boolean keepActive() {
        return true;
    }

    public Enemy clone() {
        return new BasicEnemy(this.position);
    }

    @Override
    public Texture getTexture() {
        return texture;
    }
}
