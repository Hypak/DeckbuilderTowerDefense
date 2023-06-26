package com.hycap.dbt.enemies;

import com.hycap.dbt.buildings.Building;

public class Neutral implements BuildingTargetPriority {
    public static Neutral neutral;
    static {
        neutral = new Neutral();
    }
    public float getPriority(Enemy enemy, Building building) {
        return 0;
    }
}
