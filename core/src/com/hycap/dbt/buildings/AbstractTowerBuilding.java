package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.hycap.dbt.*;
import com.hycap.dbt.enemies.Enemy;
import com.hycap.dbt.projectiles.Projectile;
import com.hycap.dbt.tasks.KillBaseTask;

public abstract class AbstractTowerBuilding extends AttackableBuilding implements Updatable, HasRange, Upgradable {
    float damage;
    float reloadTime;
    float timeUntilNextReload;
    float range;
    Projectile projectileType;

    private static final float riftDamageMult = 1.2f;
    private static final float riftReloadMult = 0.8f;
    public static final float riftRangeMult = 1.2f;
    private static final float upgradeDamageMult = 1.2f;
    private static final float upgradeReloadMult = 0.9f;
    private static final float upgradeRangeMult = 1.1f;
    private static final int baseUpgradeCost = 8;
    private static final float upgradeCostMult = 1.5f;
    private int currentUpgradeCost;
    public float damageDealt = 0;
    public int enemiesKilled = 0;
    private int basesDestroyed = 0;
    private int upgradeLevel = 1;

    public abstract String getName();

    @Override
    public abstract Texture getTexture();

    @Override
    public float getRange() {
        return range;
    }

    @Override
    public String getStats() {
        final float roundedDamage = Math.round(damage * 10f) / 10f;
        final float roundedReloadTime = Math.round(reloadTime * 100f) / 100f;
        final float roundedRange = Math.round(range * 100f) / 100f;
        final float roundedDamageDealt = Math.round(damageDealt);
        String res = "Damage: " + roundedDamage + "\nReload time: " + roundedReloadTime + "\nRange: " + roundedRange +
                 "\n" + super.getStats() + "\nDamage Dealt: " + roundedDamageDealt + "\nEnemies Killed: " + enemiesKilled;
        if (basesDestroyed > 0) {
            res += "\nBases Destroyed: " + basesDestroyed;
        }
        return res;
    }

    @Override
    public void onCreate(final GameState gameState, final boolean onRift) {
        super.onCreate(gameState, onRift);
        currentUpgradeCost = baseUpgradeCost;
        if (onRift) {
            damage *= riftDamageMult;
            reloadTime *= riftReloadMult;
            range *= riftRangeMult;
        }
        killBasesInRange();
    }

    private void killBasesInRange() {
        for (int i = 0; i < GameState.gameState.map.enemyBaseManager.enemyBases.size(); ++i) {
            final EnemyBase base = GameState.gameState.map.enemyBaseManager.enemyBases.get(i);
            final int xDiff = base.position.getLeft() - position.getLeft();
            final int yDiff = base.position.getRight() - position.getRight();
            final float squareDist = xDiff*xDiff + yDiff*yDiff;
            if (squareDist <= range * range) {
                GameState.gameState.addHurtParticle(new Vector2(base.position.getLeft(), base.position.getRight()));
                GameState.gameState.map.enemyBaseManager.destroyEnemyBase(base);
                --i;
                KillBaseTask.finished = true;
                GameState.gameState.gameStats.incrementEnemyBasesDestroyed();
                ++basesDestroyed;
            }
        }
    }

    @Override
    public void onDestroy(final GameState gameState) { }

    @Override
    public boolean tryUpgrade(final GameState gameState) {
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
    public void update(final float deltaT) {
        timeUntilNextReload -= deltaT;
        if (timeUntilNextReload <= 0) {
            float closestEnemyDist = range;
            Enemy closestEnemy = null;
            for (final Enemy enemy : GameState.gameState.enemies) {
                if (enemy.damageToTake >= enemy.health) {
                    continue;
                }
                final Vector2 diff = new Vector2(vecPosition).sub(enemy.getPosition());
                final float dist = diff.len();
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

    void attackEnemy(final Enemy enemy) {
        enemy.damageToTake += damage;
        final Projectile newProjectile = projectileType.duplicate();
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
