package com.hycap.dbt.cards;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.buildings.Building;
import com.hycap.dbt.buildings.PaverBuilding;

public class PaverCard extends BuildingCard implements BuyableCard, ExhaustCard {
    public static Texture texture;
    @Override
    public Building getBuilding() {
        return new PaverBuilding();
    }

    @Override
    public int getEnergyCost() {
        return 3;
    }

    @Override
    public String getName() {
        return "Paver";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public Card duplicate() {
        return new PaverCard();
    }

    @Override
    public int getBuyCost() {
        return 10;
    }
}
