package com.hycap.dbt.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.hycap.dbt.GameState;
import com.hycap.dbt.Map;
import com.hycap.dbt.Updatable;

public class BasicEnemy extends Enemy {
    public static Texture texture;
    float attackTime = 0.5f;
    float attackDamage = 4;
    float timeUntilNextAttack;

    public BasicEnemy(Vector2 position) {
        super(position);
        super.attackRange = 0.5f;
        super.moveSpeed = 1f;
        super.health = 15;
        timeUntilNextAttack = 0;
    }

    @Override
    public void update(float deltaT) {
        if (super.health < 0) {
            return;
        }
        timeUntilNextAttack -= deltaT;
        super.update(deltaT);
        if (super.targetDist <= super.attackRange && timeUntilNextAttack <= 0 && super.targetBuilding != null) {
            super.targetBuilding.attack(attackDamage);
            timeUntilNextAttack = attackTime;
        }
    }

    public Enemy clone() {
        return new BasicEnemy(new Vector2(this.position));
    }

    @Override
    public Texture getTexture() {
        return texture;
    }
}
