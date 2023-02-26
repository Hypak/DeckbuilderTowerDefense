package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.GameState;
import com.hycap.dbt.cards.BuyCard;

public class LibraryBuilding extends AttackableBuilding {
    public static Texture texture;
    private static final int buyCardIncreaseCount = 1;
    @Override
    public String getName() {
        return "Library";
    }

    @Override
    public String getInfo() {
        int _buyCardIncreaseCount = buyCardIncreaseCount;
        if (onRift) {
            _buyCardIncreaseCount *= 2;
        }
        return "Get " + _buyCardIncreaseCount + " more card to choose from when buying a card.";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public void onCreate(final GameState gameState, final boolean onRift) {
        health = 25;
        BuyCard.shownCardAmount += buyCardIncreaseCount;
        if (onRift) {
            BuyCard.shownCardAmount += buyCardIncreaseCount;
        }

        super.onCreate(gameState, onRift);
    }

    @Override
    public void onDestroy(final GameState gameState) {
        BuyCard.shownCardAmount -= buyCardIncreaseCount;
        if (onRift) {
            BuyCard.shownCardAmount -= buyCardIncreaseCount;
        }
    }

    @Override
    public Building duplicate() {
        return new LibraryBuilding();
    }
}
