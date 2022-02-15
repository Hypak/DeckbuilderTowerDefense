package com.hycap.dbt;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.hycap.dbt.enemies.Enemy;
import com.hycap.dbt.enemies.FastEnemy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class EnemyBase implements Updatable {
    public static Texture texture;
    public Pair<Integer> position;
    List<Enemy> enemySpawns;
    float spawnDelay;
    Queue<Enemy> spawnsRemaining;
    float timeUntilNextSpawn;
    int turnsBetweenSpawn;
    int turnsUntilNextSpawn;
    int turnsUntilUpgrade;
    int baseTurnsUntilUpgrade;

    public EnemyBase(Pair<Integer> position, List<Enemy> enemySpawns, float spawnDelay) {
        this.position = position;
        this.enemySpawns = enemySpawns;
        this.spawnDelay = spawnDelay;
        timeUntilNextSpawn = spawnDelay;
        turnsBetweenSpawn = 3;
        turnsUntilNextSpawn = 0;
        baseTurnsUntilUpgrade = 12;
        turnsUntilUpgrade = baseTurnsUntilUpgrade;
    }

    public void startTurn() {
        --turnsUntilUpgrade;
        if (turnsUntilUpgrade <= 0) {
            enemySpawns.add(new FastEnemy(new Vector2(position.getLeft(), position.getRight())));
            turnsUntilUpgrade = baseTurnsUntilUpgrade;
        }
        --turnsUntilNextSpawn;
        if (turnsUntilNextSpawn > 0) {
            return;
        }
        spawnsRemaining = new LinkedList<>();
        for (Enemy enemy : enemySpawns) {
            spawnsRemaining.add(enemy.clone());
        }
        turnsUntilNextSpawn = turnsBetweenSpawn;
    }

    public void update(float deltaT) {
        if (spawnsRemaining.size() < 1) {
            return;
        }
        timeUntilNextSpawn -= deltaT;
        if (timeUntilNextSpawn <= 0) {
            Enemy newEnemy = spawnsRemaining.poll();
            System.out.println(newEnemy.getPosition());
            GameState.gameState.enemies.add(newEnemy);
            timeUntilNextSpawn += spawnDelay;
        }
    }

    @Override
    public boolean keepActive() {
        return spawnsRemaining.size() > 0;
    }

    public Texture getTexture() {
        return texture;
    }
}
