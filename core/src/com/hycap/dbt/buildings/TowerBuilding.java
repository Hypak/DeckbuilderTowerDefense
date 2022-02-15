package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.hycap.dbt.GameState;
import com.hycap.dbt.Pair;
import com.hycap.dbt.Updatable;
import com.hycap.dbt.enemies.Enemy;

public class TowerBuilding implements AttackableBuilding, Updatable {
    public static Texture texture;
    float health = 50;
    float damage = 4;
    float reloadTime = 1.2f;
    float timeUntilNextReload = 0;
    float range = 3.5f;

    Pair<Integer> position;
    Vector2 vecPosition;

    @Override
    public String getName() {
        return "Tower";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public void onCreate(GameState gameState) {
        gameState.updatableBuildings.add(this);
    }

    @Override
    public void onDestroy(GameState gameState) {
        GameState.gameState.updatableBuildings.remove(this);
    }

    @Override
    public void attack(float damage) {
        health -= damage;
        GameState.gameState.addHurtParticle(new Vector2(position.getLeft(), position.getRight()));
        if (health < 0) {
            GameState.gameState.map.removeBuilding(this);
            onDestroy(GameState.gameState);
        }
    }

    @Override
    public void update(float deltaT) {
        timeUntilNextReload -= deltaT;
        if (timeUntilNextReload <= 0) {
            float closestEnemyDist = range;
            Enemy closestEnemy = null;
            for (Enemy enemy : GameState.gameState.enemies) {
                Vector2 diff = new Vector2(vecPosition).sub(enemy.getPosition());
                float dist = diff.len();
                if (dist < closestEnemyDist) {
                    closestEnemyDist = dist;
                    closestEnemy = enemy;
                }
            }
            if (closestEnemy != null) {
                closestEnemy.attack(damage);
                timeUntilNextReload = reloadTime;
            }
        }
    }

    @Override
    public boolean keepActive() {
        return false;
    }

    @Override
    public Building duplicate() {
        return new TowerBuilding();
    }

    @Override
    public void setPosition(Pair<Integer> position) {
        this.position = position;
        this.vecPosition = new Vector2(position.getLeft(), position.getRight());
    }

    @Override
    public Pair<Integer> getPosition() {
        return position;
    }
}
