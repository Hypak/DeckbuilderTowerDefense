package com.hycap.dbt;

import com.badlogic.gdx.graphics.Texture;

public class CentralBuilding implements Building {
    public static Texture texture;

    @Override
    public String getName() {
        return "Central";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }
}
