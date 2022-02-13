package com.hycap.dbt;

public class Map {
    public final int WIDTH = 21;
    public final int HEIGHT = 21;
    private Building[][] buildings;

    public Map() {
        this.buildings = new Building[WIDTH][HEIGHT];
        this.buildings[WIDTH / 2][HEIGHT / 2] = new CentralBuilding();
    }

    public Building getBuilding(int x, int y) {
        return buildings[x][y];
    }

    public boolean canPlaceBuilding(int x, int y) {
        if (x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT) {
            return false;
        }

        for (int xDiff = -1; xDiff <= 1; xDiff += 2) {
            if (this.buildings[x + xDiff][y] != null) {
                return true;
            }
        }
        for (int yDiff = -1; yDiff <= 1; yDiff += 2) {
            if (this.buildings[x][y + yDiff] != null) {
                return true;
            }
        }
        return false;
    }

    public boolean placeBuilding(Building building, int x, int y) {
        if (!canPlaceBuilding(x, y)) {
            return false;
        }
        this.buildings[x][y] = building;
        return true;
    }
}
