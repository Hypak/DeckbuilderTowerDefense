package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.GameState;
import com.hycap.dbt.Updatable;
import com.hycap.dbt.enemies.Enemy;

import java.util.ArrayList;
import java.util.Collection;

public class SpikesBuilding extends Building implements Updatable, CannotPath {
    public static Texture texture;

    private float damage = 4;
    private float reloadTime = 0.5f;
    private float timeUntilNextReload = 0;
    @Override
    public void update(final float deltaT) {
        timeUntilNextReload -= deltaT;
        if (timeUntilNextReload <= 0) {
            final Collection<Enemy> enemiesToAttack = new ArrayList<>();
            for (final Enemy enemy : GameState.gameState.enemies) {
                final float xDiff = Math.abs(enemy.getX() - vecPosition.x);
                final float yDiff = Math.abs(enemy.getY() - vecPosition.y);
                if (Math.max(xDiff, yDiff) <= 1f) {
                    enemiesToAttack.add(enemy);
                    timeUntilNextReload = reloadTime;
                }
            }
            for (final Enemy enemy : enemiesToAttack) {
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
    public void onCreate(final GameState gameState, final boolean onRift) {
        if (onRift) {
            damage *= 2;
            reloadTime *= 0.5f;
        }
        super.onCreate(gameState, onRift);
    }

    @Override
    public void onDestroy(final GameState gameState) { }

    @Override
    public Building duplicate() {
        return new SpikesBuilding();
    }
}
