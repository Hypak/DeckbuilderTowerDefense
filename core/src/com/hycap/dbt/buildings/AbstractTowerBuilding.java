package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.hycap.dbt.*;
import com.hycap.dbt.enemies.Enemy;

public abstract class AbstractTowerBuilding extends AttackableBuilding implements Updatable, HasRange {
    float damage;
    float reloadTime;
    float timeUntilNextReload;
    float range;

    public abstract String getName();

    @Override
    public abstract Texture getTexture();

    @Override
    public float getRange() {
        return range;
    }

    @Override
    public String getStats() {
        return "Damage: " + damage + "\nReload time: " + reloadTime + "\nRange: " + range + "\n" + super.getStats();
    }

    @Override
    public void onCreate(GameState gameState) {
        super.onCreate(gameState);
        gameState.updatableBuildings.add(this);
        for (int i = 0; i < GameState.gameState.map.enemyBases.size(); ++i) {
            EnemyBase base = GameState.gameState.map.enemyBases.get(i);
            int xDiff = base.position.getLeft() - position.getLeft();
            int yDiff = base.position.getRight() - position.getRight();
            float squareDist = xDiff*xDiff + yDiff*yDiff;
            if (squareDist <= range * range) {
                GameState.gameState.addHurtParticle(new Vector2(base.position.getLeft(), base.position.getRight()));
                GameState.gameState.map.destroyEnemyBase(base);
                --i;
            }
        }
    }

    @Override
    public void onDestroy(GameState gameState) {
        GameState.gameState.updatableBuildings.remove(this);
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

    public abstract Building duplicate();
}
