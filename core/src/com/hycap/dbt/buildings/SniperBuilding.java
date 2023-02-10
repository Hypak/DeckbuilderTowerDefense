package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.GameState;
import com.hycap.dbt.projectiles.SniperProjectile;

public class SniperBuilding extends AbstractTowerBuilding {
    public static Texture texture;
    public SniperBuilding() {
        super.health = 75;
        super.range = 7;
        super.reloadTime = 2.5f;
        super.damage = 5;
        super.projectileType = new SniperProjectile();
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
    public void onCreate(GameState gameState, boolean onRift) {
        super.onCreate(gameState, onRift);
    }

    @Override
    public Building duplicate() {
        return new SniperBuilding();
    }
}
