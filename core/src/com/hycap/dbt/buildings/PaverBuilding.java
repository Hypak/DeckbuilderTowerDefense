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
    public String getInfo() {
        if (onRift) {
            return "Produce two free paths every turn.";
        } else {
            return "Produce a free path every turn.";
        }
    }

    @Override
    public void onCreate(final GameState gameState, final boolean onRift) {
        GameState.gameState.freeCardsPerTurn.add(new Path0EnergyCard());
        if (onRift) {
            GameState.gameState.freeCardsPerTurn.add(new Path0EnergyCard());
        }
        health = 50;
        super.onCreate(gameState, onRift);
    }

    @Override
    public void onDestroy(final GameState gameState) {
        int removeCount = 1;
        if (onRift) {
            ++removeCount;
        }
        for (final Card card : GameState.gameState.freeCardsPerTurn) {
            if (card instanceof Path0EnergyCard) {
                GameState.gameState.freeCardsPerTurn.remove(card);
                --removeCount;
                if (removeCount < 1) {
                    return;
                }
            }
        }
    }

    @Override
    public Building duplicate() {
        return new PaverBuilding();
    }
}
