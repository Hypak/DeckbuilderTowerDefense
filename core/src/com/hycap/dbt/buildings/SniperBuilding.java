package com.hycap.dbt.buildings;

import com.hycap.dbt.GameState;

public class SniperBuilding extends TowerBuilding {
    @Override
    public void onCreate(GameState gameState) {
        super.range = 7;
        super.onCreate(gameState);
        super.health = 75;
        super.range = 7;
        super.reloadTime = 3;
        super.damage = 5;
    }
}
