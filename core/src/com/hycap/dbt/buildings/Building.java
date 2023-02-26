package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.hycap.dbt.GameState;
import com.hycap.dbt.Pair;

public abstract class Building {
    Pair<Integer> position = null;
    Vector2 vecPosition = null;

    boolean onRift = false;

    public abstract String getName();

    public abstract String getInfo();

    public abstract String getStats();

    public abstract Texture getTexture();

    public void onCreate(final GameState gameState, final boolean onRift) {
        this.onRift = onRift;
    }

    public abstract void onDestroy(GameState gameState);

    public abstract Building duplicate();

    public void setPosition(final Pair<Integer> position) {
        this.position = position;
        vecPosition = new Vector2(position.getLeft(), position.getRight());
    }

    public Pair<Integer> getPosition() {
        return position;
    }
}
