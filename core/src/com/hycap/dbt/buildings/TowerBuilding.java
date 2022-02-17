package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.hycap.dbt.*;
import com.hycap.dbt.enemies.Enemy;

public class TowerBuilding extends AbstractTowerBuilding implements Updatable {
    public static Texture texture;
    public TowerBuilding() {
        super.range = 3.5f;
        super.damage = 4;
        super.reloadTime = 1.2f;
        super.timeUntilNextReload = 0;
        super.health = 50;
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
    public void onCreate(GameState gameState) {
        super.onCreate(gameState);
    }

    @Override
    public Building duplicate() {
        return new TowerBuilding();
    }
}
