package com.hycap.dbt.enemies;

import com.hycap.dbt.buildings.*;

public class PreferEconomy implements BuildingTargetPriority {
    public static PreferEconomy preferEconomy;
    static {
        preferEconomy = new PreferEconomy();
    }
    public float getPriority(Enemy enemy, Building building) {
        if (enemy.damageToTake > 0 || enemy.takenDamage) {
            return 0;
        }
        if (building instanceof CentralBuilding || building instanceof CoffersBuilding
                || building instanceof MageBuilding || building instanceof MineBuilding ) {
            return 3;
        }
        return 0;
    }
}
