package com.hycap.dbt;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.hycap.dbt.enemies.BigEnemy;
import com.hycap.dbt.enemies.Enemy;
import com.hycap.dbt.enemies.FastEnemy;
import com.hycap.dbt.enemies.RangedEnemy;

import java.util.*;

public class EnemyBase implements Updatable {
    public static Texture texture;
    public final Pair<Integer> position;
    final List<Enemy> enemySpawns;
    private final float spawnDelay;
    private Queue<Enemy> spawnsRemaining;
    private float animationTimeUntilNextSpawn;
    private final int turnsBetweenSpawn;
    int turnsUntilNextSpawn;
    int turnsUntilUpgrade;
    private final int baseTurnsUntilUpgrade;

    EnemyBase(final Pair<Integer> position, final List<Enemy> enemySpawns, final float spawnDelay) {
        this.position = position;
        this.enemySpawns = enemySpawns;
        this.spawnDelay = spawnDelay;
        animationTimeUntilNextSpawn = spawnDelay;
        turnsBetweenSpawn = 3;
        turnsUntilNextSpawn = 0;
        baseTurnsUntilUpgrade = 12;
        turnsUntilUpgrade = baseTurnsUntilUpgrade;
    }

    void startTurn() {
        --turnsUntilUpgrade;
        if (turnsUntilUpgrade <= 0) {
            if (new Random().nextBoolean()) {
                enemySpawns.add(new FastEnemy(new Vector2(position.getLeft(), position.getRight())));
            } else {
                enemySpawns.add(0, new RangedEnemy(new Vector2(position.getLeft(), position.getRight())));
            }
            final int addBigAtRadius = 40;
            if (GameState.gameState.map.currentRadius >= addBigAtRadius) {
                enemySpawns.add(0, new BigEnemy(new Vector2(position.getLeft(), position.getRight())));
            }
            turnsUntilUpgrade = baseTurnsUntilUpgrade;
        }
        --turnsUntilNextSpawn;
        if (turnsUntilNextSpawn > 0) {
            return;
        }
        spawnsRemaining = new LinkedList<>();
        for (final Enemy enemy : enemySpawns) {
            spawnsRemaining.add(enemy.clone());
        }
        turnsUntilNextSpawn = turnsBetweenSpawn;
        animationTimeUntilNextSpawn = 0;
    }

    public void update(final float deltaT) {
        if (spawnsRemaining.size() < 1) {
            return;
        }
        animationTimeUntilNextSpawn -= deltaT;
        if (animationTimeUntilNextSpawn <= 0) {
            final Enemy newEnemy = spawnsRemaining.poll();
            GameState.gameState.enemies.add(newEnemy);
            animationTimeUntilNextSpawn += spawnDelay;
        }
    }

    @Override
    public boolean keepActive() {
        return !spawnsRemaining.isEmpty();
    }

    public Texture getTexture() {
        return texture;
    }
}
