package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.hycap.dbt.GameState;
import com.hycap.dbt.Pair;

public abstract class Building {
    Pair<Integer> position;
    Vector2 vecPosition;

    public abstract String getName();

    public abstract Texture getTexture();

    public abstract void onCreate(GameState gameState);

    public abstract void onDestroy(GameState gameState);

    public abstract Building duplicate();

    public void setPosition(Pair<Integer> position) {
        this.position = position;
        this.vecPosition = new Vector2(position.getLeft(), position.getRight());
    }

    public Pair<Integer> getPosition() {
        return position;
    }

}
