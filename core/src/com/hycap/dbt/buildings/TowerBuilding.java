package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.projectiles.TowerProjectile;

public class TowerBuilding extends AbstractTowerBuilding {
    public static Texture texture;
    public TowerBuilding() {
        range = 3.5f;
        damage = 4;
        reloadTime = 1.2f;
        timeUntilNextReload = 0;
        health = 50;
        projectileType = new TowerProjectile();
    }

    @Override
    public String getName() {
        return "Tower";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public String getInfo() {
        return "Towers attack enemies at medium range.";
    }

    @Override
    public Building duplicate() {
        return new TowerBuilding();
    }
}
