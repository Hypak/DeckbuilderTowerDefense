package com.hycap.dbt.cards;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.buildings.Building;
import com.hycap.dbt.buildings.MineBuilding;
import com.hycap.dbt.buildings.ShackBuilding;

public class ShackCard extends BuildingCard implements BuyableCard {
    public static Texture texture;
    @Override
    public Building getBuilding() {
        return new ShackBuilding();
    }

    @Override
    public int getEnergyCost() {
        return 1;
    }

    @Override
    public String getName() {
        return "Shack";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public Card duplicate() {
        return new ShackCard();
    }

    @Override
    public int getBuyCost() {
        return 1;
    }
}
