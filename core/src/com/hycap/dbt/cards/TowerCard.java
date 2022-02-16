package com.hycap.dbt.cards;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.buildings.Building;
import com.hycap.dbt.buildings.TowerBuilding;

public class TowerCard implements BuildingCard, BuyableCard {
    public static Texture texture;
    @Override
    public Building getBuilding() {
        return new TowerBuilding();
    }

    @Override
    public int getEnergyCost() {
        return 2;
    }

    @Override
    public String getName() {
        return "Tower";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public Card duplicate() {
        return new TowerCard();
    }

    @Override
    public String getInfo() {
        return "Towers attack enemies at medium range.";
    }

    @Override
    public int getBuyCost() {
        return 3;
    }
}
