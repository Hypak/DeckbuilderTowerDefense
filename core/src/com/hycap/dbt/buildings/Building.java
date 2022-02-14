package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.GameState;

public interface Building{
    String getName();
    Texture getTexture();
    void onCreate(GameState gameState);
    void onDestroy(GameState gameState);

}
