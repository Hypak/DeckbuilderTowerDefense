package com.hycap.dbt.enemies;

import com.hycap.dbt.buildings.AbstractTowerBuilding;
import com.hycap.dbt.buildings.Building;
import com.hycap.dbt.buildings.CentralBuilding;

public class PreferTowers implements BuildingTargetPriority {
    public static PreferTowers preferTowers;
    static {
        preferTowers = new PreferTowers();
    }
    public float getPriority(Enemy enemy, Building building) {
        if (building instanceof AbstractTowerBuilding) {
            return 1.5f;
        }
        if (building instanceof CentralBuilding) {
            return 1;
        }
        return 0;
    }
}