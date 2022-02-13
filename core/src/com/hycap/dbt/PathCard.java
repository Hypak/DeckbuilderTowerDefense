package com.hycap.dbt;

import com.badlogic.gdx.graphics.Texture;

public class PathCard implements BuildingCard {
    public static Texture texture;
    @Override
    public String getName() {
        return "Path";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public Building getBuilding() {
        return new PathBuilding();
    }
}
