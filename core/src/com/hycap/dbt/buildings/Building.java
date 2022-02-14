package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.GameState;
import com.hycap.dbt.Pair;

public interface Building {
    String getName();
    Texture getTexture();
    void onCreate(GameState gameState);
    void onDestroy(GameState gameState);
    void setPosition(Pair<Integer> position);
    Pair<Integer> getPosition();
    Building duplicate();
}
