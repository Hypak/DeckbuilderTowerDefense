package com.hycap.dbt.cards;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.buildings.Building;
import com.hycap.dbt.buildings.SniperBuilding;

public class SniperCard implements BuildingCard, BuyableCard {
    public static Texture texture;
    @Override
    public Building getBuilding() {
        return new SniperBuilding();
    }

    @Override
    public int getBuyCost() {
        return 8;
    }

    @Override
    public int getEnergyCost() {
        return 3;
    }

    @Override
    public String getName() {
        return "Sniper";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public String getInfo() {
        return "Sniper towers deal damage slowly at long range.";
    }

    @Override
    public Card duplicate() {
        return new SniperCard();
    }
}
