package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.GameState;
import com.hycap.dbt.Updatable;
import com.hycap.dbt.enemies.Enemy;

import java.util.ArrayList;
import java.util.List;

public class SpikesBuilding extends Building implements Updatable {
    public static Texture texture;

    public float damage = 2;
    public float reloadTime = 0.5f;
    float timeUntilNextReload = 0;
    @Override
    public void update(float deltaT) {
        timeUntilNextReload -= deltaT;
        if (timeUntilNextReload <= 0) {
            List<Enemy> enemiesToAttack = new ArrayList<>();
            for (Enemy enemy : GameState.gameState.enemies) {
                float xDiff = Math.abs(enemy.getX() - super.vecPosition.x);
                float yDiff = Math.abs(enemy.getY() - super.vecPosition.y);
                if (Math.max(xDiff, yDiff) <= 1f) {
                    enemiesToAttack.add(enemy);
                    timeUntilNextReload = reloadTime;
                }
            }
            for (Enemy enemy : enemiesToAttack) {
                enemy.attack(damage);
            }
        }
    }

    @Override
    public boolean keepActive() {
        return false;
    }

    @Override
    public String getName() {
        return "Spikes";
    }

    @Override
    public String getInfo() {
        return "Spikes damage enemies that walk over them.";
    }

    @Override
    public String getStats() {
        return "Damage: " + damage + "\nReload time: " + reloadTime;
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public void onCreate(GameState gameState) {
        GameState.gameState.updatableBuildings.add(this);
    }

    @Override
    public void onDestroy(GameState gameState) {
        GameState.gameState.updatableBuildings.remove(this);
    }

    @Override
    public Building duplicate() {
        return new SpikesBuilding();
    }
}
