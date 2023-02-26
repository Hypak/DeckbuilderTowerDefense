package com.hycap.dbt.cards;

import com.hycap.dbt.buildings.Building;

public abstract class BuildingCard implements Card {
    public abstract Building getBuilding();

    @Override
    public String getInfo() {
        return getBuilding().getInfo();
    }
}
