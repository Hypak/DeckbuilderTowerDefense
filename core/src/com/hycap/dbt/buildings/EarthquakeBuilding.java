package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.hycap.dbt.GameState;
import com.hycap.dbt.enemies.Enemy;
import com.hycap.dbt.projectiles.EarthquakeProjectile;

import java.util.ArrayList;
import java.util.Collection;

public class EarthquakeBuilding extends AbstractTowerBuilding {
    public static Texture texture;

    public EarthquakeBuilding() {
        health = 75;
        damage = 8;
        reloadTime = 1.8f;
        timeUntilNextReload = reloadTime;
        range = 1.7f;
        projectileType = new EarthquakeProjectile();
    }

    @Override
    public String getName() {
        return "Earthquake";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public String getInfo() {
        return "Earthquake buildings deal damage to lots of enemies at short range.";
    }

    @Override
    public Building duplicate() {
        return new EarthquakeBuilding();
    }

    @Override
    public void update(final float deltaT) {
        timeUntilNextReload -= deltaT;
        if (timeUntilNextReload <= 0) {
            final Collection<Enemy> enemiesToAttack = new ArrayList<>();
            for (final Enemy enemy : GameState.gameState.enemies) {
                final Vector2 position = enemy.getPosition();
                final Vector2 diff = new Vector2(vecPosition).sub(position);
                final float dist = diff.len();
                if (dist < range) {
                    enemiesToAttack.add(enemy);
                }
            }
            if (!enemiesToAttack.isEmpty()) {
                for (final Enemy enemy : enemiesToAttack) {
                    attackEnemy(enemy);
                }
                timeUntilNextReload = reloadTime;
            }
        }
    }
}
