package com.hycap.dbt.cards;

import com.hycap.dbt.GameState;
import com.hycap.dbt.buildings.Building;

public interface BuildingCard extends Card {
    Building getBuilding();
}
