package com.hycap.dbt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.hycap.dbt.cards.Card;
import com.hycap.dbt.cards.GetCardInfo;

public class UIManager {
    static Stage stage;
    static Table handTable;
    static Label goldDisplay;
    static Label energyDisplay;
    static TextButton endTurnButton;
    static Label cardCounts;
    static Table resourceTable;

    static Label cardInfo;
    static Table cardInfoTable;
    static Label roundInfo;
    static Table roundInfoTable;

    static TextButton fastForwardButton;
    static Table fastForwardTable;

    public static void create(final MyGdxGame myGdxGame) {
        stage = new Stage();
        handTable = new Table();
        cardCounts = new Label("Loading...", SkinClass.skin);
        goldDisplay = new Label("Loading...", SkinClass.skin);
        energyDisplay = new Label("Loading...", SkinClass.skin);
        // energyDisplay.setFillParent(true);
        endTurnButton = new TextButton("End Turn", SkinClass.skin);
        endTurnButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!GameState.gameState.blocked) {
                    myGdxGame.newTurn();
                }
            }
        });

        resourceTable = new Table();
        resourceTable.add(cardCounts).row();
        resourceTable.add(goldDisplay).row();
        resourceTable.add(energyDisplay).row();
        resourceTable.add(endTurnButton).row();
        resourceTable.padBottom(30);
        resourceTable.padRight(30);
        resourceTable.setFillParent(true);
        resourceTable.align(Align.bottomRight);

        cardInfo = new Label("", SkinClass.skin);
        cardInfo.setAlignment(Align.right);
        cardInfo.setWrap(true);
        cardInfoTable = new Table();
        cardInfoTable.add(cardInfo);
        cardInfoTable.setFillParent(true);
        cardInfoTable.align(Align.right);
        cardInfoTable.padRight(30);

        roundInfo = new Label("Loading...", SkinClass.skin);
        roundInfoTable = new Table();
        roundInfoTable.add(roundInfo);
        roundInfoTable.setFillParent(true);
        roundInfoTable.align(Align.topLeft);
        roundInfoTable.padTop(30);
        roundInfoTable.padLeft(30);

        fastForwardButton = new TextButton(">>>", SkinClass.skin);
        fastForwardButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameState.gameState.toggleFastForward();
            }
        });
        fastForwardTable = new Table();
        fastForwardTable.add(fastForwardButton);
        fastForwardTable.padTop(30);
        fastForwardTable.setFillParent(true);
        fastForwardTable.align(Align.top);

        stage.addActor(handTable);
        stage.addActor(resourceTable);
        stage.addActor(cardInfoTable);
        stage.addActor(roundInfoTable);
        stage.addActor(fastForwardTable);
    }

    public static void render(MyGdxGame myGdxGame) {
        updateHandTable(myGdxGame);
        updateDisplays();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public static void updateHandTable(final MyGdxGame myGdxGame) {
        handTable.reset();
        handTable.setFillParent(true);
        handTable.bottom();

        int i = 0;
        for (final Card card : GameState.gameState.deck.getHand()) {
            TextureRegionDrawable image = new TextureRegionDrawable(new TextureRegion(card.getTexture()));
            image.setMinSize(108, 192);
            final ImageButton imageButton = new ImageButton(image, image);
            handTable.add(imageButton);
            final int finalIndex = i;
            imageButton.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    myGdxGame.selectedIndex = finalIndex;
                    cardInfo.setText(GetCardInfo.getInfo(card));
                    return true;
                }
            });
            ++i;
        }
    }

    public static void updateDisplays() {
        energyDisplay.setText(GameState.gameState.currentEnergy + " / " + GameState.gameState.baseEnergy + " Energy");
        goldDisplay.setText(GameState.gameState.gold + "(+" + GameState.gameState.goldPerTurn + ") / " + GameState.gameState.maxGold + " Gold");
        cardCounts.setText(GameState.gameState.deck.getDrawPile().size() + " Draw, "
                + GameState.gameState.deck.getCards().size() + " Total");
        roundInfo.setText("Radius: " + GameState.gameState.map.currentRadius + " / " + GameState.gameState.map.WIDTH / 2);
    }


}
