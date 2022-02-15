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
    public final List<EnemyBase> enemyBases;
    final List<EnemyBase> activeEnemyBases;
    final int enemyBaseCount = 100;
    final int noBaseRadius = 13;
    final List<Integer> setBaseRadii;
    int currentRadius = 2;

    public Map() {
        buildings = new Building[WIDTH][HEIGHT];
        Building centralBuilding = new CentralBuilding();
        centralBuilding.setPosition(new Pair<Integer>(WIDTH / 2, HEIGHT / 2));
        centralBuilding.onCreate(GameState.gameState);
        buildings[WIDTH / 2][HEIGHT / 2] = centralBuilding;
        buildingCoords = new LinkedList<>();
        buildingCoords.add(new Pair<>(WIDTH / 2, HEIGHT / 2));

        setBaseRadii = new ArrayList<>();
        setBaseRadii.add(8);
        setBaseRadii.add(10);
        setBaseRadii.add(12);
        enemyBases = new ArrayList<>();
        activeEnemyBases = new ArrayList<>();
        generateBases();
    }

    void generateBases() {
        Random random = new Random();
        for (int radius : setBaseRadii) {
            boolean onX = random.nextBoolean();
            boolean positive = random.nextBoolean();
            int x, y;
            if (positive) {
                x = radius;
            } else {
                x = -radius;
            }
            y = random.nextInt(2 * radius + 1) - radius;
            if (!onX) {
                // Swap x and y
                int tmp = x;
                x = y;
                y = tmp;
            }
            x += WIDTH / 2;
            y += HEIGHT / 2;
            generateEnemyBaseAt(x, y, 1);
        }
        for (int i = 0; i < enemyBaseCount - setBaseRadii.size(); ++i) {
            int x = random.nextInt(WIDTH);
            int y = random.nextInt(HEIGHT);
            if (Math.abs(x - WIDTH / 2) <= noBaseRadius && Math.abs(y - HEIGHT / 2) <= noBaseRadius) {
                --i;
                continue;
            }
            generateEnemyBaseAt(x, y, 2);
        }
    }

    void generateEnemyBaseAt(int x, int y, int spawnCount) {
        List<Enemy> enemySpawns = new ArrayList<>();
        for (int i = 0; i < spawnCount; ++i) {
            enemySpawns.add(new BasicEnemy(new Vector2(x, y)));
        }
        EnemyBase newBase = new EnemyBase(new Pair<>(x, y), enemySpawns, 1.5f);
        enemyBases.add(newBase);
    }

    public void newTurn() {
        ++currentRadius;
        for (EnemyBase base : enemyBases) {
            if (isInRadius(base.position.getLeft(), base.position.getRight())) {
                base.startTurn();
                if (!GameState.gameState.updatableBuildings.contains(base)) {
                    GameState.gameState.updatableBuildings.add(base);
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
        int xDiff = Math.abs(WIDTH / 2 - x);
        int yDiff = Math.abs(HEIGHT / 2 - x);
        if (Math.max(xDiff, yDiff) > currentRadius) {
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
