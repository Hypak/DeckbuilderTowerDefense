package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.hycap.dbt.ActionOnStartTurn;
import com.hycap.dbt.GameState;
import com.hycap.dbt.Pair;
import com.hycap.dbt.Updatable;
import com.hycap.dbt.enemies.Enemy;

import java.util.ArrayList;
import java.util.List;

public class BombBuilding extends Building implements CannotPath, HasRange, Updatable, ActionOnStartTurn {
    final float baseTriggerRange = 2;
    float triggerRange;
    final float baseDamageRange = 4.5f;
    float damageRange;
    final float baseDamage = 15;
    float damage;
    boolean charged = true;

    public static Texture texture;
    public static Texture noChargeTexture;

    @Override
    public float getRange() {
        return triggerRange;
    }

    @Override
    public String getName() {
        return "Bomb";
    }

    @Override
    public String getInfo() {
        return "Explodes shortly after an enemy enters its range.";
    }

    @Override
    public String getStats() {
        String res = "Trigger Range: " + triggerRange;
        res += "\nBlast Radius: " + damageRange;
        res += "\nDamage:" + damage;
        return res;
    }

    @Override
    public Texture getTexture() {
        if (charged || !GameState.gameState.animating) {
            return texture;
        } else {
            return noChargeTexture;
        }
    }

    @Override
    public void onCreate(GameState gameState, boolean onRift) {
        if (onRift) {
            triggerRange = 2 * baseTriggerRange;
            damageRange = 2 * baseDamageRange;
            damage = 2 * baseDamage;
        } else {
            triggerRange = baseTriggerRange;
            damageRange = baseDamageRange;
            damage = baseDamage;
        }
        super.onCreate(gameState, onRift);
    }

    @Override
    public void onDestroy(GameState gameState) {

    }

    @Override
    public Building duplicate() {
        return new BombBuilding();
    }

    @Override
    public void update(final float deltaT) {
        if (!charged) {
            return;
        }
        float closestEnemyDist = triggerRange;
        Enemy closestEnemy = null;
        for (final Enemy enemy : GameState.gameState.enemies) {
            if (enemy.damageToTake >= enemy.health || enemy.invisSeconds > 0) {
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
            activate();
        }
    }

    private void activate() {
        charged = false;
        List<Enemy> enemiesToAttack = new ArrayList<>(1);
        for (final Enemy enemy : GameState.gameState.enemies) {
            final Vector2 diff = new Vector2(vecPosition).sub(enemy.getPosition());
            final float dist = diff.len();
            if (dist <= damageRange) {
                enemiesToAttack.add(enemy);
            }
        }
        for (Enemy enemy : enemiesToAttack) {
            enemy.attack(damage);
        }
    }

    @Override
    public boolean keepActive() {
        return false;
    }

    @Override
    public void startTurn() {
        charged = true;
    }
}
