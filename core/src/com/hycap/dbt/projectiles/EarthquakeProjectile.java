package com.hycap.dbt.projectiles;

import com.badlogic.gdx.graphics.Texture;

public class EarthquakeProjectile extends Projectile {
    public static Texture texture;

    public EarthquakeProjectile() {
        projectileRadius = 0.4f;
        projectileSpeed = 4;
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
        return projectileRadius;
    }
}
