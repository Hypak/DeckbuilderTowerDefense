package com.hycap.dbt.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class RangedEnemy extends BasicEnemy {
    public static Texture texture;
    public RangedEnemy(Vector2 position) {
        super(position);
        super.moveSpeed = 0.75f;
        super.health = 8;
        super.attackTime = 0.7f;
        super.attackDamage = 4;
        super.attackRange = 3;
    }

    @Override
    public Enemy clone() {
        return new RangedEnemy(new Vector2(position));
    }
    @Override
    public Texture getTexture() {
        return texture;
    }

}
