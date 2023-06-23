package com.hycap.dbt;

import com.hycap.dbt.buildings.*;
import com.hycap.dbt.tasks.BuildMageTask;
import com.hycap.dbt.tasks.BuildRiftTask;
import com.hycap.dbt.tasks.BuildMineTask;
import com.hycap.dbt.tasks.BuildTowerTask;

import java.util.*;

public class Map {
    public static final java.util.Map<GameScreen.Difficulty, Integer> difficultyRadius;
    static {
        difficultyRadius = new HashMap<>();
        difficultyRadius.put(GameScreen.Difficulty.CREATIVE, 50);
        difficultyRadius.put(GameScreen.Difficulty.EASY, 35);
        difficultyRadius.put(GameScreen.Difficulty.NORMAL, 50);
        difficultyRadius.put(GameScreen.Difficulty.HARD, 60);
    }

    public final int SIZE;
    private final Building[][] buildings;
    final List<Building> buildingList;
    final List<Pair<Integer>> riftCoords;
    private final int energyPerRift = 1;

    int currentRadius = 2;
    final int extraViewRadius = 5;

    public final EnemyBaseManager enemyBaseManager;

    public Map(final GameState gameState, final GameScreen.Difficulty difficulty) {
        SIZE = 2 * difficultyRadius.get(difficulty) + 1;

        buildings = new Building[SIZE][SIZE];
        final Building centralBuilding = new CentralBuilding();
        centralBuilding.setPosition(new Pair<>(SIZE / 2, SIZE / 2));
        centralBuilding.onCreate(gameState, false);
        buildings[SIZE / 2][SIZE / 2] = centralBuilding;
        buildingList = new LinkedList<>();
        buildingList.add(centralBuilding);
        riftCoords = new ArrayList<>();

        enemyBaseManager = new EnemyBaseManager(difficulty, this);
    }

    public void newTurn() {
        ++currentRadius;
        GameState.gameState.gameStats.setRadius(currentRadius);
        enemyBaseManager.newTurn();
    }


    public int getRadius(final int x, final int y) {
        final int xRad = Math.abs(x - SIZE / 2);
        final int yRad = Math.abs(y - SIZE / 2);
        return Math.max(xRad, yRad);
    }

    public boolean isInRadius(final int x, final int y) {
        final int radius = getRadius(x, y);
        return radius <= currentRadius;
    }

    public boolean isInViewRadius(final int x, final int y) {
        final int xRad = Math.abs(x - SIZE / 2);
        final int yRad = Math.abs(y - SIZE / 2);
        return Math.max(xRad, yRad) <= currentRadius + extraViewRadius;
    }

    public Building getBuilding(final int x, final int y) {
        if (x < 0 || y < 0 || x >= SIZE || y >= SIZE) {
            return null;
        }
        return buildings[x][y];
    }

    public boolean canPlaceBuilding(final int x, final int y) {
        if (x < 0 || y < 0 || x >= SIZE || y >= SIZE || !isInRadius(x, y)) {
            return false;
        }
        if (buildings[x][y] != null) {
            return buildings[x][y] instanceof CanBeBuiltOver;
        }

        final Pair<Integer> position = new Pair<>(x, y);
        for (final EnemyBase base : enemyBaseManager.enemyBases) {
            if (base.position.equals(position)) {
                return false;
            }
        }
        if (x - 1 >= 0 && buildings[x-1][y] != null) {
            return true;
        }
        if (x + 1 < SIZE && buildings[x+1][y] != null) {
            return true;
        }
        if (y - 1 >= 0 && buildings[x][y-1] != null) {
            return true;
        }
        return y + 1 < SIZE && buildings[x][y + 1] != null;
    }

    public boolean placeBuilding(final Building building, final int x, final int y) {
        if (!canPlaceBuilding(x, y)) {
            return false;
        }
        final Building oldBuilding = buildings[x][y];
        if (oldBuilding != null) {
            if (building.getName().equals(oldBuilding.getName())) {
                return false;
            }
        }
        buildings[x][y] = building;
        final Pair<Integer> coords = new Pair<>(x, y);
        buildingList.add(building);
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
        if (oldBuilding != null) {
            buildingList.remove(oldBuilding);
            UIManager.setSelectedInfo(building);
            if (oldBuilding instanceof HasRange) {
                GameScreen.gameScreen.selectedViewTowers.remove((HasRange) oldBuilding);
            }
            oldBuilding.onDestroy(GameState.gameState);
        }
        return true;
    }

    private void removeBuildingAt(final int x, final int y) {
        final Building building = buildings[x][y];
        final Pair<Integer> coords = new Pair<>(x, y);
        final PathBuilding newPath = new PathBuilding();
        buildings[x][y] = newPath;
        newPath.setPosition(coords);
        buildingList.add(newPath);
        if (riftCoords.contains(coords) && building instanceof AttackableBuilding) {
            GameState.gameState.baseEnergy -= energyPerRift;
            if (GameState.gameState.currentEnergy > GameState.gameState.baseEnergy) {
                GameState.gameState.currentEnergy = GameState.gameState.baseEnergy;
            }
        }
        buildingList.remove(building);
    }

    public void removeBuilding(final Building building) {
        for (int x = 0; x < SIZE; ++x) {
            for (int y = 0; y < SIZE; ++y) {
                if (buildings[x][y] != null && buildings[x][y].equals(building)) {
                    removeBuildingAt(x, y);
                    return;
                }
            }
        }
    }

    public List<Building> getBuildingList() {
        return buildingList;
    }

    public boolean areAllBuildingsDead() {
        for (final Building building : getBuildingList()) {
            if (building instanceof AttackableBuilding) {
                return false;
            }
        }
        return true;
    }
}
