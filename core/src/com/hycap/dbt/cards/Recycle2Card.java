package com.hycap.dbt.cards;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.hycap.dbt.Deck;
import com.hycap.dbt.GameState;
import com.hycap.dbt.SkinClass;
import com.hycap.dbt.UIManager;

public class Recycle2Card implements ActionCard, BuyableCard {
    public static Texture texture;
    @Override
    public boolean tryPlayCard(final GameState gameState, final Stage stage) {
        final Deck deck = gameState.deck;
        if (!deck.drawNewCard()) {
            return false;
        }
        if (!deck.drawNewCard()) {
            return false;
        }
        deck.discardCard(this);
        deck.cardsLeftToDiscard = 2;
        createQueryTable(gameState, stage);
        return true;
    }

    public static void createQueryTable(final GameState gameState, final Stage stage) {
        if (UIManager.queryTable != null) {
            UIManager.queryTable.remove();
        }
        UIManager.queryTable = new Table();
        UIManager.queryTable.setFillParent(true);

        final Label label = new Label("Pick 2 cards to discard", SkinClass.skin);
        UIManager.queryTable.add(label).row();

        final Table cardTable = new Table();
        UIManager.queryTable.setFillParent(true);
        UIManager.queryTable.add(cardTable);

        stage.addActor(UIManager.queryTable);

        gameState.prompting = true;
        final Deck deck = GameState.gameState.deck;
        for (final Card card : gameState.deck.getHand()) {
            final TextureRegionDrawable image = new TextureRegionDrawable(new TextureRegion(card.getTexture()));
            image.setMinSize(108, 192);
            final ImageButton imageButton = new ImageButton(image, image);
            cardTable.add(imageButton);
            imageButton.addListener(new InputListener() {
                @Override
                public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
                    GameState.gameState.deck.discardCard(card);
                    imageButton.remove();
                    --deck.cardsLeftToDiscard;
                    if (deck.cardsLeftToDiscard <= 0) {
                        GameState.gameState.prompting = false;
                        UIManager.queryTable.remove();
                    }
                    return true;
                }
            });
        }
    }

    @Override
    public int getEnergyCost() {
        return 0;
    }

    @Override
    public int getBuyCost() {
        return 4;
    }

    @Override
    public String getName() {
        return "Recycle";
    }

    @Override
    public String getInfo() {
        return "Draw 2 cards then discard 2 cards";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public Card duplicate() {
        return new Recycle2Card();
    }
}
