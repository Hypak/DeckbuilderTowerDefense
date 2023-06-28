package com.hycap.dbt.enemies;

import com.hycap.dbt.buildings.Building;
import com.hycap.dbt.buildings.FieldBuilding;

public class Neutral implements BuildingTargetPriority {
    public static Neutral neutral;
    static {
        neutral = new Neutral();
    }
    public float getPriority(Enemy enemy, Building building) {
        if (building instanceof FieldBuilding) {
            return -3;
        }
        return 0;
    }
}
