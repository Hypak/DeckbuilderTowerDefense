package com.hycap.dbt.cards;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.buildings.BombBuilding;
import com.hycap.dbt.buildings.Building;
import com.hycap.dbt.buildings.CoffersBuilding;

public class BombCard extends BuildingCard implements BuyableCard {
    public static Texture texture;
    @Override
    public Building getBuilding() {
        return new BombBuilding();
    }

    @Override
    public int getEnergyCost() {
        return 1;
    }

    @Override
    public String getName() {
        return "Bomb";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public Card duplicate() {
        return new BombCard();
    }



    @Override
    public int getBuyCost() {
        return 4;
    }
}
