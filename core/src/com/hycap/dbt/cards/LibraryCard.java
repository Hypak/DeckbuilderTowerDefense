package com.hycap.dbt.cards;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.buildings.Building;
import com.hycap.dbt.buildings.LibraryBuilding;

public class LibraryCard extends BuildingCard implements BuyableCard, ExhaustCard {
    public static Texture texture;
    @Override
    public Building getBuilding() {
        return new LibraryBuilding();
    }

    @Override
    public int getBuyCost() {
        return 6;
    }

    @Override
    public int getEnergyCost() {
        return 2;
    }

    @Override
    public String getName() {
        return "Library";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public Card duplicate() {
        return new LibraryCard();
    }
}
