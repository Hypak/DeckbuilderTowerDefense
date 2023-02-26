package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.projectiles.SniperProjectile;

public class SniperBuilding extends AbstractTowerBuilding {
    public static Texture texture;
    public SniperBuilding() {
        health = 75;
        range = 7;
        reloadTime = 2.5f;
        damage = 5;
        projectileType = new SniperProjectile();
    }

    @Override
    public String getName() {
        return "Sniper";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public String getInfo() {
        return "Sniper towers deal damage slowly at long range.";
    }

    @Override
    public Building duplicate() {
        return new SniperBuilding();
    }
}
