package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.hycap.dbt.ActionOnStartTurn;
import com.hycap.dbt.GameState;
import com.hycap.dbt.Updatable;
import com.hycap.dbt.units.FarmerUnit;

public class ShackBuilding extends AttackableBuilding implements ActionOnStartTurn {
    public static Texture texture;

    private static final int baseUnitsPerTurn = 1;
    private int unitsPerTurn = baseUnitsPerTurn;

    @Override
    public String getName() {
        return "Shack";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public String getInfo() {
        if (unitsPerTurn == 1) {
            return "Generate 1 unit per turn.\n";
        } else {
            return "Generate " + unitsPerTurn + " units per turn.\n";
        }
    }

    @Override
    public void onCreate(final GameState gameState, final boolean onRift) {
        health = 5;
        unitsPerTurn = baseUnitsPerTurn;
        if (onRift) {
            unitsPerTurn += baseUnitsPerTurn;
        }
        super.onCreate(gameState, onRift);
    }

    @Override
    public void onDestroy(final GameState gameState) {

    }

    @Override
    public Building duplicate() {
        return new ShackBuilding();
    }

    @Override
    public void startTurn() {
        for (int i = 0; i < unitsPerTurn; ++i) {
            Vector2 vecPos = new Vector2(position.getLeft(), position.getRight());
            GameState.gameState.units.add(new FarmerUnit(vecPos));
        }
    }
}
