package com.hycap.dbt.cards;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.buildings.Building;
import com.hycap.dbt.buildings.MineBuilding;

public class MineCard implements BuildingCard, BuyableCard {
    public static Texture texture;
    @Override
    public Building getBuilding() {
        return new MineBuilding();
    }

    @Override
    public int getEnergyCost() {
        return 2;
    }

    @Override
    public String getName() {
        return "Mine";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public Card duplicate() {
        return new MineCard();
    }

    @Override
    public int getBuyCost() {
        return 2;
    }
}
