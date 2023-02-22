package com.hycap.dbt.cards;

import com.badlogic.gdx.graphics.Texture;

public class Path0EnergyCard extends PathCard implements ExhaustCard, EtherealCard {
    public static Texture texture;

    @Override
    public int getEnergyCost() {
        return 0;
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public Card duplicate() {
        return new Path0EnergyCard();
    }
}
