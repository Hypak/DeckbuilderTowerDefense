package com.hycap.dbt.cards;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.buildings.Building;
import com.hycap.dbt.buildings.WallBuilding;

public class WallCard implements BuildingCard, BuyableCard {
    public static Texture texture;
    @Override
    public Building getBuilding() {
        return new WallBuilding();
    }

    @Override
    public int getBuyCost() {
        return 7;
    }

    @Override
    public int getEnergyCost() {
        return 2;
    }

    @Override
    public String getName() {
        return "Wall";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public String getInfo() {
        return "Walls have lots of health.";
    }

    @Override
    public Card duplicate() {
        return new WallCard();
    }
}
