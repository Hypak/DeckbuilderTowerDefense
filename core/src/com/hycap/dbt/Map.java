package com.hycap.dbt;

import com.badlogic.gdx.math.Vector2;
import com.hycap.dbt.buildings.*;
import com.hycap.dbt.enemies.*;
import com.hycap.dbt.tasks.BuildMageTask;
import com.hycap.dbt.tasks.BuildRiftTask;
import com.hycap.dbt.tasks.BuildMineTask;
import com.hycap.dbt.tasks.BuildTowerTask;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Map {
    public final int WIDTH = 101;
    public final int HEIGHT = 101;
    final Building[][] buildings;
    final List<Pair<Integer>> buildingCoords;
    final List<Pair<Integer>> riftCoords;
    int energyPerRift = 1;
    public final List<EnemyBase> enemyBases;
    final List<EnemyBase> activeEnemyBases;
    final int enemyBaseCount = 80;
    final int fastEnemyBaseCount = 30;
    final int bigEnemyBaseCount = 30;
    final int noBaseRadius = 15;
    final int noFastBaseRadius = 20;
    final int noBigBaseRadius = 30;
    final List<Integer> setBaseRadii;
    int currentRadius = 2;
    final int extraViewRadius = 5;

    public Map() {
        buildings = new Building[WIDTH][HEIGHT];
        Building centralBuilding = new CentralBuilding();
        centralBuilding.setPosition(new Pair<>(WIDTH / 2, HEIGHT / 2));
        centralBuilding.onCreate(GameState.gameState);
        buildings[WIDTH / 2][HEIGHT / 2] = centralBuilding;
        buildingCoords = new LinkedList<>();
        buildingCoords.add(new Pair<>(WIDTH / 2, HEIGHT / 2));
        riftCoords = new ArrayList<>();

        setBaseRadii = new ArrayList<>();
        setBaseRadii.add(8);
        setBaseRadii.add(10);
        setBaseRadii.add(12);
        setBaseRadii.add(14);
        enemyBases = new ArrayList<>();
        activeEnemyBases = new ArrayList<>();
        generateBases();
    }

    void generateBases() {
        Random random = new Random();
        generateSetBases();
        for (int i = 0; i < enemyBaseCount - setBaseRadii.size(); ++i) {
            int x = random.nextInt(WIDTH);
            int y = random.nextInt(HEIGHT);
            if (Math.abs(x - WIDTH / 2) <= noBaseRadius && Math.abs(y - HEIGHT / 2) <= noBaseRadius || isEnemyBaseAt(x, y)) {
                --i;
                continue;
            }
            generateBasicEnemyBaseAt(x, y, 2);
        }
        for (int i = 0; i < fastEnemyBaseCount; ++i) {
            int x = random.nextInt(WIDTH);
            int y = random.nextInt(HEIGHT);
            if (Math.abs(x - WIDTH / 2) <= noFastBaseRadius && Math.abs(y - HEIGHT / 2) <= noFastBaseRadius || isEnemyBaseAt(x, y)) {
                --i;
                continue;
            }
            generateFastEnemyBaseAt(x, y, 2);
        }
        for (int i = 0; i < bigEnemyBaseCount; ++i) {
            int x = random.nextInt(WIDTH);
            int y = random.nextInt(HEIGHT);
            if (Math.abs(x - WIDTH / 2) <= noBigBaseRadius && Math.abs(y - HEIGHT / 2) <= noBigBaseRadius || isEnemyBaseAt(x, y)) {
                --i;
                continue;
            }
            generateBigEnemyBaseAt(x, y);
        }
    }

    void generateSetBases() {
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
            generateBasicEnemyBaseAt(x, y, 1);
        }
    }

    void generateBasicEnemyBaseAt(int x, int y, int spawnCount) {
        List<Enemy> enemySpawns = new ArrayList<>();
        for (int i = 0; i < spawnCount; ++i) {
            enemySpawns.add(new BasicEnemy(new Vector2(x, y)));
        }
        EnemyBase newBase = new EnemyBase(new Pair<>(x, y), enemySpawns, 1.5f);
        enemyBases.add(newBase);
    }

    void generateFastEnemyBaseAt(int x, int y, int spawnCount) {
        List<Enemy> enemySpawns = new ArrayList<>();
        for (int i = 0; i < spawnCount; ++i) {
            enemySpawns.add(new FastEnemy(new Vector2(x, y)));
        }
        EnemyBase newBase = new EnemyBase(new Pair<>(x, y), enemySpawns, 2f);
        enemyBases.add(newBase);
    }

    void generateBigEnemyBaseAt(int x, int y) {
        List<Enemy> enemySpawns = new ArrayList<>();
        enemySpawns.add(new BigEnemy(new Vector2(x, y)));
        EnemyBase newBase = new EnemyBase(new Pair<>(x, y), enemySpawns, 2f);
        enemyBases.add(newBase);
    }

    public void destroyEnemyBase(EnemyBase base) {
        enemyBases.remove(base);
        riftCoords.add(new Pair<>(base.position.getLeft(), base.position.getRight()));
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
        for (Pair<Integer> buildingCoord : buildingCoords) {
            Building building = buildings[buildingCoord.getLeft()][buildingCoord.getRight()];
            if (building instanceof AttackableBuilding) {
                AttackableBuilding attackableBuilding = (AttackableBuilding)building;
                attackableBuilding.health = attackableBuilding.maxHealth;
            }
        }
    }

    public boolean isInRadius(int x, int y) {
        int xRad = Math.abs(x - WIDTH / 2);
        int yRad = Math.abs(y - HEIGHT / 2);
        return Math.max(xRad, yRad) <= currentRadius;
    }

    public boolean isInViewRadius(int x, int y) {
        int xRad = Math.abs(x - WIDTH / 2);
        int yRad = Math.abs(y - HEIGHT / 2);
        return Math.max(xRad, yRad) <= currentRadius + extraViewRadius;
    }

    public boolean isEnemyBaseAt(int x, int y) {
        Pair<Integer> pos = new Pair<>(x, y);
        for (EnemyBase base : enemyBases) {
            if (base.position.equals(pos)) {
                return true;
            }
        }
        return false;
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
            return this.buildings[x][y] instanceof CanBuildOver;
        }
        int xDiff = Math.abs(WIDTH / 2 - x);
        int yDiff = Math.abs(HEIGHT / 2 - x);
        if (Math.max(xDiff, yDiff) > currentRadius) {
            return false;
        }
        Pair<Integer> position = new Pair<>(x, y);
        for (EnemyBase base : enemyBases) {
            if (base.position.equals(position)) {
                return false;
            }
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
        Building otherBuilding = getBuilding(x, y);
        if (otherBuilding != null && building.getName().equals(otherBuilding.getName())) {
            return false;
        }
        this.buildings[x][y] = building;
        Pair<Integer> coords = new Pair<>(x, y);
        this.buildingCoords.add(coords);
        if (riftCoords.contains(coords) && building instanceof AttackableBuilding) {
            GameState.gameState.baseEnergy += energyPerRift;
            BuildRiftTask.finished = true;
        }
        if (building instanceof MineBuilding) {
            BuildMineTask.finished = true;
        } else if (building instanceof AbstractTowerBuilding) {
            BuildTowerTask.finished = true;
        } else if (building instanceof MageBuilding) {
            BuildMageTask.finished = true;
        }
        return true;
    }

    public void removeBuilding(int x, int y) {
        Building building = buildings[x][y];
        Pair<Integer> coords = new Pair<>(x, y);
        this.buildings[x][y] = new PathBuilding();
        if (riftCoords.contains(coords) && building instanceof AttackableBuilding) {
            GameState.gameState.baseEnergy -= energyPerRift;
            if (GameState.gameState.currentEnergy > GameState.gameState.baseEnergy) {
                GameState.gameState.currentEnergy = GameState.gameState.baseEnergy;
            }
        }
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
