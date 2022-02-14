package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.GameState;
import com.hycap.dbt.Pair;

public class PathBuilding implements Building {
    public static Texture texture;
    Pair<Integer> position;

    @Override
    public String getName() {
        return "Path";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public void onCreate(GameState gameState) {

    }

    @Override
    public void onDestroy(GameState gameState) {

    }

    @Override
    public Building duplicate() {
        return new PathBuilding();
    }

    @Override
    public void setPosition(Pair<Integer> position) {
        this.position = position;
    }

    @Override
    public Pair<Integer> getPosition() {
        return position;
    }
}
