package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;

public class CoffersBuilding implements Building {
    public static Texture texture;
    @Override
    public String getName() {
        return "Coffers";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }
}
