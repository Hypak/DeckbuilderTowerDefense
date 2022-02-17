package com.hycap.dbt.cards;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.buildings.Building;
import com.hycap.dbt.buildings.PathBuilding;

public class PathCard extends BuildingCard {
    public static Texture texture;

    @Override
    public int getEnergyCost() {
        return 1;
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
        return new PathCard();
    }

    @Override
    public Building getBuilding() {
        return new PathBuilding();
    }
}
