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
import com.hycap.dbt.GameState;
import com.hycap.dbt.SkinClass;

public class Recycle2Card implements ActionCard, BuyableCard {
    public static Texture texture;
    @Override
    public boolean tryPlayCard(GameState gameState, Stage stage) {
        if (!gameState.deck.drawNewCard()) {
            return false;
        }
        if (!gameState.deck.drawNewCard()) {
            return false;
        }
        gameState.deck.discardCard(this);
        final Table queryTable = new Table();
        queryTable.setFillParent(true);

        Label label = new Label("Pick 2 cards to discard", SkinClass.skin);
        queryTable.add(label).row();

        final Table cardTable = new Table();
        queryTable.setFillParent(true);
        queryTable.add(cardTable);

        stage.addActor(queryTable);

        gameState.blocked = true;
        final int[] cardsLeftToDiscard = {2};
        for (final Card card : GameState.gameState.deck.getHand()) {
            TextureRegionDrawable image = new TextureRegionDrawable(new TextureRegion(card.getTexture()));
            image.setMinSize(108, 192);
            final ImageButton imageButton = new ImageButton(image, image);
            cardTable.add(imageButton);
            imageButton.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    GameState.gameState.deck.discardCard(card);
                    imageButton.remove();
                    --cardsLeftToDiscard[0];
                    if (cardsLeftToDiscard[0] <= 0) {
                        GameState.gameState.blocked = false;
                        queryTable.remove();
                    }
                    return true;
                }
            });
        }
        return true;
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
