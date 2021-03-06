package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.GameState;

public class WallBuilding extends AttackableBuilding {
    public static Texture texture;
    @Override
    public String getName() {
        return "Wall";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public String getInfo() {
        return "Walls have lots of health.";
    }


    @Override
    public void onCreate(GameState gameState) {
        super.health = 175;
        super.onCreate(gameState);
    }

    @Override
    public void onDestroy(GameState gameState) {}

    @Override
    public Building duplicate() {
        return new WallBuilding();
    }
}
