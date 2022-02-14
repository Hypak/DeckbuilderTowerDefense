package com.hycap.dbt;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.enemies.Enemy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class EnemyBase implements Updatable {
    public static Texture texture;
    Pair<Integer> position;
    List<Enemy> enemySpawns;
    float spawnDelay;
    Queue<Enemy> spawnsRemaining;
    float timeUntilNextSpawn;

    public EnemyBase(Pair<Integer> position, List<Enemy> enemySpawns, float spawnDelay) {
        this.position = position;
        this.enemySpawns = enemySpawns;
        this.spawnDelay = spawnDelay;
        timeUntilNextSpawn = spawnDelay;
    }

    public void startTurn() {
        spawnsRemaining = new LinkedList<>();
        for (Enemy enemy : enemySpawns) {
            spawnsRemaining.add(enemy.clone());
        }
    }

    public void update(float deltaT) {
        if (spawnsRemaining.size() < 1) {
            return;
        }
        timeUntilNextSpawn -= deltaT;
        if (timeUntilNextSpawn <= 0) {
            Enemy newEnemy = spawnsRemaining.poll();
            GameState.gameState.updatablesToAdd.add((Updatable)newEnemy);
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
