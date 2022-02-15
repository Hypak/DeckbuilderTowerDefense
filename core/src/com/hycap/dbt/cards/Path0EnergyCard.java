package com.hycap.dbt.cards;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.buildings.Building;
import com.hycap.dbt.buildings.PathBuilding;

public class Path0EnergyCard implements BuildingCard, ExhaustCard {
    public static Texture texture;
    @Override
    public Building getBuilding() {
        return new PathBuilding();
    }

    @Override
    public int getEnergyCost() {
        return 0;
    }

    @Override
    public String getName() {
        return "Path";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public Card duplicate() {
        return new Path0EnergyCard();
    }
}