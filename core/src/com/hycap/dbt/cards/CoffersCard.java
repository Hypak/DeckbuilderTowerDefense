package com.hycap.dbt.cards;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.buildings.Building;
import com.hycap.dbt.buildings.CoffersBuilding;

public class CoffersCard implements BuildingCard {
    public static Texture texture;
    @Override
    public Building getBuilding() {
        return new CoffersBuilding();
    }

    @Override
    public int getEnergyCost() {
        return 2;
    }

    @Override
    public String getName() {
        return "Coffers";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }
}
