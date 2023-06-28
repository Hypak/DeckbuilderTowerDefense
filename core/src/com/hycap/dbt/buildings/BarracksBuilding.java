package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.hycap.dbt.ActionOnStartTurn;
import com.hycap.dbt.GameState;
import com.hycap.dbt.units.FarmerUnit;
import com.hycap.dbt.units.KnightUnit;
import com.hycap.dbt.units.SuperKnightUnit;

public class BarracksBuilding extends AttackableBuilding implements ActionOnStartTurn {
    public static Texture texture;

    private boolean isOnRift = false;

    @Override
    public String getName() {
        return "Barracks";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public String getInfo() {
        if (isOnRift) {
            return "Generate 1 knight per turn.\nAdjacent shacks produce knights.";
        } else {
            return "Generate 1 Super Knight per turn.\nAdjacent shacks produce knights.";
        }
    }

    @Override
    public void onCreate(final GameState gameState, final boolean onRift) {
        health = 100;
        isOnRift = onRift;
        super.onCreate(gameState, onRift);
    }

    @Override
    public void onDestroy(final GameState gameState) {

    }

    @Override
    public Building duplicate() {
        return new BarracksBuilding();
    }

    @Override
    public void startTurn() {
        Vector2 vecPos = new Vector2(position.getLeft(), position.getRight());
        if (onRift) {
            GameState.gameState.units.add(new SuperKnightUnit(vecPos));
        } else {
            GameState.gameState.units.add(new KnightUnit(vecPos));
        }

    }
}
