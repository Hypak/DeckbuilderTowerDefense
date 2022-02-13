package com.hycap.dbt;

import com.badlogic.gdx.graphics.Texture;

public class PathBuilding implements Building {
    public static Texture texture;

    @Override
    public String getName() {
        return "Path";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }
}
