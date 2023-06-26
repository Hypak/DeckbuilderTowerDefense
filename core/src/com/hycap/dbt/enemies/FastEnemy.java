package com.hycap.dbt.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class FastEnemy extends BasicEnemy {
    public static Texture texture;
    public FastEnemy(final Vector2 position) {
        super(position);
        super.buildingTargetPriority = PreferEconomy.preferEconomy;
        moveSpeed = 1.8f;
        health = 10;
        attackTime = 0.35f;
    }

    @Override
    public Enemy clone() {
        return new FastEnemy(new Vector2(position));
    }

    @Override
    public String getName() {
        return "Fast Enemy";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }
}
