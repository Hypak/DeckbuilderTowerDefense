package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.GameState;

public class PathBuilding extends Building implements CannotBeRemoved, CanBeBuiltOver {
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
    public String getInfo() {
        return "Paths expand your base cheaply.";
    }

    @Override
    public String getStats() {
        return "";
    }

    @Override
    public void onCreate(GameState gameState, boolean onRift) {}

    @Override
    public void onDestroy(GameState gameState) {}


    @Override
    public Building duplicate() {
        return new PathBuilding();
    }
}
