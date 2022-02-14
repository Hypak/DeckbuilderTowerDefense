package com.hycap.dbt;

import com.badlogic.gdx.math.Vector2;
import com.hycap.dbt.buildings.Building;
import com.hycap.dbt.buildings.CentralBuilding;
import com.hycap.dbt.enemies.BasicEnemy;
import com.hycap.dbt.enemies.Enemy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Map {
    public final int WIDTH = 101;
    public final int HEIGHT = 101;
    final Building[][] buildings;
    final List<Pair<Integer>> buildingCoords;
    final List<EnemyBase> enemyBases;
    final List<EnemyBase> activeEnemyBases;
    final int enemyBaseCount = 100;
    final int noBaseRadius = 7;
    int currentRadius = 2;

    public Map() {
        buildings = new Building[WIDTH][HEIGHT];
        buildings[WIDTH / 2][HEIGHT / 2] = new CentralBuilding();
        buildingCoords = new LinkedList<>();
        buildingCoords.add(new Pair<>(WIDTH / 2, HEIGHT / 2));
        enemyBases = new ArrayList<>();
        activeEnemyBases = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < enemyBaseCount; ++i) {
            int x = random.nextInt(WIDTH);
            int y = random.nextInt(HEIGHT);
            if (Math.abs(x - WIDTH / 2) <= noBaseRadius && Math.abs(y - HEIGHT / 2) <= noBaseRadius) {
                --i;
                continue;
            }
            List<Enemy> enemySpawns = new ArrayList<>();
            enemySpawns.add(new BasicEnemy(new Vector2(x, y)));
            EnemyBase newBase = new EnemyBase(new Pair<>(x, y), enemySpawns, 1.5f);
            enemyBases.add(newBase);
        }
    }

    public void newTurn() {
        ++currentRadius;
        for (EnemyBase base : enemyBases) {
            if (isInRadius(base.position.getLeft(), base.position.getRight())) {
                base.startTurn();
                if (!GameState.gameState.updatables.contains(base)) {
                    GameState.gameState.updatables.add(base);
                }
            }
        }
    }

    public boolean isInRadius(int x, int y) {
        int xRad = Math.abs(x - WIDTH / 2);
        int yRad = Math.abs(y - HEIGHT / 2);
        return Math.max(xRad, yRad) <= currentRadius;
    }

    public Building getBuilding(int x, int y) {
        if (x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT) {
            return null;
        }
        return buildings[x][y];
    }

    public boolean canPlaceBuilding(int x, int y) {
        if (x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT) {
            return false;
        }
        if (this.buildings[x][y] != null) {
            return false;
        }
        if (x - 1 >= 0 && this.buildings[x-1][y] != null) {
            return true;
        }
        if (x + 1 < WIDTH && this.buildings[x+1][y] != null) {
            return true;
        }
        if (y - 1 >= 0 && this.buildings[x][y-1] != null) {
            return true;
        }
        if (y + 1 < HEIGHT && this.buildings[x][y+1] != null) {
            return true;
        }
        return false;
    }

    public boolean placeBuilding(Building building, int x, int y) {
        if (!canPlaceBuilding(x, y)) {
            return false;
        }
        this.buildings[x][y] = building;
        this.buildingCoords.add(new Pair<>(x, y));
        return true;
    }

    public void removeBuilding(int x, int y) {
        this.buildings[x][y] = null;
    }

    public void removeBuilding(Building b) {
        for (int x = 0; x < WIDTH; ++x) {
            for (int y = 0; y < HEIGHT; ++y) {
                if (buildings[x][y] != null && buildings[x][y].equals(b)) {
                    removeBuilding(x, y);
                }
            }
        }
    }

    public List<Pair<Integer>> getBuildingCoords() {
        return buildingCoords;
    }
}
