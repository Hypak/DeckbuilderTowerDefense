package com.hycap.dbt.projectiles;

import com.badlogic.gdx.graphics.Texture;

public class EarthquakeProjectile extends Projectile {
    public static Texture texture;

    public EarthquakeProjectile() {
        super.projectileRadius = 0.4f;
        super.projectileSpeed = 4;
    }

    @Override
    public Projectile duplicate() {
        return new EarthquakeProjectile();
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
