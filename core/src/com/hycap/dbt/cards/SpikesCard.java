package com.hycap.dbt.cards;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.buildings.Building;
import com.hycap.dbt.buildings.SpikesBuilding;

public class SpikesCard extends BuildingCard implements BuyableCard {
    public static Texture texture;
    @Override
    public Building getBuilding() {
        return new SpikesBuilding();
    }

    @Override
    public int getEnergyCost() {
        return 2;
    }

    @Override
    public String getName() {
        return "Spikes";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public Card duplicate() {
        return new SpikesCard();
    }

    @Override
    public int getBuyCost() {
        return 6;
    }
}
