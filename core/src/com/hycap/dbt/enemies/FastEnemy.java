package com.hycap.dbt.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class FastEnemy extends BasicEnemy {
    public static Texture texture;
    public FastEnemy(Vector2 position) {
        super(position);
        super.moveSpeed = 1.8f;
        super.health = 10;
        super.attackTime = 0.35f;
    }

    @Override
    public Enemy clone() {
        return new FastEnemy(new Vector2(position));
    }
    @Override
    public Texture getTexture() {
        return texture;
    }
}
