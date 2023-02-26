package com.hycap.dbt.cards;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.hycap.dbt.GameState;
import com.hycap.dbt.SkinClass;

public class Remove1Card implements ActionCard, BuyableCard {
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

    @Override
    public Card duplicate() {
        return new Remove1Card();
    }

    @Override
    public boolean tryPlayCard(final GameState gameState, final Stage stage) {
        if (gameState.deck.getHand().size() > 1) {
            final Card thisCard = this;

            final Table queryTable = new Table();
            queryTable.setFillParent(true);

            final Label label = new Label("Pick card to remove", SkinClass.skin);
            queryTable.add(label).row();

            final Table cardTable = new Table();
            queryTable.setFillParent(true);
            queryTable.add(cardTable);


            stage.addActor(queryTable);

            gameState.blocked = true;


            for (final Card card : gameState.deck.getHand()) {
                final TextureRegionDrawable image = new TextureRegionDrawable(new TextureRegion(card.getTexture()));
                image.setMinSize(108, 192);
                final ImageButton imageButton = new ImageButton(image, image);
                cardTable.add(imageButton);
                imageButton.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
                        gameState.deck.removeCard(card);
                        gameState.deck.discardCard(thisCard);
                        gameState.blocked = false;
                        queryTable.remove();
                        return true;
                    }
                });
            }
            final TextButton passButton = new TextButton("Pass", SkinClass.skin);
            passButton.addListener(new InputListener() {
                @Override
                public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
                    gameState.deck.discardCard(thisCard);
                    gameState.blocked = false;
                    queryTable.remove();
                    return true;
                }
            });
            queryTable.row();
            queryTable.add(passButton);
            return true;
        }
        return false;
    }

    @Override
    public String getInfo() {
        return "Permanently remove 1 card from your hand.";
    }

    @Override
    public int getBuyCost() {
        return 7;
    }
}
