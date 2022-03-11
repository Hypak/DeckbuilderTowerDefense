package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.hycap.dbt.GameState;
import com.hycap.dbt.enemies.Enemy;
import com.hycap.dbt.projectiles.EarthquakeProjectile;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeBuilding extends AbstractTowerBuilding {
    public static Texture texture;

    public EarthquakeBuilding() {
        super.health = 75;
        super.damage = 8;
        super.reloadTime = 1.8f;
        super.timeUntilNextReload = reloadTime;
        super.range = 1.7f;
        super.projectileType = new EarthquakeProjectile();
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
    public float getRange() {
        return range;
    }

    @Override
    public Building duplicate() {
        return new EarthquakeBuilding();
    }

    @Override
    public void update(float deltaT) {
        timeUntilNextReload -= deltaT;
        if (timeUntilNextReload <= 0) {
            List<Enemy> enemiesToAttack = new ArrayList<>();
            for (Enemy enemy : GameState.gameState.enemies) {
                Vector2 diff = new Vector2(vecPosition).sub(enemy.getPosition());
                float dist = diff.len();
                if (dist < range) {
                    enemiesToAttack.add(enemy);
                }
            }
            if (enemiesToAttack.size() > 0) {
                for (Enemy enemy : enemiesToAttack) {
                    super.attackEnemy(enemy);
                }
                timeUntilNextReload = reloadTime;
            }
        }
    }
}
