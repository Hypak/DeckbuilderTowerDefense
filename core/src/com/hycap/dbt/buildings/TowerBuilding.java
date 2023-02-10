package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.*;
import com.hycap.dbt.projectiles.TowerProjectile;

public class TowerBuilding extends AbstractTowerBuilding implements Updatable {
    public static Texture texture;
    public TowerBuilding() {
        super.range = 3.5f;
        super.damage = 4;
        super.reloadTime = 1.2f;
        super.timeUntilNextReload = 0;
        super.health = 50;
        super.projectileType = new TowerProjectile();
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
    public void onCreate(GameState gameState, boolean onRift) {
        super.onCreate(gameState, onRift);
    }
    @Override
    public Building duplicate() {
        return new TowerBuilding();
    }
}
