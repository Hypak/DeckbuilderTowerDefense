package com.hycap.dbt.projectiles;

import com.badlogic.gdx.graphics.Texture;

public class TowerProjectile extends Projectile {
    public static Texture texture;

    public TowerProjectile() {
        super.projectileRadius = 0.5f;
        super.projectileSpeed = 6;
    }

    @Override
    public TowerProjectile duplicate() {
        return new TowerProjectile();
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
