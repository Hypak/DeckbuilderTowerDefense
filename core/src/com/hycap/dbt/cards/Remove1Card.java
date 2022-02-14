package com.hycap.dbt.cards;

import com.badlogic.gdx.graphics.Texture;

public class Remove1Card implements Card {
    public static Texture texture;

    @Override
    public int getEnergyCost() {
        return 1;
    }

    @Override
    public String getName() {
        return "Remove 1";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }
}
