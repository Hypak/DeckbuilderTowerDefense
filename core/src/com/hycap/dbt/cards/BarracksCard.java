package com.hycap.dbt.cards;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.buildings.BarracksBuilding;
import com.hycap.dbt.buildings.Building;
import com.hycap.dbt.buildings.ShackBuilding;

public class BarracksCard extends BuildingCard implements BuyableCard {
    public static Texture texture;
    @Override
    public Building getBuilding() {
        return new BarracksBuilding();
    }

    @Override
    public int getEnergyCost() {
        return 4;
    }

    @Override
    public String getName() {
        return "Barracks";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public Card duplicate() {
        return new BarracksCard();
    }

    @Override
    public int getBuyCost() {
        return 20;
    }
}
