package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.hycap.dbt.*;
import com.hycap.dbt.enemies.Enemy;
import com.hycap.dbt.projectiles.Projectile;
import com.hycap.dbt.tasks.KillBaseTask;

public abstract class AbstractTowerBuilding extends AttackableBuilding implements Updatable, HasRange {
    public float damage;
    public float reloadTime;
    float timeUntilNextReload;
    public float range;
    public Projectile projectileType;

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
                KillBaseTask.finished = true;
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
                attackEnemy(closestEnemy);
                timeUntilNextReload = reloadTime;
            }
        }
    }

    public void attackEnemy(Enemy enemy) {
        Projectile newProjectile = projectileType.duplicate();
        newProjectile.positionVector = new Vector2(position.getLeft(), position.getRight());
        newProjectile.targetEnemy = enemy;
        newProjectile.damage = damage;
        GameState.gameState.projectiles.add(newProjectile);
    }

    @Override
    public boolean keepActive() {
        return false;
    }

    public abstract Building duplicate();
}
