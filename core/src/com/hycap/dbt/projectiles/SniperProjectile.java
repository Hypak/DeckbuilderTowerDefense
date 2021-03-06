package com.hycap.dbt.projectiles;

import com.badlogic.gdx.graphics.Texture;

public class SniperProjectile extends Projectile {
    public static Texture texture;

    public SniperProjectile() {
        super.projectileSpeed = 10;
        super.projectileRadius = 0.5f;
    }

    @Override
    public Projectile duplicate() {
        return new SniperProjectile();
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
