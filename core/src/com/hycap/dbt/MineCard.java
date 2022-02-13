package com.hycap.dbt;

import com.badlogic.gdx.graphics.Texture;

public class MineCard implements BuildingCard {
    public static Texture texture;
    @Override
    public Building getBuilding() {
        return new MineBuilding();
    }

    @Override
    public String getName() {
        return "Mine";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }
}
