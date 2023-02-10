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
    public void onCreate(GameState gameState, boolean onRift) {
        super.health = 175;
        if (onRift) {
            super.health *= 2;  // N.B. health is multiplied by 1.5 after this, total 3x health boost
        }
        super.onCreate(gameState, onRift);
    }

    @Override
    public void onDestroy(GameState gameState) {}

    @Override
    public Building duplicate() {
        return new WallBuilding();
    }
}
