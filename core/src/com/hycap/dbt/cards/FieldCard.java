package com.hycap.dbt.cards;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.buildings.Building;
import com.hycap.dbt.buildings.FieldBuilding;
import com.hycap.dbt.buildings.MineBuilding;

public class FieldCard extends BuildingCard implements BuyableCard {
    public static Texture texture;
    @Override
    public Building getBuilding() {
        return new FieldBuilding();
    }

    @Override
    public int getEnergyCost() {
        return 0;
    }

    @Override
    public String getName() {
        return "Field";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public Card duplicate() {
        return new FieldCard();
    }

    @Override
    public int getBuyCost() {
        return 1;
    }
}
