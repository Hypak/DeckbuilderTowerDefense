package com.hycap.dbt.cards;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.Deck;

public class Draw2Card implements ActionCard {
    public static Texture texture;

    @Override
    public String getName() {
        return "Draw 2";
    }

    @Override
    public int getEnergyCost() {
        return 1;
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public boolean tryPlayCard(Deck deck) {
        if (!deck.drawNewCard()) {
            return false;  // No card to draw
        }
        deck.drawNewCard();
        return true;
    }
}
