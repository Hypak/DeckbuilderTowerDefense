package com.hycap.dbt;

import com.hycap.dbt.buildings.Building;
import com.hycap.dbt.buildings.CentralBuilding;

public class Map {
    public final int WIDTH = 21;
    public final int HEIGHT = 21;
    private final Building[][] buildings;

    public Map() {
        this.buildings = new Building[WIDTH][HEIGHT];
        this.buildings[WIDTH / 2][HEIGHT / 2] = new CentralBuilding();
    }

    public Building getBuilding(int x, int y) {
        return buildings[x][y];
    }

    public boolean canPlaceBuilding(int x, int y) {
        if (this.buildings[x][y] != null) {
            return false;
        }
        if (x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT) {
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
        return true;
    }
}
