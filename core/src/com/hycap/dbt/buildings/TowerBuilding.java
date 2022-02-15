package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.hycap.dbt.*;
import com.hycap.dbt.enemies.Enemy;

public class TowerBuilding extends AttackableBuilding implements Updatable {
    public static Texture texture;

    float damage = 4;
    float reloadTime = 1.2f;
    float timeUntilNextReload = 0;
    float range = 3.5f;

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
        super.health = 50;
        gameState.updatableBuildings.add(this);
        for (int i = 0; i < GameState.gameState.map.enemyBases.size(); ++i) {
            EnemyBase base = GameState.gameState.map.enemyBases.get(i);
            int xDiff = base.position.getLeft() - position.getLeft();
            int yDiff = base.position.getRight() - position.getRight();
            float squareDist = xDiff*xDiff + yDiff*yDiff;
            if (squareDist <= range) {
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

    @Override
    public Building duplicate() {
        return new TowerBuilding();
    }
}
