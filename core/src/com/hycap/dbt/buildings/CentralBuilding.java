package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.GameState;
import com.hycap.dbt.Pair;

public class CentralBuilding extends AttackableBuilding {
    public static Texture texture;

    @Override
    public String getName() {
        return "Central";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public void onCreate(GameState gameState) {
        super.health = 75;
    }

    @Override
    public void onDestroy(GameState gameState) {

    }

    @Override
    public Building duplicate() {
        return new CentralBuilding();
    }

}
