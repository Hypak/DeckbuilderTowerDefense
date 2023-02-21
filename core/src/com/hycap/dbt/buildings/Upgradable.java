package com.hycap.dbt.buildings;

import com.hycap.dbt.GameState;

public interface Upgradable {
    boolean tryUpgrade(GameState gameState);
    int getUpgradeCost();
}
