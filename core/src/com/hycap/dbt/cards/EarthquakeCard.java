package com.hycap.dbt.cards;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.buildings.Building;
import com.hycap.dbt.buildings.EarthquakeBuilding;

public class EarthquakeCard extends BuildingCard implements BuyableCard {
    public static Texture texture;

    @Override
    public Building getBuilding() {
        return new EarthquakeBuilding();
    }

    @Override
    public int getEnergyCost() {
        return 3;
    }

    @Override
    public String getName() {
        return "Earthquake";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public Card duplicate() {
        return new EarthquakeCard();
    }

    @Override
    public int getBuyCost() {
        return 8;
    }
}
