package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.hycap.dbt.*;
import com.hycap.dbt.enemies.Enemy;
import com.hycap.dbt.projectiles.Projectile;
import com.hycap.dbt.tasks.KillBaseTask;

public abstract class AbstractTowerBuilding extends AttackableBuilding implements Updatable, HasRange, Upgradable {
    public float damage;
    public float reloadTime;
    float timeUntilNextReload;
    public float range;
    public Projectile projectileType;

    public static float riftDamageMult = 1.2f;
    public static float riftReloadMult = 0.8f;
    public static float riftRangeMult = 1.2f;
    public static float upgradeDamageMult = 1.2f;
    public static float upgradeReloadMult = 0.9f;
    public static float upgradeRangeMult = 1.1f;
    public static int baseUpgradeCost = 8;
    public static float upgradeCostMult = 1.5f;
    public int currentUpgradeCost;
    public float damageDealt = 0;
    public int enemiesKilled = 0;
    public int basesDestroyed = 0;
    public int upgradeLevel = 1;

    public abstract String getName();

    @Override
    public abstract Texture getTexture();

    @Override
    public float getRange() {
        return range;
    }

    @Override
    public String getStats() {
        float roundedDamage = Math.round(damage * 10f) / 10f;
        float roundedReloadTime = Math.round(reloadTime * 100f) / 100f;
        float roundedRange = Math.round(range * 100f) / 100f;
        float roundedDamageDealt = Math.round(damageDealt);
        String res = "Damage: " + roundedDamage + "\nReload time: " + roundedReloadTime + "\nRange: " + roundedRange +
                 "\n" + super.getStats() + "\nDamage Dealt: " + roundedDamageDealt + "\nEnemies Killed: " + enemiesKilled;
        if (basesDestroyed > 0) {
            res += "\nBases Destroyed: " + basesDestroyed;
        }
        return res;
    }

    @Override
    public void onCreate(GameState gameState, boolean onRift) {
        super.onCreate(gameState, onRift);
        currentUpgradeCost = baseUpgradeCost;
        if (onRift) {
            damage *= riftDamageMult;
            reloadTime *= riftReloadMult;
            range *= riftRangeMult;
        }
        killBasesInRange();
    }

    void killBasesInRange() {
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
                GameState.gameState.gameStats.incrementEnemyBasesDestroyed();
                ++this.basesDestroyed;
            }
        }
    }

    @Override
    public void onDestroy(GameState gameState) { }

    @Override
    public boolean tryUpgrade(GameState gameState) {
        if (gameState.gold < currentUpgradeCost) {
            return false;
        }
        gameState.gold -= currentUpgradeCost;
        currentUpgradeCost = Math.round(upgradeCostMult * currentUpgradeCost);
        damage *= upgradeDamageMult;
        reloadTime *= upgradeReloadMult;
        range *= upgradeRangeMult;
        ++upgradeLevel;
        killBasesInRange();
        return true;
    }

    @Override
    public int getUpgradeCost() {
        return currentUpgradeCost;
    }

    @Override
    public int getUpgradeLevel() {
        return upgradeLevel;
    }

    @Override
    public void update(float deltaT) {
        timeUntilNextReload -= deltaT;
        if (timeUntilNextReload <= 0) {
            float closestEnemyDist = range;
            Enemy closestEnemy = null;
            for (Enemy enemy : GameState.gameState.enemies) {
                if (enemy.damageToTake >= enemy.health) {
                    continue;
                }
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
        enemy.damageToTake += damage;
        Projectile newProjectile = projectileType.duplicate();
        newProjectile.positionVector = new Vector2(position.getLeft(), position.getRight());
        newProjectile.targetEnemy = enemy;
        newProjectile.damage = damage;
        newProjectile.sourceBuilding = this;
        GameState.gameState.projectiles.add(newProjectile);
    }

    @Override
    public boolean keepActive() {
        return false;
    }

    public abstract Building duplicate();
}
