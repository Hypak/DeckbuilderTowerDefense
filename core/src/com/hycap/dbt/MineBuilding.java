package com.hycap.dbt;

import com.badlogic.gdx.graphics.Texture;

public class MineBuilding implements Building {
    public static Texture texture;
    @Override
    public String getName() {
        return "Mine";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }
}
