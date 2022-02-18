package com.hycap.dbt.projectiles;

import com.badlogic.gdx.graphics.Texture;

public class RangedEnemyProjectile extends EnemyProjectile {
    public static Texture texture;

    public RangedEnemyProjectile() {
        super.projectileRadius = 0.4f;
        super.projectileSpeed = 2.5f;
    }

    @Override
    public EnemyProjectile duplicate() {
        return new RangedEnemyProjectile();
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public float getTextureScale() {
        return super.projectileRadius;
    }
}
