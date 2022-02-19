package com.hycap.dbt;

import com.badlogic.gdx.math.Vector2;
import com.hycap.dbt.buildings.*;
import com.hycap.dbt.enemies.*;
import com.hycap.dbt.tasks.BuildMageTask;
import com.hycap.dbt.tasks.BuildRiftTask;
import com.hycap.dbt.tasks.BuildMineTask;
import com.hycap.dbt.tasks.BuildTowerTask;

import java.util.*;

public class Map {
    public static java.util.Map<GameScreen.Difficulty, Integer> difficultyRadius;
    static {
        difficultyRadius = new HashMap<>();
        difficultyRadius.put(GameScreen.Difficulty.EASY, 35);
        difficultyRadius.put(GameScreen.Difficulty.NORMAL, 50);
        difficultyRadius.put(GameScreen.Difficulty.HARD, 60);
    }

    public final int SIZE;
    final Building[][] buildings;
    final List<Pair<Integer>> buildingCoords;
    final List<Pair<Integer>> riftCoords;
    int energyPerRift = 1;
    public final List<EnemyBase> enemyBases;
    final List<EnemyBase> activeEnemyBases;
    final int enemyBaseCount;
    final int fastEnemyBaseCount;
    final int bigEnemyBaseCount;
    final int noBaseRadius = 18;
    final int noFastBaseRadius = 26;
    final int noBigBaseRadius = 35;
    final List<Pair<Integer>> setBasicBaseRadii;
    final List<Pair<Integer>> setFastBaseRadii;
    final List<Pair<Integer>> setBigBaseRadii;
    int currentRadius = 2;
    final int extraViewRadius = 5;

    public Map(GameState gameState, GameScreen.Difficulty difficulty) {
        this.SIZE = 2 * difficultyRadius.get(difficulty) + 1;
        if (difficulty == GameScreen.Difficulty.EASY) {
            enemyBaseCount = 30;
            fastEnemyBaseCount = 8;
            bigEnemyBaseCount = 0;
        } else if (difficulty == GameScreen.Difficulty.NORMAL) {
            enemyBaseCount = 75;
            fastEnemyBaseCount = 30;
            bigEnemyBaseCount = 30;
        } else {
            enemyBaseCount = 115;
            fastEnemyBaseCount = 45;
            bigEnemyBaseCount = 45;
        }
        buildings = new Building[SIZE][SIZE];
        Building centralBuilding = new CentralBuilding();
        centralBuilding.setPosition(new Pair<>(SIZE / 2, SIZE / 2));
        centralBuilding.onCreate(gameState);
        buildings[SIZE / 2][SIZE / 2] = centralBuilding;
        buildingCoords = new LinkedList<>();
        buildingCoords.add(new Pair<>(SIZE / 2, SIZE / 2));
        riftCoords = new ArrayList<>();

        setBasicBaseRadii = new ArrayList<>();
        setBasicBaseRadii.add(new Pair<>(8, 1));
        setBasicBaseRadii.add(new Pair<>(10, 1));
        setBasicBaseRadii.add(new Pair<>(12, 1));
        setBasicBaseRadii.add(new Pair<>(14, 1));
        setBasicBaseRadii.add(new Pair<>(16, 2));
        setBasicBaseRadii.add(new Pair<>(17, 2));
        setBasicBaseRadii.add(new Pair<>(18, 2));

        setFastBaseRadii = new ArrayList<>();
        setFastBaseRadii.add(new Pair<>(20, 1));
        setFastBaseRadii.add(new Pair<>(22, 1));
        setFastBaseRadii.add(new Pair<>(24, 2));
        setFastBaseRadii.add(new Pair<>(26, 2));

        setBigBaseRadii = new ArrayList<>();
        setBigBaseRadii.add(new Pair<>(30, 1));
        setBigBaseRadii.add(new Pair<>(32, 1));
        setBigBaseRadii.add(new Pair<>(34, 1));

        enemyBases = new ArrayList<>();
        activeEnemyBases = new ArrayList<>();
        generateBases();
    }

    void generateBases() {
        Random random = new Random();
        generateSetBases();
        for (int i = 0; i < enemyBaseCount - setBasicBaseRadii.size(); ++i) {
            int x = random.nextInt(SIZE);
            int y = random.nextInt(SIZE);
            if (Math.abs(x - SIZE / 2) <= noBaseRadius && Math.abs(y - SIZE / 2) <= noBaseRadius || isEnemyBaseAt(x, y)) {
                --i;
                continue;
            }
            generateBasicEnemyBaseAt(x, y, 2);
        }
        for (int i = 0; i < fastEnemyBaseCount; ++i) {
            int x = random.nextInt(SIZE);
            int y = random.nextInt(SIZE);
            if (Math.abs(x - SIZE / 2) <= noFastBaseRadius && Math.abs(y - SIZE / 2) <= noFastBaseRadius || isEnemyBaseAt(x, y)) {
                --i;
                continue;
            }
            generateFastEnemyBaseAt(x, y, 2);
        }
        for (int i = 0; i < bigEnemyBaseCount; ++i) {
            int x = random.nextInt(SIZE);
            int y = random.nextInt(SIZE);
            if (Math.abs(x - SIZE / 2) <= noBigBaseRadius && Math.abs(y - SIZE / 2) <= noBigBaseRadius || isEnemyBaseAt(x, y)) {
                --i;
                continue;
            }
            generateBigEnemyBaseAt(x, y, 1);
        }
    }

    Pair<Integer> getCoordsAtRadius(int radius) {
        Random random = new Random();
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
        x += SIZE / 2;
        y += SIZE / 2;
        return new Pair<>(x, y);
    }

    void generateSetBases() {
        for (Pair<Integer> radiusCount : setBasicBaseRadii) {
            Pair<Integer> coords = getCoordsAtRadius(radiusCount.getLeft());
            generateBasicEnemyBaseAt(coords.getLeft(), coords.getRight(), radiusCount.getRight());
        }
        for (Pair<Integer> radiusCount : setFastBaseRadii) {
            Pair<Integer> coords = getCoordsAtRadius(radiusCount.getLeft());
            generateFastEnemyBaseAt(coords.getLeft(), coords.getRight(), radiusCount.getRight());
        }
        for (Pair<Integer> radiusCount : setBigBaseRadii) {
            Pair<Integer> coords = getCoordsAtRadius(radiusCount.getLeft());
            generateBigEnemyBaseAt(coords.getLeft(), coords.getRight(), radiusCount.getRight());
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

    void generateBigEnemyBaseAt(int x, int y, int spawnCount) {
        List<Enemy> enemySpawns = new ArrayList<>();
        for (int i = 0; i < spawnCount; ++i) {
            enemySpawns.add(new BigEnemy(new Vector2(x, y)));
        }
        EnemyBase newBase = new EnemyBase(new Pair<>(x, y), enemySpawns, 2f);
        enemyBases.add(newBase);
    }

    public void destroyEnemyBase(EnemyBase base) {
        enemyBases.remove(base);
        riftCoords.add(new Pair<>(base.position.getLeft(), base.position.getRight()));
    }

    public void newTurn() {
        ++currentRadius;
        GameState.gameState.gameStats.setRadius(currentRadius);
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
        int xRad = Math.abs(x - SIZE / 2);
        int yRad = Math.abs(y - SIZE / 2);
        return Math.max(xRad, yRad) <= currentRadius;
    }

    public boolean isInViewRadius(int x, int y) {
        int xRad = Math.abs(x - SIZE / 2);
        int yRad = Math.abs(y - SIZE / 2);
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
        if (x < 0 || y < 0 || x >= SIZE || y >= SIZE) {
            return null;
        }
        return buildings[x][y];
    }

    public boolean canPlaceBuilding(int x, int y) {
        if (x < 0 || y < 0 || x >= SIZE || y >= SIZE) {
            return false;
        }
        if (this.buildings[x][y] != null) {
            return this.buildings[x][y] instanceof CanBuildOver;
        }
        if (!isInRadius(x, y)) {
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
        if (x + 1 < SIZE && this.buildings[x+1][y] != null) {
            return true;
        }
        if (y - 1 >= 0 && this.buildings[x][y-1] != null) {
            return true;
        }
        if (y + 1 < SIZE && this.buildings[x][y+1] != null) {
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
        ++GameState.gameState.gameStats.buildingsPlaced;
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
        buildingCoords.remove(coords);
    }

    public void removeBuilding(Building b) {
        for (int x = 0; x < SIZE; ++x) {
            for (int y = 0; y < SIZE; ++y) {
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
