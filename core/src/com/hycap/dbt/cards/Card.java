package com.hycap.dbt.cards;

import com.badlogic.gdx.graphics.Texture;

public interface Card {
    int getEnergyCost();
    String getName();
    Texture getTexture();
}
