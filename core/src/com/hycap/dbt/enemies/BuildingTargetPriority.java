package com.hycap.dbt.enemies;

import com.hycap.dbt.buildings.Building;

public interface BuildingTargetPriority {
    float getPriority(Enemy enemy, Building building);
}

