package com.hycap.dbt.cards;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.buildings.Building;
import com.hycap.dbt.buildings.MageBuilding;

public class MageCard implements BuildingCard, BuyableCard, ExhaustCard {
    public static Texture texture;
    @Override
    public Building getBuilding() {
        return new MageBuilding();
    }

    @Override
    public int getEnergyCost() {
        return 3;
    }

    @Override
    public String getName() {
        return "Mage";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public Card duplicate() {
        return new MageCard();
    }

    @Override
    public int getBuyCost() {
        return 8;
    }
}
