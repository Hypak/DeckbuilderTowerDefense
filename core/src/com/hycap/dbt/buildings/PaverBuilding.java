package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.GameState;
import com.hycap.dbt.cards.Card;
import com.hycap.dbt.cards.Path0EnergyCard;

public class PaverBuilding extends AttackableBuilding {
    public static Texture texture;
    @Override
    public String getName() {
        return "Paver";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public void onCreate(GameState gameState) {
        GameState.gameState.freeCardsPerTurn.add(new Path0EnergyCard());
        super.health = 50;
    }

    @Override
    public void onDestroy(GameState gameState) {
        for (Card card : GameState.gameState.freeCardsPerTurn) {
            if (card instanceof Path0EnergyCard) {
                GameState.gameState.freeCardsPerTurn.remove(card);
                return;
            }
        }
    }

    @Override
    public Building duplicate() {
        return new PaverBuilding();
    }
}
