package com.hycap.dbt.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class FastEnemy extends BasicEnemy {
    public static Texture texture;
    public FastEnemy(Vector2 position) {
        super(position);
        moveSpeed = 1.8f;
        health = 10;
        attackTime = 0.35f;
    }

    @Override
    public Enemy clone() {
        return new FastEnemy(position);
    }
    @Override
    public Texture getTexture() {
        return texture;
    }
}
