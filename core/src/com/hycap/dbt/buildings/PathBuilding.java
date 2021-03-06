package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.GameState;
import com.hycap.dbt.Pair;

public class PathBuilding extends Building implements CanBuildOver {
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
    public void onCreate(GameState gameState) {}

    @Override
    public void onDestroy(GameState gameState) {}


    @Override
    public Building duplicate() {
        return new PathBuilding();
    }
}
