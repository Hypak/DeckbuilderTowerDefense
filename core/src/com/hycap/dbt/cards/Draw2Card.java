package com.hycap.dbt.cards;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hycap.dbt.Deck;
import com.hycap.dbt.GameState;

public class Draw2Card implements ActionCard, BuyableCard {
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
    public Card duplicate() {
        return new Draw2Card();
    }

    @Override
    public boolean tryPlayCard(GameState gameState, Stage stage) {
        if (!gameState.deck.drawNewCard()) {
            return false;  // No card to draw
        }
        gameState.deck.drawNewCard();
        gameState.deck.discardCard(this);
        return true;
    }

    @Override
    public int getBuyCost() {
        return 4;
    }
}
